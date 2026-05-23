package com.library.service;

import com.library.dto.mapper.TransactionMapper;
import com.library.dto.request.IssueRequest;
import com.library.dto.response.TransactionResponse;
import com.library.entity.Book;
import com.library.entity.Member;
import com.library.entity.enums.MemberStatus;
import com.library.entity.Transaction;
import com.library.entity.enums.TransactionStatus;
import com.library.exception.InvalidOperationException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing book issue/return transactions.
 * Enforces business rules around book availability, member status, and duplicate issues.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    /**
     * Issues a book to a member after enforcing all business rules.
     *
     * <p>Business rules enforced:
     * <ol>
     *   <li>Book must exist and not be soft-deleted</li>
     *   <li>Member must exist</li>
     *   <li>Member must have ACTIVE status</li>
     *   <li>Book must have available copies</li>
     *   <li>Member must not already have this book issued</li>
     * </ol>
     *
     * @param request the issue request containing bookId and memberId
     * @return the created transaction response
     * @throws ResourceNotFoundException if the book or member is not found
     * @throws InvalidOperationException if any business rule is violated
     */
    public TransactionResponse issueBook(IssueRequest request) {
        // 1. Find book (must exist and not be deleted)
        Book book = bookRepository.findByIdAndDeletedFalse(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", request.getBookId()));

        // 2. Find member (must exist)
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", request.getMemberId()));

        // 3. Check member is active
        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new InvalidOperationException("Cannot issue book to inactive member");
        }

        // 4. Check book availability
        if (book.getAvailableCopies() <= 0) {
            throw new InvalidOperationException("No copies available for this book");
        }

        // 5. Check for existing issued transaction for same book + member
        if (transactionRepository.existsByBookIdAndMemberIdAndStatus(
                request.getBookId(), request.getMemberId(), TransactionStatus.ISSUED)) {
            throw new InvalidOperationException("Member already has this book issued");
        }

        // 6. Create transaction
        Transaction transaction = Transaction.builder()
                .book(book)
                .member(member)
                .issuedAt(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS))
                .status(TransactionStatus.ISSUED)
                .build();

        // 7. Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // 8. Save and return
        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toResponse(savedTransaction);
    }

    /**
     * Processes the return of a previously issued book.
     *
     * <p>Business rules enforced:
     * <ol>
     *   <li>Transaction must exist</li>
     *   <li>Transaction must have ISSUED status (not already returned)</li>
     * </ol>
     *
     * @param transactionId the ID of the transaction to return
     * @return the updated transaction response
     * @throws ResourceNotFoundException if the transaction is not found
     * @throws InvalidOperationException if the transaction is not in ISSUED status
     */
    public TransactionResponse returnBook(Long transactionId) {
        // 1. Find transaction
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        // 2. Check transaction is currently issued
        if (transaction.getStatus() != TransactionStatus.ISSUED) {
            throw new InvalidOperationException("This transaction is already returned/not active");
        }

        // 3. Update transaction
        transaction.setReturnedAt(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.RETURNED);

        // 4. Increment available copies
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        // 5. Save and return
        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.toResponse(savedTransaction);
    }

    /**
     * Retrieves all transactions ordered by creation date descending.
     *
     * @return the list of all transaction responses
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return TransactionMapper.toResponseList(transactions);
    }

    /**
     * Retrieves all transactions for a specific member.
     *
     * @param memberId the member ID
     * @return the list of transaction responses for the member
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByMember(Long memberId) {
        List<Transaction> transactions = transactionRepository.findByMemberId(memberId);
        return TransactionMapper.toResponseList(transactions);
    }

    /**
     * Retrieves all transactions for a specific book.
     *
     * @param bookId the book ID
     * @return the list of transaction responses for the book
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByBook(Long bookId) {
        List<Transaction> transactions = transactionRepository.findByBookId(bookId);
        return TransactionMapper.toResponseList(transactions);
    }

    /**
     * Returns the count of currently active (ISSUED) transactions.
     *
     * @return the active issue count
     */
    @Transactional(readOnly = true)
    public long getActiveIssueCount() {
        return transactionRepository.countByStatus(TransactionStatus.ISSUED);
    }
}

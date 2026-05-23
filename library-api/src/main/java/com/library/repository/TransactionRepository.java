package com.library.repository;

import com.library.entity.Transaction;
import com.library.entity.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Transaction} entity operations.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for a specific member.
     *
     * @param memberId the member ID
     * @return list of transactions for the member
     */
    List<Transaction> findByMemberId(Long memberId);

    /**
     * Finds all transactions for a specific book.
     *
     * @param bookId the book ID
     * @return list of transactions for the book
     */
    List<Transaction> findByBookId(Long bookId);

    /**
     * Finds a transaction by book ID, member ID, and status.
     *
     * @param bookId   the book ID
     * @param memberId the member ID
     * @param status   the transaction status
     * @return an Optional containing the matching transaction if found
     */
    Optional<Transaction> findByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, TransactionStatus status);

    /**
     * Retrieves all transactions with the given status.
     *
     * @param status the transaction status to filter by
     * @return list of transactions with the specified status
     */
    List<Transaction> findByStatus(TransactionStatus status);

    /**
     * Counts all transactions with the given status.
     *
     * @param status the transaction status to count
     * @return the count of transactions with the specified status
     */
    long countByStatus(TransactionStatus status);

    /**
     * Checks whether a transaction exists for a given book, member, and status.
     *
     * @param bookId   the book ID
     * @param memberId the member ID
     * @param status   the transaction status
     * @return true if such a transaction exists
     */
    boolean existsByBookIdAndMemberIdAndStatus(Long bookId, Long memberId, TransactionStatus status);
}

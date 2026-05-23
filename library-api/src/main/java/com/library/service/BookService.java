package com.library.service;

import com.library.dto.mapper.BookMapper;
import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.entity.Book;
import com.library.exception.DuplicateResourceException;
import com.library.exception.InvalidOperationException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing library books.
 * Handles CRUD operations with soft-delete support and ISBN uniqueness enforcement.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Creates a new book after validating ISBN uniqueness and copy counts.
     *
     * @param request the book creation request
     * @return the created book response
     * @throws DuplicateResourceException if a book with the same ISBN already exists
     * @throws InvalidOperationException  if available copies exceed total copies
     */
    public BookResponse createBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book", "isbn", request.getIsbn());
        }

        validateCopyCounts(request);

        Book book = BookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);
        return BookMapper.toResponse(savedBook);
    }

    /**
     * Updates an existing book identified by its ID.
     * Validates ISBN uniqueness if it has changed and verifies copy counts.
     *
     * @param id      the book ID
     * @param request the book update request
     * @return the updated book response
     * @throws ResourceNotFoundException  if the book is not found or is soft-deleted
     * @throws DuplicateResourceException if the new ISBN conflicts with another book
     * @throws InvalidOperationException  if available copies exceed total copies
     */
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        // Check ISBN uniqueness only if it has changed
        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book", "isbn", request.getIsbn());
        }

        validateCopyCounts(request);

        BookMapper.updateEntity(book, request);
        Book updatedBook = bookRepository.save(book);
        return BookMapper.toResponse(updatedBook);
    }

    /**
     * Soft-deletes a book by setting its {@code deleted} flag to {@code true}.
     *
     * @param id the book ID
     * @throws ResourceNotFoundException if the book is not found or is already deleted
     */
    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        book.setDeleted(true);
        bookRepository.save(book);
    }

    /**
     * Retrieves a single book by its ID (excluding soft-deleted books).
     *
     * @param id the book ID
     * @return the book response
     * @throws ResourceNotFoundException if the book is not found or is soft-deleted
     */
    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        return BookMapper.toResponse(book);
    }

    /**
     * Retrieves all non-deleted books.
     *
     * @return the list of book responses
     */
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findByDeletedFalse();
        return BookMapper.toResponseList(books);
    }

    /**
     * Searches for books matching the given search term across title, author, and ISBN.
     *
     * @param searchTerm the search keyword
     * @return the list of matching book responses
     */
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String searchTerm) {
        List<Book> books = bookRepository.searchBooks(searchTerm);
        return BookMapper.toResponseList(books);
    }

    /**
     * Returns the total count of non-deleted books.
     *
     * @return the book count
     */
    @Transactional(readOnly = true)
    public long getBookCount() {
        return bookRepository.countByDeletedFalse();
    }

    /**
     * Validates that available copies do not exceed total copies.
     *
     * @param request the book request to validate
     * @throws InvalidOperationException if available copies exceed total copies
     */
    private void validateCopyCounts(BookRequest request) {
        if (request.getAvailableCopies() > request.getTotalCopies()) {
            throw new InvalidOperationException(
                    "Available copies (" + request.getAvailableCopies()
                            + ") cannot exceed total copies (" + request.getTotalCopies() + ")");
        }
    }
}

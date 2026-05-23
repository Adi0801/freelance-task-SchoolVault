package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Book} entity operations.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Finds a book by its ISBN.
     *
     * @param isbn the ISBN to search for
     * @return an Optional containing the book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Finds a non-deleted book by its ID.
     *
     * @param id the book ID
     * @return an Optional containing the book if found and not deleted
     */
    Optional<Book> findByIdAndDeletedFalse(Long id);

    /**
     * Retrieves all non-deleted books.
     *
     * @return list of active books
     */
    List<Book> findByDeletedFalse();

    /**
     * Searches for non-deleted books by title, author, ISBN, or category
     * containing the given search term (case-insensitive).
     *
     * @param search the search term
     * @return list of matching books
     */
    @Query("SELECT b FROM Book b WHERE b.deleted = false AND (" +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(b.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Book> searchBooks(@Param("search") String search);

    /**
     * Checks whether a book with the given ISBN exists.
     *
     * @param isbn the ISBN to check
     * @return true if a book with the ISBN exists
     */
    boolean existsByIsbn(String isbn);

    /**
     * Counts all non-deleted books.
     *
     * @return the count of active books
     */
    long countByDeletedFalse();
}

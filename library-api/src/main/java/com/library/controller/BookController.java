package com.library.controller;

import com.library.dto.request.BookRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.BookResponse;
import com.library.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing books in the library system.
 * Provides CRUD operations and search functionality for books.
 */
@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Book Management", description = "APIs for managing books")
public class BookController {

    private final BookService bookService;

    /**
     * Creates a new book in the library.
     *
     * @param request the book creation request containing book details
     * @return the created book wrapped in an API response with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new book", description = "Adds a new book to the library catalog")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Book created successfully"));
    }

    /**
     * Updates an existing book by its ID.
     *
     * @param id      the ID of the book to update
     * @param request the book update request containing updated details
     * @return the updated book wrapped in an API response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing book", description = "Updates the details of an existing book by its ID")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @Parameter(description = "ID of the book to update") @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Book updated successfully"));
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id the ID of the book to delete
     * @return an API response confirming the deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Removes a book from the library catalog by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteBook(
            @Parameter(description = "ID of the book to delete") @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Book deleted successfully"));
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id the ID of the book to retrieve
     * @return the requested book wrapped in an API response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieves the details of a specific book by its ID")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(
            @Parameter(description = "ID of the book to retrieve") @PathVariable Long id) {
        BookResponse response = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Retrieves all books, optionally filtered by a search term.
     * When a search term is provided, books are filtered by title, author, or ISBN.
     *
     * @param search optional search term to filter books
     * @return a list of books wrapped in an API response
     */
    @GetMapping
    @Operation(summary = "Get all books or search", description = "Retrieves all books, or searches by title/author/ISBN when a search term is provided")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(
            @Parameter(description = "Optional search term to filter books")
            @RequestParam(required = false) String search) {
        List<BookResponse> responses;
        if (search != null && !search.isBlank()) {
            responses = bookService.searchBooks(search);
        } else {
            responses = bookService.getAllBooks();
        }
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

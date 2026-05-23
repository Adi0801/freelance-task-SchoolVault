package com.library.dto.mapper;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between {@link Book} entities and Book DTOs.
 */
@Component
public class BookMapper {

    /**
     * Converts a {@link Book} entity to a {@link BookResponse} DTO.
     *
     * @param book the book entity
     * @return the book response DTO
     */
    public static BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .shelfLocation(book.getShelfLocation())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    /**
     * Converts a {@link BookRequest} DTO to a new {@link Book} entity.
     * Sets {@code deleted} to {@code false} by default.
     *
     * @param request the book request DTO
     * @return a new book entity
     */
    public static Book toEntity(BookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .category(request.getCategory())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getAvailableCopies())
                .shelfLocation(request.getShelfLocation())
                .deleted(false)
                .build();
    }

    /**
     * Updates an existing {@link Book} entity's fields from a {@link BookRequest} DTO.
     *
     * @param book    the existing book entity to update
     * @param request the book request containing updated values
     */
    public static void updateEntity(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());
        book.setShelfLocation(request.getShelfLocation());
    }

    /**
     * Converts a list of {@link Book} entities to a list of {@link BookResponse} DTOs.
     *
     * @param books the list of book entities
     * @return the list of book response DTOs
     */
    public static List<BookResponse> toResponseList(List<Book> books) {
        return books.stream()
                .map(BookMapper::toResponse)
                .collect(Collectors.toList());
    }
}

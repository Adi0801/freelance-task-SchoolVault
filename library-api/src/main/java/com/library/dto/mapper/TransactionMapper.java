package com.library.dto.mapper;

import com.library.dto.response.TransactionResponse;
import com.library.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between {@link Transaction} entities and Transaction DTOs.
 */
@Component
public class TransactionMapper {

    /**
     * Converts a {@link Transaction} entity to a {@link TransactionResponse} DTO.
     * Denormalizes related book and member fields for API convenience.
     *
     * @param txn the transaction entity (must have book and member eagerly loaded)
     * @return the transaction response DTO
     */
    public static TransactionResponse toResponse(Transaction txn) {
        return TransactionResponse.builder()
                .id(txn.getId())
                .bookId(txn.getBook().getId())
                .bookTitle(txn.getBook().getTitle())
                .bookIsbn(txn.getBook().getIsbn())
                .memberId(txn.getMember().getId())
                .memberName(txn.getMember().getName())
                .memberEmail(txn.getMember().getEmail())
                .issuedAt(txn.getIssuedAt())
                .dueDate(txn.getDueDate())
                .returnedAt(txn.getReturnedAt())
                .status(txn.getStatus().name())
                .createdAt(txn.getCreatedAt())
                .build();
    }

    /**
     * Converts a list of {@link Transaction} entities to a list of
     * {@link TransactionResponse} DTOs.
     *
     * @param transactions the list of transaction entities
     * @return the list of transaction response DTOs
     */
    public static List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionMapper::toResponse)
                .collect(Collectors.toList());
    }
}

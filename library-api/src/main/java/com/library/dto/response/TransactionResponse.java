package com.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO representing a library transaction (book issue/return).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private LocalDateTime issuedAt;
    private LocalDate dueDate;
    private LocalDateTime returnedAt;
    private String status;
    private LocalDateTime createdAt;
}

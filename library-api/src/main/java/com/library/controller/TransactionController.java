package com.library.controller;

import com.library.dto.request.IssueRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.TransactionResponse;
import com.library.service.TransactionService;

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
 * REST controller for managing book transactions (issue and return).
 * Provides endpoints for issuing books, returning books, and querying transaction history.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "APIs for issuing and returning books")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Issues a book to a member.
     *
     * @param request the issue request containing member ID and book ID
     * @return the created transaction wrapped in an API response with HTTP 201 status
     */
    @PostMapping("/issue")
    @Operation(summary = "Issue a book", description = "Issues a book to a library member, creating a new transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> issueBook(
            @Valid @RequestBody IssueRequest request) {
        TransactionResponse response = transactionService.issueBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Book issued successfully"));
    }

    /**
     * Processes the return of a previously issued book.
     *
     * @param id the transaction ID of the book to return
     * @return the updated transaction wrapped in an API response
     */
    @PostMapping("/{id}/return")
    @Operation(summary = "Return a book", description = "Processes the return of a previously issued book")
    public ResponseEntity<ApiResponse<TransactionResponse>> returnBook(
            @Parameter(description = "ID of the transaction to complete") @PathVariable Long id) {
        TransactionResponse response = transactionService.returnBook(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Book returned successfully"));
    }

    /**
     * Retrieves all transactions in the system.
     *
     * @return a list of all transactions wrapped in an API response
     */
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves the complete list of all book transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        List<TransactionResponse> responses = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Retrieves all transactions for a specific member.
     *
     * @param memberId the ID of the member whose transactions to retrieve
     * @return a list of transactions for the specified member
     */
    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get transactions by member", description = "Retrieves all transactions associated with a specific member")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByMember(
            @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<TransactionResponse> responses = transactionService.getTransactionsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    /**
     * Retrieves all transactions for a specific book.
     *
     * @param bookId the ID of the book whose transactions to retrieve
     * @return a list of transactions for the specified book
     */
    @GetMapping("/book/{bookId}")
    @Operation(summary = "Get transactions by book", description = "Retrieves all transactions associated with a specific book")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByBook(
            @Parameter(description = "ID of the book") @PathVariable Long bookId) {
        List<TransactionResponse> responses = transactionService.getTransactionsByBook(bookId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

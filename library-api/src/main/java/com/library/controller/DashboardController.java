package com.library.controller;

import com.library.dto.response.ApiResponse;
import com.library.service.BookService;
import com.library.service.MemberService;
import com.library.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST controller for providing dashboard statistics.
 * Aggregates key metrics from books, members, and transactions.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard statistics")
public class DashboardController {

    private final BookService bookService;
    private final MemberService memberService;
    private final TransactionService transactionService;

    /**
     * Retrieves aggregated dashboard statistics including total books,
     * active members, and currently active book issues.
     *
     * @return a map of dashboard statistics wrapped in an API response
     */
    @GetMapping("/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieves aggregated library statistics including total books, active members, and active issues")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getDashboardStats() {
        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("totalBooks", bookService.getBookCount());
        stats.put("activeMembers", memberService.getActiveMemberCount());
        stats.put("activeIssues", transactionService.getActiveIssueCount());
        return ResponseEntity.ok(ApiResponse.success(stats, "Dashboard statistics retrieved successfully"));
    }
}

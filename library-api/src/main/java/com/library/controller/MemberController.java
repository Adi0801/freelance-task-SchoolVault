package com.library.controller;

import com.library.dto.request.MemberRequest;
import com.library.dto.response.ApiResponse;
import com.library.dto.response.MemberResponse;
import com.library.service.MemberService;

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
 * REST controller for managing library members.
 * Provides CRUD operations and member deactivation functionality.
 */
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member Management", description = "APIs for managing members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Registers a new member in the library system.
     *
     * @param request the member registration request containing member details
     * @return the created member wrapped in an API response with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Register a new member", description = "Creates a new library member account")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(
            @Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Member created successfully"));
    }

    /**
     * Updates an existing member's details by their ID.
     *
     * @param id      the ID of the member to update
     * @param request the member update request containing updated details
     * @return the updated member wrapped in an API response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a member", description = "Updates the details of an existing member by their ID")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @Parameter(description = "ID of the member to update") @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        MemberResponse response = memberService.updateMember(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Member updated successfully"));
    }

    /**
     * Deactivates a member by their ID.
     * Uses PATCH since this is a partial state change rather than a full update.
     *
     * @param id the ID of the member to deactivate
     * @return the deactivated member wrapped in an API response
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a member", description = "Deactivates a library member, preventing further book issuance")
    public ResponseEntity<ApiResponse<MemberResponse>> deactivateMember(
            @Parameter(description = "ID of the member to deactivate") @PathVariable Long id) {
        MemberResponse response = memberService.deactivateMember(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Member deactivated successfully"));
    }

    /**
     * Retrieves a member by their ID.
     *
     * @param id the ID of the member to retrieve
     * @return the requested member wrapped in an API response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a member by ID", description = "Retrieves the details of a specific member by their ID")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(
            @Parameter(description = "ID of the member to retrieve") @PathVariable Long id) {
        MemberResponse response = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Retrieves all registered members.
     *
     * @return a list of all members wrapped in an API response
     */
    @GetMapping
    @Operation(summary = "Get all members", description = "Retrieves a list of all registered library members")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> responses = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}

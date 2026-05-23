package com.library.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for returning a book.
 * The transaction is identified via path variable; this DTO carries optional metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRequest {

    /**
     * Optional notes to record alongside the return (e.g., book condition remarks).
     */
    private String notes;
}

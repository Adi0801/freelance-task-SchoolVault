package com.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper providing a consistent response structure
 * across all endpoints.
 *
 * @param <T> the type of the response data payload
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    /**
     * Creates a successful response with data and a custom message.
     *
     * @param data    the response payload
     * @param message the success message
     * @param <T>     the type of the response data
     * @return a successful {@link ApiResponse}
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a successful response with data and a default message.
     *
     * @param data the response payload
     * @param <T>  the type of the response data
     * @return a successful {@link ApiResponse} with default message
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    /**
     * Creates an error response with the given message and no data.
     *
     * @param message the error message
     * @param <T>     the type of the response data
     * @return an error {@link ApiResponse}
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

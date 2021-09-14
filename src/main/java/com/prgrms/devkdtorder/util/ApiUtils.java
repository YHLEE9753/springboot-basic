package com.prgrms.devkdtorder.util;

import lombok.*;
import org.springframework.http.HttpStatus;

public class ApiUtils {

    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> error(Throwable throwable, HttpStatus status) {
        return new ApiResponse<>(false, null, new ApiError(throwable, status));
    }

    public static ApiResponse<?> error(String message, HttpStatus status) {
        return new ApiResponse<>(false, null, new ApiError(message, status));
    }

    @Getter
    @RequiredArgsConstructor
    @ToString(of = {"success", "data", "error"})
    public static class ApiResponse<T> {

        private final boolean success;
        private final T data;
        private final ApiError error;

    }

    @Getter
    @ToString(of = {"message", "status"})
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ApiError {

        private String message;
        private int status;

        ApiError(Throwable throwable, HttpStatus status) {
            this(throwable.getMessage(), status);
        }

        ApiError(String message, HttpStatus status) {
            this.message = message;
            this.status = status.value();
        }
    }
}






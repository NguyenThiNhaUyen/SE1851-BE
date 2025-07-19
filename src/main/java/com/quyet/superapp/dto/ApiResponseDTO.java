package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
<<<<<<< HEAD
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private String message;

    public ApiResponseDTO(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // ✅ Factory methods (giúp Controller viết gọn gàng)
    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return new ApiResponseDTO<>(true, data, message);
    }

    public static <T> ApiResponseDTO<T> success(String message) {
        return new ApiResponseDTO<>(true, null, message);
    }

    public static <T> ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>(false, null, message);
=======
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    // ✅ Constructor dùng khi chỉ muốn trả success và message
    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }

    // ✅ Static method giúp gọi gọn gàng trong controller
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>(false, message, null);
>>>>>>> origin/main
    }
}

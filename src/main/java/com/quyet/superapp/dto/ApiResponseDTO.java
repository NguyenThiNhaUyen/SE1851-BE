package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponseDTO <T>{
    private boolean success;
    private String message;
    private T data;

    public ApiResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.data = null;
    }
}

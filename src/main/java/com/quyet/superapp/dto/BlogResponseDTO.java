package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogResponseDTO {
    @NotNull
    private Long blogId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String status;

    private LocalDateTime createdAt;

    @NotBlank
    private String authorName; // hoặc authorId nếu cần chi tiết
}

package com.quyet.superapp.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.quyet.superapp.enums.BlogStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponseDTO {
    @NotNull
    private Long blogId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


    @NotBlank
    private String authorName; // hoặc authorId nếu cần chi tiết

    private String previewContent;
    private BlogStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long authorId;
    private String thumbnailUrl;
    private List<String> tags;
    private Integer viewCount;

}


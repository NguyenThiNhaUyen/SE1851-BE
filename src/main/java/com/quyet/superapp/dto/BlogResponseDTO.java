package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogResponseDTO {
    private Long blogId;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private String authorName; // hoặc authorId nếu cần chi tiết
}

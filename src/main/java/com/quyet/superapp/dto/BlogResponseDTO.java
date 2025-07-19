package com.quyet.superapp.dto;

<<<<<<< HEAD
=======

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

>>>>>>> origin/main
import com.quyet.superapp.enums.BlogStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogResponseDTO {
<<<<<<< HEAD
    private Long blogId;
    private String title;
    private String content;
=======
    @NotNull
    private Long blogId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;


    @NotBlank
    private String authorName; // hoặc authorId nếu cần chi tiết

>>>>>>> origin/main
    private String previewContent;
    private BlogStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
<<<<<<< HEAD
    private String authorName;
=======

>>>>>>> origin/main
    private Long authorId;
    private String thumbnailUrl;
    private List<String> tags;
    private Integer viewCount;
<<<<<<< HEAD
=======

>>>>>>> origin/main
}


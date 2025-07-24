package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    @NotNull
    private Long blogId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String status;

    private LocalDateTime createdAt;

    @NotNull
    private Long authorId;
}

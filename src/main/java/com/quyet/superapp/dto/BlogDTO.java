package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
<<<<<<< HEAD
    private Long blogId;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;
=======
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
>>>>>>> origin/main
    private Long authorId;
}

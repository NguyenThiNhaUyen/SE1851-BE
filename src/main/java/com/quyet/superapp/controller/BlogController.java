package com.quyet.superapp.controller;

import com.quyet.superapp.dto.BlogDTO;
import com.quyet.superapp.dto.BlogRequestDTO;
import com.quyet.superapp.dto.BlogResponseDTO;
import com.quyet.superapp.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
=======
import org.springframework.validation.annotation.Validated;
>>>>>>> origin/main
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
<<<<<<< HEAD
=======
@Validated
>>>>>>> origin/main
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAll());
    }

<<<<<<< HEAD
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> getBlogById(@PathVariable Long id) {
=======

    @GetMapping("/by-id")
    public ResponseEntity<BlogResponseDTO> getBlogById(@RequestParam Long id) {

>>>>>>> origin/main
        return ResponseEntity.ok(blogService.getById(id));
    }

    @PostMapping("/create")
<<<<<<< HEAD
=======

>>>>>>> origin/main
    public ResponseEntity<BlogResponseDTO> createBlog(@RequestBody @Valid BlogRequestDTO blogDto) {
        return ResponseEntity.ok(blogService.save(blogDto));
    }

<<<<<<< HEAD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
=======
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBlog(@RequestParam Long id) {
>>>>>>> origin/main
        blogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

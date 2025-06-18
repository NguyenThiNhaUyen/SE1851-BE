package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.BlogDTO;
import com.quyet.superapp.entity.Blog;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public BlogDTO toDto(Blog blog) {
        return new BlogDTO(
                blog.getBlogId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getStatus(),
                blog.getCreatedAt(),
                blog.getAuthor().getUserId()
        );
    }

    public Blog toEntity(BlogDTO dto) {
        Blog blog = new Blog();
        blog.setBlogId(dto.getBlogId());
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setStatus(dto.getStatus());
        blog.setCreatedAt(dto.getCreatedAt());
        // blog.setAuthor(...) sẽ được gán trong BlogService
        return blog;
    }
}

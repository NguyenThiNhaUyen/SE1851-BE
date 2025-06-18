package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Blog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blog_Id")
    private Long blogId;

    @Column(name = "Title", columnDefinition = "NVARCHAR(200)")
    private String title;

    @ManyToOne
    @JoinColumn(name = "User_Id")
    private User author;

    @Column(name = "Content", columnDefinition = "NVARCHAR(200)")
    private String content;

    @Column(name = "Created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
    private String status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

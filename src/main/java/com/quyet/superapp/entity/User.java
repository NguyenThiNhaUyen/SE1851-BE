package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_Id")
    private Long userId;

    @Column(name = "UserName", columnDefinition = "VARCHAR(100)", nullable = false, unique = true)
    private String username;

    @Column(name = "Password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;

    @Column(name = "Email", columnDefinition = "VARCHAR(50)", nullable = false, unique = true)
    private String email;

    @Column(name = "IsEnable")
    private boolean isEnable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Role_Id")
    private Role role;

    // ✅ Giữ lại duy nhất profile
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserProfile userProfile;
}

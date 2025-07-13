package com.quyet.superapp.entity;

import com.quyet.superapp.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_Id")
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private RoleEnum name;

    // nếu bạn có mappedBy User thì thêm sau
    // @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    // private List<User> users;
}

package com.quyet.superapp.entity;

<<<<<<< HEAD
import com.quyet.superapp.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
=======
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Entity

@Table(name = "Roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
>>>>>>> origin/main
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_Id")
    private Long roleId;

<<<<<<< HEAD
    @Enumerated(EnumType.STRING)
    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private RoleEnum name;

    // nếu bạn có mappedBy User thì thêm sau
    // @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    // private List<User> users;
=======
    @Column(name = "Name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<User> users;

>>>>>>> origin/main
}

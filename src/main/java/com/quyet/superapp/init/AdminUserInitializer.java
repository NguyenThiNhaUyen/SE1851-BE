package com.quyet.superapp.init;

import com.quyet.superapp.entity.Role;
import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.RoleEnum;
import com.quyet.superapp.repository.RoleRepository;
import com.quyet.superapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin123";
        String adminPassword = "admin123";

        if (userRepository.existsByUsername(adminUsername)) {
            System.out.println("Admin user đã tồn tại, bỏ qua tạo mới.");
            return;
        }

        // Tạo Role ADMIN nếu chưa có
        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleEnum.ADMIN);
                    return roleRepository.save(role);
                });

        // Tạo user admin mới
        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        admin.setEnable(true);  // Theo đúng tên biến boolean isEnable
        admin.setEmail("tranquyetcoder@gmail.com");

        userRepository.save(admin);

        System.out.println("Đã tạo user admin mặc định với username: " + adminUsername + " và password: " + adminPassword);
    }
}

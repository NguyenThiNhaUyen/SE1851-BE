package com.quyet.superapp.repository;

import com.quyet.superapp.entity.User;
import com.quyet.superapp.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
    SELECT u FROM User u
    JOIN u.userProfile p
    WHERE p.citizenId = :citizenId
""")
    Optional<User> findByCitizenId(@Param("citizenId") String citizenId);


    // 🔢 Đếm số user đang kích hoạt
    long countByEnableTrue();

    // 🔍 Tìm theo tên role (ADMIN, STAFF, MEMBER...)
    @Query("SELECT u FROM User u WHERE u.role.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    // 🔍 Tìm theo Enum role (nếu role là Enum)
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") RoleEnum role);

    // 🔍 Tìm 1 user đầu tiên theo Role
    Optional<User> findFirstByRole(RoleEnum role);

    // 🔍 Tìm theo username
    Optional<User> findByUsername(String username);

    // 🔍 Tìm user kèm role để giảm lazy loading
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRole(@Param("username") String username);

    // ✅ Check tồn tại
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 🩺 Lấy tất cả bác sĩ
    @Query("""
        SELECT u FROM User u
        JOIN u.userProfile p
        WHERE u.role.name = 'STAFF' AND p.staffPosition = 'Doctor'
    """)
    List<User> findDoctors();

    // 👨‍⚕️ Lấy cả bác sĩ & nhân viên hỗ trợ
    @Query("""
        SELECT u FROM User u
        JOIN u.userProfile p
        WHERE u.role.name = 'STAFF' AND (p.staffPosition = 'Doctor' OR p.staffPosition = 'Staff')
    """)
    List<User> findStaffAndDoctors();

    // 🧑‍⚕️ Tìm theo chức danh staff (có thể truyền null)
    @Query("""
        SELECT u FROM User u
        JOIN u.userProfile p
        WHERE u.role.name = 'STAFF' AND (:position IS NULL OR p.staffPosition = :position)
    """)
    List<User> findByStaffPosition(@Param("position") String position);

    // ✅ Thêm: tìm theo email
    Optional<User> findByEmail(String email);
}

package com.quyet.superapp.repository;

import com.quyet.superapp.entity.BloodType;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
    @Query(value = """
    SELECT TOP 1 bt.Description + bt.Rh
    FROM Donations d
    JOIN BloodTypes bt ON d.blood_type = bt.BloodTypeID
    GROUP BY bt.Description, bt.Rh
    ORDER BY COUNT(*) DESC
    """, nativeQuery = true)
    String findMostCommonBloodType();


    boolean existsByDescriptionAndRh(String description, String rh);



    Optional<BloodType> findByDescription(String description); // ví dụ: "O+"
    Optional<BloodType> findByDescriptionIgnoreCase(String description); // "o+" → "O+"
    List<BloodType> findByDescriptionIn(List<String> descriptions); // ví dụ: ["A+", "B+"]
    List<BloodType> findByDescriptionContaining(String keyword); // chứa chuỗi, ví dụ "O"


=======
import com.quyet.superapp.enums.RhType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloodTypeRepository extends JpaRepository<BloodType, Long> {
    // Có thể thêm custom query nếu cần, ví dụ:

    // ✅ Repository
    Optional<BloodType> findByDescription(String description);
>>>>>>> origin/main
}

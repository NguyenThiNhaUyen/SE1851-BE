
    package com.quyet.superapp.repository;

    import com.quyet.superapp.entity.BloodComponent;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface BloodComponentRepository extends JpaRepository<BloodComponent,Long> {
        // Tìm theo tên thành phần máu: "Hồng cầu", "Huyết tương", "Tiểu cầu"
        Optional<BloodComponent> findByNameIgnoreCase(String name);

        // (tuỳ chọn) Tìm theo code y tế: PRC, FFP, PLT
        Optional<BloodComponent> findByCode(String code);


    }


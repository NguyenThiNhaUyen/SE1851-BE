package com.quyet.superapp.init;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.BloodComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BloodComponentInitializer implements CommandLineRunner {

    private final BloodComponentRepository bloodComponentRepository;

    @Override
    public void run(String... args) {
        if (bloodComponentRepository.count() > 0) {
            System.out.println("🔁 Thành phần máu đã tồn tại, bỏ qua insert.");
            return;
        }

        List<BloodComponent> components = Arrays.asList(
                BloodComponent.builder()
                        .name("Hồng cầu")
                        .code("RBC")
                        .storageTemp("2-6°C")
                        .storageDays(42)
                        .usage("Truyền cho người thiếu máu")
                        .isApheresisCompatible(true)
                        .type(BloodComponentType.RED_BLOOD_CELLS)
                        .build(),

                BloodComponent.builder()
                        .name("Tiểu cầu")
                        .code("PLT")
                        .storageTemp("20-24°C")
                        .storageDays(5)
                        .usage("Truyền cho người giảm tiểu cầu")
                        .isApheresisCompatible(true)
                        .type(BloodComponentType.PLATELETS)
                        .build(),

                BloodComponent.builder()
                        .name("Huyết tương")
                        .code("PLAS")
                        .storageTemp("-18°C")
                        .storageDays(365)
                        .usage("Điều trị rối loạn đông máu")
                        .isApheresisCompatible(false)
                        .type(BloodComponentType.PLASMA)
                        .build()
        );

        bloodComponentRepository.saveAll(components);
        System.out.println("✅ Đã khởi tạo 3 thành phần máu: Hồng cầu, Tiểu cầu, Huyết tương.");
    }
}

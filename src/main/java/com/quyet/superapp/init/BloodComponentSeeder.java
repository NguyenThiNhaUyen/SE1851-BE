package com.quyet.superapp.init;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.enums.BloodComponentType;
import com.quyet.superapp.repository.BloodComponentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BloodComponentSeeder {

    private final BloodComponentRepository repository;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    BloodComponent.builder()
                            .name("Hồng cầu")
                            .code("PRC")
                            .storageTemp("2-6°C")
                            .storageDays(42)
                            .usage("Dùng trong trường hợp thiếu máu nặng")
                            .isApheresisCompatible(true)
                            .type(BloodComponentType.RED_BLOOD_CELLS) // gán đúng enum
                            .build(),
                    BloodComponent.builder()
                            .name("Huyết tương")
                            .code("FFP")
                            .storageTemp("-25°C")
                            .storageDays(365)
                            .usage("Hỗ trợ đông máu, truyền trong sốc")
                            .isApheresisCompatible(false)
                            .type(BloodComponentType.PLASMA)
                            .build(),
                    BloodComponent.builder()
                            .name("Tiểu cầu")
                            .code("PLT")
                            .storageTemp("20-24°C")
                            .storageDays(5)
                            .usage("Bổ sung tiểu cầu, hỗ trợ đông máu")
                            .isApheresisCompatible(true)
                            .type(BloodComponentType.PLATELETS)
                            .build()
            ));
        }
    }
}

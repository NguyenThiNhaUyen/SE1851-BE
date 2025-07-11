package com.quyet.superapp.init;

import com.quyet.superapp.entity.BloodComponent;
import com.quyet.superapp.entity.BloodPrice;
import com.quyet.superapp.entity.BloodType;
import com.quyet.superapp.repository.BloodComponentRepository;
import com.quyet.superapp.repository.BloodPriceRepository;
import com.quyet.superapp.repository.BloodTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BloodPriceInitializer implements CommandLineRunner {

    private final BloodPriceRepository bloodPriceRepository;
    private final BloodTypeRepository bloodTypeRepository;
    private final BloodComponentRepository bloodComponentRepository;

    @Override
    public void run(String... args) {
        List<BloodType> bloodTypes = bloodTypeRepository.findAll();
        List<BloodComponent> components = bloodComponentRepository.findAll();

        if (bloodTypes.isEmpty() || components.isEmpty()) {
            System.err.println("❌ Không thể khởi tạo bảng giá: thiếu dữ liệu nhóm máu hoặc thành phần máu.");
            return;
        }

        List<BloodPrice> prices = new ArrayList<>();
        int skipped = 0;

        for (BloodType bloodType : bloodTypes) {
            for (BloodComponent component : components) {
                // Tránh tạo trùng (nếu DB chưa có ràng buộc unique)
                boolean exists = bloodPriceRepository
                        .findByBloodTypeAndBloodComponent(bloodType, component)
                        .isPresent();
                if (exists) {
                    skipped++;
                    continue;
                }

                double price = switch (component.getName().trim().toLowerCase()) {
                    case "hồng cầu" -> 300_000;
                    case "tiểu cầu" -> 450_000;
                    case "huyết tương" -> 200_000;
                    default -> {
                        System.out.printf("⚠ Thành phần không xác định: %s%n", component.getName());
                        yield 0.0;
                    }
                };

                if (price > 0) {
                    BloodPrice bloodPrice = BloodPrice.builder()
                            .bloodType(bloodType)
                            .bloodComponent(component)
                            .pricePerBag(price)
                            .build();
                    prices.add(bloodPrice);
                }
            }
        }

        if (!prices.isEmpty()) {
            bloodPriceRepository.saveAll(prices);
        }

        System.out.printf("✅ Đã thêm %d dòng bảng giá máu. Bỏ qua %d dòng đã tồn tại.%n", prices.size(), skipped);
    }
}

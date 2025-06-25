package com.quyet.superapp.service;

import com.quyet.superapp.dto.BloodUnitDTO;
import com.quyet.superapp.dto.SeparationResultDTO;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.BloodUnitStatus;
import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.mapper.BloodUnitMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.util.BloodSeparationCalculator;
import com.quyet.superapp.dto.BloodSeparationSuggestionDTO;
import com.quyet.superapp.dto.CreateSeparationWithSuggestionRequest;
import com.quyet.superapp.util.CodeGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeparationOrderService {

    private final SeparationOrderRepository separationOrderRepository;
    private final BloodBagRepository bloodBagRepository;
    private final UserRepository userRepository;
    private final ApheresisMachineRepository apheresisMachineRepository;
    private final SeparationPresetService presetService;
    private final BloodSeparationCalculator calculator;
    private final BloodComponentRepository bloodComponentRepository;
    private final BloodUnitRepository bloodUnitRepository;
    /**
     * Tạo một lệnh tách máu mới
     */
    @Transactional
    public SeparationOrder createSeparationOrder(Long bloodBagId,
                                                 Long operatorId,
                                                 Long machineId,
                                                 SeparationMethod type,
                                                 String note) {
        BloodBag bag = bloodBagRepository.findById(bloodBagId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu"));

        User operator = userRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thao tác"));

        ApheresisMachine machine = null;
        if (type == SeparationMethod.MACHINE) {
            machine = apheresisMachineRepository.findById(machineId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy máy tách máu"));
        }


        SeparationOrder order = new SeparationOrder();
        order.setBloodBag(bag);
        order.setPerformedBy(operator);
        order.setMachine(machine);
        order.setSeparationMethod(type);
        order.setPerformedAt(LocalDateTime.now());
        order.setNote(note);

        return separationOrderRepository.save(order);
    }

    /**
     * Truy xuất tất cả các lệnh tách
     */
    public List<SeparationOrder> getAll() {
        return separationOrderRepository.findAll();
    }

    /**
     * Truy xuất theo loại tách
     */
    public List<SeparationOrder> findByType(SeparationMethod method) {
        return separationOrderRepository.findBySeparationMethod(method);
    }

    /**
     * Truy xuất theo nhân viên thao tác
     */
    public List<SeparationOrder> findByOperator(Long userId) {
        return separationOrderRepository.findByPerformedBy_UserId(userId);
    }

    /**
     * Truy xuất theo mã túi máu
     */
    public List<SeparationOrder> findByBagCode(String bagCode) {
        return separationOrderRepository.findByBloodBag_BagCode(bagCode);
    }

    /**
     * Kiểm tra xem túi máu đã được tách chưa
     */
    public boolean hasBeenSeparated(Long bloodBagId) {
        return separationOrderRepository.existsByBloodBag_BloodBagId(bloodBagId);
    }

    /**
     * Tìm theo thời gian
     */
    public List<SeparationOrder> findBetween(LocalDateTime start, LocalDateTime end) {
        return separationOrderRepository.findByPerformedAtBetween(start, end);
    }

    public SeparationResultDTO createWithSuggestion(CreateSeparationWithSuggestionRequest request) {
        // 1. Lấy túi máu
        BloodBag bloodBag = bloodBagRepository.findById(request.getBloodBagId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu"));

        // Kiểm tra thể tích tối thiểu
        if (bloodBag.getVolume() < 250) {
            throw new IllegalArgumentException("Thể tích túi máu quá nhỏ để tách (phải >= 250ml)");
        }

        // 2. Lấy nhân viên
        User operator = userRepository.findById(request.getOperatorId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

        // 3. Nếu là phương pháp MACHINE → bắt buộc phải có máy
        ApheresisMachine machine = null;
        if (request.getType() == SeparationMethod.MACHINE) {
            if (request.getMachineId() == null) {
                throw new IllegalArgumentException("Cần cung cấp ID máy khi tách bằng máy.");
            }
            machine = apheresisMachineRepository.findById(request.getMachineId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy máy tách máu"));
        }

        // 4. Tìm preset phù hợp
        SeparationPresetConfig preset = presetService.getPreset(
                request.getGender(),
                request.getWeight(),
                request.getType().name(),
                request.isLeukoreduced()
        );

        // 5. Tính toán gợi ý
        BloodSeparationSuggestionDTO suggestion = calculator.calculateFromPreset(bloodBag, preset);

        // 6. Tạo lệnh tách máu
        SeparationOrder order = new SeparationOrder();
        order.setBloodBag(bloodBag);
        order.setPerformedBy(operator);
        order.setMachine(machine);
        order.setSeparationMethod(request.getType());
        order.setPerformedAt(LocalDateTime.now());
        order.setNote(request.getNote());
        separationOrderRepository.save(order);

        // 7. Đổi trạng thái túi máu gốc
        bloodBag.setStatus(BloodBagStatus.SEPARATED); // Enum bạn cần tạo nếu chưa có
        bloodBagRepository.save(bloodBag);

        // 8. Tạo các đơn vị máu từ gợi ý
        createBloodUnitsFromSuggestion(suggestion, bloodBag, order);

        // 9. Truy xuất lại các đơn vị máu đã sinh ra
        List<BloodUnit> createdUnits = bloodUnitRepository.findBySeparationOrder(order);
        List<BloodUnitDTO> dtoUnits = createdUnits.stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());


        return new SeparationResultDTO(order.getSeparationOrderId(), suggestion, dtoUnits);

    }
    private void createBloodUnitsFromSuggestion(BloodSeparationSuggestionDTO suggestion,
                                                BloodBag bloodBag,
                                                SeparationOrder order) {
        createUnit(suggestion.getRedCellsMl(), "HỒNG CẦU", bloodBag, order);
        createUnit(suggestion.getPlasmaMl(), "HUYẾT TƯƠNG", bloodBag, order);
        createUnit(suggestion.getPlateletsMl(), "TIỂU CẦU", bloodBag, order);
    }

    private void createUnit(int volume, String componentName,
                            BloodBag bag, SeparationOrder order) {
        if (volume <= 0) return;

        var component = bloodComponentRepository.findByName(componentName)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thành phần máu: " + componentName));

        // 👉 Gán mã viết tắt cho component
        String componentCode = switch (componentName) {
            case "HỒNG CẦU" -> "RBC";
            case "HUYẾT TƯƠNG" -> "PLAS";
            case "TIỂU CẦU" -> "PLT";
            default -> "UNK";
        };

        String unitCode = CodeGeneratorUtil.generateUniqueUnitCode(bag, componentCode, bloodUnitRepository);

        var unit = new BloodUnit();
        unit.setQuantityMl(volume);
        unit.setComponent(component);
        unit.setBloodBag(bag);
        unit.setBloodType(bag.getBloodType());
        unit.setSeparationOrder(order);
        unit.setStatus(BloodUnitStatus.AVAILABLE); // hoặc AVAILABLE
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUnitCode(unitCode);

        bloodUnitRepository.save(unit);
    }

}

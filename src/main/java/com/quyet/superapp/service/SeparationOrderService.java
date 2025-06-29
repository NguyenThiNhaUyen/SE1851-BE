package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.*;
import com.quyet.superapp.mapper.BloodUnitMapper;
import com.quyet.superapp.repository.*;
import com.quyet.superapp.util.*;
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

    // ✅ Dùng trong tạo nhanh (manual) có sinh đơn vị máu
    @Transactional
    public SeparationResultDTO createSeparationOrderEntity(Long bloodBagId, Long operatorId,
                                                           Long machineId, SeparationMethod type, String note) {
        BloodBag bag = getBloodBagValidated(bloodBagId);
        checkNotSeparated(bag);
        User operator = getOperator(operatorId);
        ApheresisMachine machine = (type == SeparationMethod.MACHINE) ? getMachine(machineId) : null;

        SeparationOrder order = buildAndSaveOrder(bag, operator, machine, type, note);
        bag.setStatus(BloodBagStatus.SEPARATED);
        bloodBagRepository.save(bag);

        int total = bag.getVolume();
        int red = (int) (total * 0.48);
        int plasma = (int) (total * 0.42);
        int platelets = total - red - plasma;

        BloodSeparationSuggestionDTO suggestion = new BloodSeparationSuggestionDTO(
                red, plasma, platelets,
                "PRC-" + bag.getBloodType().getDescription(),
                "FFP-" + bag.getBloodType().getDescription(),
                "PLT-" + bag.getBloodType().getDescription(),
                String.format("Manual separation: %.0f/%.0f/%.0f", 48.0, 42.0, 10.0)
        );

        createBloodUnitsFromSuggestion(suggestion, bag, order);
        List<BloodUnitDTO> dtoUnits = getDTOUnits(order);

        return new SeparationResultDTO(order.getSeparationOrderId(), suggestion, dtoUnits, note);
    }

    // ✅ Tạo từ gợi ý preset
    @Transactional
    public SeparationResultDTO createWithSuggestion(CreateSeparationWithSuggestionRequest request) {
        BloodBag bag = getBloodBagValidated(request.getBloodBagId());
        User operator = getOperator(request.getOperatorId());
        ApheresisMachine machine = (request.getType() == SeparationMethod.MACHINE) ?
                getMachine(request.getMachineId()) : null;

        SeparationPresetConfig preset = presetService.getPreset(
                request.getGender(), request.getWeight(), request.getType().name(), request.isLeukoreduced()
        );
        BloodSeparationSuggestionDTO suggestion = calculator.calculateFromPreset(bag, preset);

        SeparationOrder order = buildAndSaveOrder(bag, operator, machine, request.getType(), request.getNote());
        bag.setStatus(BloodBagStatus.SEPARATED);
        bloodBagRepository.save(bag);

        createBloodUnitsFromSuggestion(suggestion, bag, order);
        List<BloodUnitDTO> dtoUnits = getDTOUnits(order);

        return new SeparationResultDTO(order.getSeparationOrderId(), suggestion, dtoUnits, request.getNote());
    }

    // ✅ Tạo bản ghi lệnh tách đơn giản, không sinh đơn vị máu
    @Transactional
    public SeparationOrder createSeparationOrder(Long bloodBagId, Long operatorId,
                                                 Long machineId, SeparationMethod type, String note) {
        BloodBag bag = getBloodBagValidated(bloodBagId);
        User operator = getOperator(operatorId);
        ApheresisMachine machine = (type == SeparationMethod.MACHINE) ? getMachine(machineId) : null;
        return buildAndSaveOrder(bag, operator, machine, type, note);
    }

    // --------------------------------------------
    // 🔧 HELPER – tái sử dụng logic tạo lệnh tách
    // --------------------------------------------

    private SeparationOrder buildAndSaveOrder(BloodBag bag, User operator,
                                              ApheresisMachine machine, SeparationMethod type, String note) {
        SeparationOrder order = new SeparationOrder();
        order.setBloodBag(bag);
        order.setPerformedBy(operator);
        order.setMachine(machine);
        order.setSeparationMethod(type);
        order.setPerformedAt(LocalDateTime.now());
        order.setNote(note);
        return separationOrderRepository.save(order);
    }

    private BloodBag getBloodBagValidated(Long id) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu"));
        if (bag.getVolume() < 250) {
            throw new IllegalArgumentException("Thể tích túi máu quá nhỏ để tách (phải >= 250ml)");
        }
        return bag;
    }

    private void checkNotSeparated(BloodBag bag) {
        if (hasBeenSeparated(bag.getBloodBagId())) {
            throw new IllegalStateException("Túi máu này đã được tách trước đó.");
        }
    }

    private User getOperator(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thao tác"));
    }

    private ApheresisMachine getMachine(Long id) {
        return apheresisMachineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy máy tách máu"));
    }

    private void createBloodUnitsFromSuggestion(BloodSeparationSuggestionDTO suggestion,
                                                BloodBag bloodBag, SeparationOrder order) {
        createUnit(suggestion.getRedCellsMl(), "HỒNG CẦU", bloodBag, order);
        createUnit(suggestion.getPlasmaMl(), "HUYẾT TƯƠNG", bloodBag, order);
        createUnit(suggestion.getPlateletsMl(), "TIỂU CẦU", bloodBag, order);
    }

    private void createUnit(int volume, String componentName,
                            BloodBag bag, SeparationOrder order) {
        if (volume <= 0) return;
        var component = bloodComponentRepository.findByName(componentName)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thành phần máu: " + componentName));
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
        unit.setStatus(BloodUnitStatus.AVAILABLE);
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUnitCode(unitCode);

        bloodUnitRepository.save(unit);
    }

    private List<BloodUnitDTO> getDTOUnits(SeparationOrder order) {
        return bloodUnitRepository.findBySeparationOrder(order).stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --------------------------------------------
    // 🔍 Truy vấn đơn giản
    // --------------------------------------------

    public List<SeparationOrder> getAll() {
        return separationOrderRepository.findAll();
    }

    public List<SeparationOrder> findByType(SeparationMethod method) {
        return separationOrderRepository.findBySeparationMethod(method);
    }

    public List<SeparationOrder> findByOperator(Long userId) {
        return separationOrderRepository.findByPerformedBy_UserId(userId);
    }

    public List<SeparationOrder> findByBagCode(String bagCode) {
        return separationOrderRepository.findByBloodBag_BagCode(bagCode);
    }

    public boolean hasBeenSeparated(Long bloodBagId) {
        return separationOrderRepository.existsByBloodBag_BloodBagId(bloodBagId);
    }

    public List<SeparationOrder> findBetween(LocalDateTime start, LocalDateTime end) {
        return separationOrderRepository.findByPerformedAtBetween(start, end);
    }
}
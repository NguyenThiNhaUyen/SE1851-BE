package com.quyet.superapp.service;

import com.quyet.superapp.dto.*;
import com.quyet.superapp.entity.*;
import com.quyet.superapp.enums.*;
import com.quyet.superapp.mapper.*;
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
    private final SeparationResultRepository separationResultRepository;
    private final BloodSeparationSuggestionRepository bloodSeparationSuggestionRepository;
    private final BloodInventorySyncService bloodInventorySyncService;
    private final DonationRepository donationRepository;

    // ✅ Tạo lệnh tách máu thủ công (manual) có sinh đơn vị máu từ volume mặc định
    @Transactional
    public SeparationResultDTO createSeparationOrderEntity(
            Long bloodBagId, Long operatorId,
            Long machineId, SeparationMethod type, String note
    ) {
        BloodBag bag = getBloodBagValidated(bloodBagId);
        checkNotSeparated(bag);
        validateVolume(bag);

        User operator = getOperator(operatorId);
        ApheresisMachine machine = (type == SeparationMethod.MACHINE) ? getMachine(machineId) : null;

        SeparationOrder order = buildAndSaveOrder(bag, operator, machine, type, note);
        bag.setStatus(BloodBagStatus.SEPARATED);
        bloodBagRepository.save(bag);

        BloodSeparationSuggestion suggestion = buildManualSuggestion(bag, operator);
        bloodSeparationSuggestionRepository.save(suggestion);

        createBloodUnitsFromSuggestion(suggestion, bag, order);
        List<BloodUnit> units = bloodUnitRepository.findBySeparationOrder(order);

        SeparationResult result = buildSeparationResult(order, operator, note, suggestion);
        separationResultRepository.save(result);

        return SeparationResultMapper.toDTO(
                result,
                BloodSeparationSuggestionMapper.toDTO(suggestion),
                units
        );
    }

    // 🔧 Xây dựng gợi ý thủ công từ volume túi máu
    private BloodSeparationSuggestion buildManualSuggestion(BloodBag bag, User operator) {
        int total = bag.getVolume();
        int red = (int) (total * 0.48);
        int plasma = (int) (total * 0.42);
        int platelets = total - red - plasma;

        BloodSeparationSuggestion suggestion = new BloodSeparationSuggestion();
        suggestion.setRedCells(red);
        suggestion.setPlasma(plasma);
        suggestion.setPlatelets(platelets);
        suggestion.setRedCellsCode("PRC-" + bag.getBloodType().getDescription());
        suggestion.setPlasmaCode("FFP-" + bag.getBloodType().getDescription());
        suggestion.setPlateletsCode("PLT-" + bag.getBloodType().getDescription());
        suggestion.setDescription("Manual separation: 48% / 42% / 10%");
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setGeneratedBy(operator);
        return suggestion;
    }

    // 🔧 Xây dựng đối tượng kết quả sau khi tách
    private SeparationResult buildSeparationResult(SeparationOrder order, User operator, String note, BloodSeparationSuggestion suggestion) {
        SeparationResult result = new SeparationResult();
        result.setOrder(order);
        result.setProcessedBy(operator);
        result.setCompletedAt(LocalDateTime.now());
        result.setNote(note);
        result.setSuggestion(suggestion);
        return result;
    }

    // ✅ Validate thể tích túi máu (250 - 500ml)
    private void validateVolume(BloodBag bag) {
        int volume = bag.getVolume();
        if (volume < 250 || volume > 500) {
            throw new IllegalArgumentException("Thể tích túi máu phải từ 250ml đến 500ml");
        }
    }

    // ✅ Tạo lệnh tách máu dựa theo gợi ý preset (có gender, weight, etc.)
    @Transactional
    public SeparationResultDTO createWithSuggestion(CreateSeparationWithSuggestionRequest request) {
        BloodBag bag = getBloodBagValidated(request.getBloodBagId());
        User operator = getOperator(request.getOperatorId());
        ApheresisMachine machine = resolveMachineIfNeeded(request.getType(), request.getMachineId());

        BloodSeparationSuggestion suggestion = generateSuggestionFromPreset(bag, operator, request);
        SeparationOrder order = buildAndSaveOrder(bag, operator, machine, request.getType(), request.getNote());

        updateBagStatusToSeparated(bag);

        createBloodUnitsFromSuggestion(suggestion, bag, order);
        SeparationResult result = saveSeparationResult(order, operator, suggestion, request.getNote());
        List<BloodUnit> units = bloodUnitRepository.findBySeparationOrder(order);

        return SeparationResultMapper.toDTO(result, BloodSeparationSuggestionMapper.toDTO(suggestion), units);
    }

    // 🔧 Lấy máy nếu phương thức là MACHINE
    private ApheresisMachine resolveMachineIfNeeded(SeparationMethod type, Long machineId) {
        return (type == SeparationMethod.MACHINE) ? getMachine(machineId) : null;
    }

    // 🔧 Sinh gợi ý từ preset cấu hình (gender, weight...)
    private BloodSeparationSuggestion generateSuggestionFromPreset(
            BloodBag bag, User operator, CreateSeparationWithSuggestionRequest request) {
        SeparationPresetConfig preset = presetService.getPreset(
                request.getGender(), request.getWeight(),
                request.getType().name(), request.isLeukoreduced());

        BloodSeparationSuggestion suggestion = calculator.calculateFromPreset(bag, preset);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setGeneratedBy(operator);
        return bloodSeparationSuggestionRepository.save(suggestion);
    }

    // 🔧 Cập nhật trạng thái túi máu là đã tách
    private void updateBagStatusToSeparated(BloodBag bag) {
        bag.setStatus(BloodBagStatus.SEPARATED);
        bloodBagRepository.save(bag);
    }

    // 🔧 Lưu kết quả tách máu
    private SeparationResult saveSeparationResult(SeparationOrder order, User operator,
                                                  BloodSeparationSuggestion suggestion, String note) {
        SeparationResult result = new SeparationResult();
        result.setOrder(order);
        result.setProcessedBy(operator);
        result.setCompletedAt(LocalDateTime.now());
        result.setNote(note);
        result.setSuggestion(suggestion);
        return separationResultRepository.save(result);
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

    // 🔧 Tạo và lưu lệnh tách máu
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

    // ✅ Kiểm tra và trả về túi máu hợp lệ
    private BloodBag getBloodBagValidated(Long id) {
        BloodBag bag = bloodBagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy túi máu"));
        if (bag.getVolume() < 250) {
            throw new IllegalArgumentException("Thể tích túi máu quá nhỏ để tách (phải >= 250ml)");
        }
        return bag;
    }

    // ✅ Kiểm tra túi máu đã tách chưa
    private void checkNotSeparated(BloodBag bag) {
        if (hasBeenSeparated(bag.getBloodBagId())) {
            throw new IllegalStateException("Túi máu này đã được tách trước đó.");
        }
    }

    // ✅ Truy vấn operator từ ID
    private User getOperator(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thao tác"));
    }

    // ✅ Truy vấn máy tách từ IDx`
    private ApheresisMachine getMachine(Long id) {
        return apheresisMachineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy máy tách máu"));
    }

    // ✅ Tạo các đơn vị máu từ gợi ý (3 loại: Hồng cầu, huyết tương, tiểu cầu)
    private void createBloodUnitsFromSuggestion(BloodSeparationSuggestion suggestion,
                                                BloodBag bloodBag, SeparationOrder order) {
        createUnit(suggestion.getRedCells(), "HỒNG CẦU", bloodBag, order);
        createUnit(suggestion.getPlasma(), "HUYẾT TƯƠNG", bloodBag, order);
        createUnit(suggestion.getPlatelets(), "TIỂU CẦU", bloodBag, order);
    }

    // 🔧 Tạo đơn vị máu cụ thể
    private void createUnit(int volume, String componentName, BloodBag bag, SeparationOrder order) {
        if (volume <= 0) return;

        List<BloodComponent> matchingComponents =
                bloodComponentRepository.findAll().stream()
                        .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(componentName))
                        .toList();

        if (matchingComponents.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy thành phần máu: " + componentName);
        }

        BloodComponent component = matchingComponents.get(0); // lấy cái đầu tiên match ignore-case

        String componentCode = switch (componentName.toUpperCase()) {
            case "HỒNG CẦU" -> "RBC";
            case "HUYẾT TƯƠNG" -> "PLAS";
            case "TIỂU CẦU" -> "PLT";
            default -> "UNK";
        };

        String unitCode = CodeGeneratorUtil.generateUniqueUnitCode(bag, componentCode, bloodUnitRepository);

        BloodUnit unit = new BloodUnit();
        unit.setQuantityMl(volume);
        unit.setComponent(component);
        unit.setBloodBag(bag);
        unit.setBloodType(bag.getBloodType());
        unit.setSeparationOrder(order);
        unit.setStatus(BloodUnitStatus.AVAILABLE);
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUnitCode(unitCode);

        bloodUnitRepository.save(unit);
        bloodInventorySyncService.syncInventory(unit);
    }



    // ✅ Lấy danh sách DTO các đơn vị máu của một lệnh tách
    private List<BloodUnitDTO> getDTOUnits(SeparationOrder order) {
        return bloodUnitRepository.findBySeparationOrder(order).stream()
                .map(BloodUnitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ Lấy chi tiết đầy đủ lệnh tách theo ID
    public SeparationOrderFullDTO getFullOrderWithUnits(Long orderId) {
        SeparationOrder order = separationOrderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lệnh tách"));
        List<BloodUnitDTO> units = getDTOUnits(order);
        return SeparationOrderFullMapper.toFullDTO(order, units);
    }

    // ✅ Truy vấn tất cả lệnh tách
    public List<SeparationOrder> getAll() {
        return separationOrderRepository.findAll();
    }

    // ✅ Truy vấn lệnh theo loại tách
    public List<SeparationOrder> findByType(SeparationMethod method) {
        return separationOrderRepository.findBySeparationMethod(method);
    }

    // ✅ Truy vấn lệnh theo nhân viên thao tác
    public List<SeparationOrder> findByOperator(Long userId) {
        return separationOrderRepository.findByPerformedBy_UserId(userId);
    }

    // ✅ Truy vấn lệnh theo mã túi máu
    public List<SeparationOrder> findByBagCode(String bagCode) {
        return separationOrderRepository.findByBloodBag_BagCode(bagCode);
    }

    // ✅ Kiểm tra túi máu đã từng tách chưa
    public boolean hasBeenSeparated(Long bloodBagId) {
        return separationOrderRepository.existsByBloodBag_BloodBagId(bloodBagId);
    }

    // ✅ Lọc lệnh tách theo khoảng thời gian
    public List<SeparationOrder> findBetween(LocalDateTime start, LocalDateTime end) {
        return separationOrderRepository.findByPerformedAtBetween(start, end);
    }

    // ✅ Tạo túi máu thủ công từ DTO (nhập tay mã túi, thể tích...)
    @Transactional
    public BloodBag createManualBloodBagFromDTO(BloodBagDTO dto, Long donationId) {
        Donation donation = validateDonationForNewBag(donationId);
        BloodBag bag = mapDTOToBloodBag(dto, donation);

        bloodBagRepository.save(bag);
        donation.setBloodBag(bag);
        donationRepository.save(donation);

        return bag;
    }

    // Tách validate + lấy donation
    private Donation validateDonationForNewBag(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hiến máu"));

        if (donation.getBloodBag() != null) {
            throw new IllegalStateException("Đơn hiến máu đã có túi máu. Không thể tạo thêm.");
        }
        return donation;
    }

    // Tách ánh xạ từ DTO sang Entity (có thêm mặc định volume, collectedAt...)
    private BloodBag mapDTOToBloodBag(BloodBagDTO dto, Donation donation) {
        BloodType bloodType = donation.getBloodType();

        // ✅ Tìm donor từ donorId trong DTO
        User donor = null;
        if (dto.getDonorId() != null) {
            donor = userRepository.findById(dto.getDonorId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người hiến máu"));
        }

        // ✅ Tạo BloodBag entity từ mapper
        BloodBag bag = BloodBagMapper.fromDTO(dto, bloodType, donor);

        // ✅ Gán các giá trị mặc định nếu chưa có
        bag.setBagCode(CodeGeneratorUtil.generateBloodBagCode());
        bag.setVolume(dto.getVolume() != null ? dto.getVolume() : 350);
        bag.setCollectedAt(dto.getCollectedAt() != null ? dto.getCollectedAt() : LocalDateTime.now());
        bag.setTestStatus(dto.getTestStatus() != null ? dto.getTestStatus() : TestStatus.NOT_TESTED);
        bag.setStatus(dto.getStatus() != null ? dto.getStatus() : BloodBagStatus.COLLECTED);
        bag.setDonation(donation);

        return bag;
    }

    //    Tìm kiếm nâng cao các lệnh tách máu dựa trên nhiều tiêu chí:
    //          - Loại tách máu (SeparationMethod)
    //          - Nhân viên thao tác (Operator ID)
    //          - Mã túi máu (BagCode)
    //          - Thời gian thực hiện (from - to)
    public List<SeparationOrderDTO> advancedSearch(SeparationOrderSearchRequest request) {
        return separationOrderRepository.findAll().stream()
                .filter(order -> {
                    if (request.getType() != null && !request.getType().equals(order.getSeparationMethod())) {
                        return false;
                    }
                    if (request.getOperatorId() != null && !request.getOperatorId().equals(order.getPerformedBy().getUserId())) {
                        return false;
                    }
                    if (request.getBagCode() != null && !order.getBloodBag().getBagCode().contains(request.getBagCode())) {
                        return false;
                    }
                    if (request.getFrom() != null && order.getPerformedAt().isBefore(request.getFrom())) {
                        return false;
                    }
                    if (request.getTo() != null && order.getPerformedAt().isAfter(request.getTo())) {
                        return false;
                    }
                    return true;
                })
                .map(SeparationOrderMapper::toDTO)
                .collect(Collectors.toList());
    }
    //    *  Cập nhật lệnh tách máu:
    //              - Ghi chú (note)
    // *  - Người thao tác (operatorId)
    //         *  Chỉ cập nhật nếu trường tương ứng khác null.
    //          ✅ Có kiểm tra tồn tại order và user.
    @Transactional
    public SeparationOrderDTO updateOrder(Long id, UpdateSeparationOrderRequest request) {
        SeparationOrder order = separationOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lệnh tách máu"));

        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }

        if (request.getOperatorId() != null) {
            User newOperator = userRepository.findById(request.getOperatorId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thao tác mới"));
            order.setPerformedBy(newOperator);
        }

        return SeparationOrderMapper.toDTO(separationOrderRepository.save(order));
    }
    //    Xoá lệnh tách máu theo ID.
    //            - Nếu hệ thống hỗ trợ "soft delete" thì nên cập nhật trạng thái thay vì gọi delete().
    //            - Hiện tại dùng hard-delete trực tiếp từ repository.
    @Transactional
    public void softDeleteOrder(Long id) {
        SeparationOrder order = separationOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lệnh tách máu"));
        separationOrderRepository.delete(order);
    }



}
package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO tổng hợp kết quả tách máu, gộp từ BloodSeparationResultDTO và SeparationResultDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationResultDTO {

    private Long separationOrderId;                                // ID lệnh tách máu

    private BloodSeparationSuggestionDTO suggestion;          // Gợi ý preset nếu có

    private List<BloodSeparationDetailDTO> components;        // Thành phần chi tiết sau tách (volume, component type)

    private List<BloodUnitDTO> createdUnits;                  // Đơn vị máu đã sinh ra và lưu kho

    private String note;                                      // Ghi chú thêm nếu có

    private Long processedById;                               // ID người thực hiện
    private String processedByName;                           // Tên người thực hiện

    private LocalDateTime completedAt;                        // Thời gian hoàn tất
}

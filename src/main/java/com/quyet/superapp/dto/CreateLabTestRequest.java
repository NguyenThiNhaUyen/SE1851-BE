package com.quyet.superapp.dto;

<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotNull;
>>>>>>> origin/main
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLabTestRequest {
<<<<<<< HEAD
    private Long bloodUnitId;    // ID của đơn vị máu cần xét nghiệm
=======
    @NotNull(message = "ID đơn vị máu không được để trống")
    private Long bloodUnitId;

    @NotNull(message = "ID nhân viên thực hiện xét nghiệm không được để trống")
>>>>>>> origin/main
    private Long testedById;     // ID của nhân viên thực hiện xét nghiệm

    // Các kết quả từng bệnh
    private boolean hivNegative;
    private boolean hbvNegative;
    private boolean hcvNegative;
    private boolean syphilisNegative;
    private boolean malariaNegative;

    // ✅ passed sẽ được tính tự động ở backend (tất cả âm tính)
}

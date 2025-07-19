package com.quyet.superapp.dto;

<<<<<<< HEAD
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
>>>>>>> origin/main

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private Long bloodUnitId;
<<<<<<< HEAD
    private Long bloodTypeId;
    private Long componentId;
    private Long bloodBagId; // ✅ đổi tên cho rõ nghĩa
    private Integer quantityMl;
    private LocalDate expirationDate;
    private String status;
=======
    private String unitCode;

    private Integer quantityMl;
    private LocalDate expirationDate;
    private String status;

>>>>>>> origin/main
    private LocalDateTime storedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

<<<<<<< HEAD
    private String unitCode;         // để truy xuất hoặc in tem
    private String bloodTypeName;    // để hiển thị rõ hơn
    private String componentName;    // để hiển thị rõ hơn
}


=======
    private Long bloodTypeId;
    private String bloodTypeName;

    private Long componentId;
    private String componentName;

    private Long bloodBagId;
    private String bagCode;
}
>>>>>>> origin/main

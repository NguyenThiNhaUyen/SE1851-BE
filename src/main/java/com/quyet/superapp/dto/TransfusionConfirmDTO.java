package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
<<<<<<< HEAD
public class TransfusionConfirmDTO {
=======
public class TransfusionConfirmDTO { //chưa sử dụng
>>>>>>> origin/main
    private Long transfusionConfirmId;
    private String recipientName;
    private String bloodType;
    private int units;
    private LocalDateTime confirmedAt;
    private String status;
}

package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.VnPaymentDTO;
import com.quyet.superapp.entity.VnPayment;

public class VnPaymentMapper {
    public static VnPaymentDTO toDTO(VnPayment payment) {
        return new VnPaymentDTO(
                payment.getPaymentId(),              // sửa lại đúng field
                payment.getUser().getUserId(),       // đảm bảo User có getUserId()
                payment.getAmount(),
                payment.getPaymentTime(),
                payment.getTransactionCode(),
                payment.getStatus()
        );
    }
}

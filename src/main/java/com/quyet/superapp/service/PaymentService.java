package com.quyet.superapp.service;

import com.quyet.superapp.entity.BloodRequest;
import com.quyet.superapp.entity.VnPayment;
import com.quyet.superapp.enums.PaymentStatus;
import com.quyet.superapp.repository.BloodRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final VnPaymentService vnPaymentService;
    private final BloodRequestRepository bloodRequestRepository;

    public void processPayment(Long requestId) {
        BloodRequest request = bloodRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy yêu cầu máu"));

        VnPayment payment = new VnPayment();
        payment.setBloodRequest(request); // 👈 sử dụng entity thay vì ID
        payment.setUser(request.getRequester()); // giả sử staff là người thanh toán
        payment.setAmount(new BigDecimal("500000")); // gán tạm (sau sẽ tính động)
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionCode("TXN-" + System.currentTimeMillis());
        payment.setStatus(PaymentStatus.SUCCESS);

        vnPaymentService.save(payment);
    }
}

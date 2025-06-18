package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "VnPayPayments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VnPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "Payment_Id")
    private Long paymentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "Payment_Time", columnDefinition = "DATETIME")
    private LocalDateTime paymentTime;

    @Column(name = "Transaction_Code", columnDefinition = "NVARCHAR(100)")
    private String transactionCode;

    @Column(name = "Status", columnDefinition = "NVARCHAR(20)")
    private String status;
}

package com.pulse_gym.lb_common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultDTO {
    private Boolean success;
    private String transactionId;
    private String paymentStatus; // APPROVED, REJECTED, PENDING
    private String message;
    private String paymentMethod;
    private String paymentType;
}
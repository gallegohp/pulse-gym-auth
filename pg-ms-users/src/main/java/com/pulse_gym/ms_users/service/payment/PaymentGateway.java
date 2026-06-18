package com.pulse_gym.ms_users.service.payment;

import java.math.BigDecimal;

import com.pulse_gym.lb_common.dto.PaymentResultDTO;

public interface PaymentGateway {
    
    /**
     * Procesa un pago con tarjeta de crédito/débito
     */
    PaymentResultDTO processCardPayment(
        String cardNumber,
        String cardHolderName,
        String expiryDate,
        String cvv,
        BigDecimal amount,
        String description,
        String email
    );
    
    /**
     * Procesa un pago con token generado por el frontend
     */
    default PaymentResultDTO processCardPaymentWithToken(
        String cardToken,
        BigDecimal amount,
        String description,
        String email,
        String paymentMethodId
    ) {
        return PaymentResultDTO.builder()
            .success(false)
            .message("Método no soportado por este gateway")
            .build();
    }
}
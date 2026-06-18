package com.pulse_gym.ms_users.service.payment;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.pulse_gym.lb_common.dto.PaymentResultDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MercadoPagoGateway implements PaymentGateway {

    @Value("${payment.mercadopago.access-token}")
    private String accessToken;

    @Value("${payment.mercadopago.environment:sandbox}")
    private String environment;

    @Override
    public PaymentResultDTO processCardPayment(
            String cardNumber,
            String cardHolderName,
            String expiryDate,
            String cvv,
            BigDecimal amount,
            String description,
            String email) {

        log.info("🔹 Iniciando pago con MercadoPago SDK...");
        log.info("🔹 Monto: ${}, Descripción: {}, Email: {}", amount, description, email);
        log.info("🔹 Ambiente: {}", environment);

        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            // Limpiar el número de tarjeta
            String cleanCardNumber = cardNumber.replaceAll("\\s+", "").replaceAll("-", "");

            // Determinar el método de pago
            String paymentMethodId = determinePaymentMethod(cleanCardNumber);
            log.info("🔹 Método de pago detectado: {}", paymentMethodId);

            // 🔥 IMPORTANTE PARA SANDBOX:
            // En sandbox, el número de tarjeta se puede usar directamente como token
            String cardToken;
            if ("sandbox".equalsIgnoreCase(environment)) {
                cardToken = cleanCardNumber;
                log.info("🔹 Modo SANDBOX: usando número de tarjeta como token");
            } else {
                // En producción, el token debe venir del frontend
                cardToken = cleanCardNumber;
                log.warn("⚠️ Modo PRODUCCIÓN: debe usar un token generado por el frontend");
            }

            // Crear el payer (quien paga)
            PaymentPayerRequest payer = PaymentPayerRequest.builder()
                    .email(email)
                    .build();

            // 🔥 CORRECCIÓN: Usar el token correctamente
            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                    .transactionAmount(amount)
                    .description(description)
                    .paymentMethodId(paymentMethodId)
                    .payer(payer)
                    .token(cardToken) // ← Aquí va el token (número en sandbox)
                    .installments(1)
                    .build();

            log.info("🔹 Enviando pago a MercadoPago...");
            PaymentClient client = new PaymentClient();
            Payment payment = client.create(paymentCreateRequest);

            log.info("📋 Respuesta - ID: {}, Status: {}, StatusDetail: {}",
                    payment.getId(), payment.getStatus(), payment.getStatusDetail());

            if (payment.getStatus() != null && "approved".equalsIgnoreCase(payment.getStatus().toString())) {
                log.info("✅ Pago APROBADO en MercadoPago. ID: {}", payment.getId());

                return PaymentResultDTO.builder()
                        .success(true)
                        .transactionId(payment.getId().toString())
                        .paymentStatus(payment.getStatus().toString())
                        .message("Pago aprobado exitosamente")
                        .paymentMethod(paymentMethodId)
                        .paymentType("CREDIT_CARD")
                        .build();
            } else {
                log.warn("⚠️ Pago NO APROBADO. Estado: {}, Detalle: {}",
                        payment.getStatus(), payment.getStatusDetail());

                return PaymentResultDTO.builder()
                        .success(false)
                        .transactionId(payment.getId() != null ? payment.getId().toString() : null)
                        .paymentStatus(payment.getStatus() != null ? payment.getStatus().toString() : "UNKNOWN")
                        .message("Pago no aprobado: "
                                + (payment.getStatusDetail() != null ? payment.getStatusDetail() : "Sin detalle"))
                        .build();
            }

        } catch (MPApiException e) {
            String errorContent = e.getApiResponse() != null ? e.getApiResponse().getContent() : "Sin contenido";
            log.error("❌ Error API MercadoPago: {}", errorContent);

            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error en la pasarela: " + errorContent)
                    .build();

        } catch (MPException e) {
            log.error("❌ Error MercadoPago: {}", e.getMessage());
            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error al procesar el pago: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("❌ Error inesperado: {}", e.getMessage(), e);
            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error inesperado: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Procesa pago con token generado por el frontend (para producción)
     */
    @Override
    public PaymentResultDTO processCardPaymentWithToken(
            String cardToken,
            BigDecimal amount,
            String description,
            String email,
            String paymentMethodId) {

        log.info("🔹 Procesando pago con token de tarjeta...");

        try {
            MercadoPagoConfig.setAccessToken(accessToken);

            PaymentPayerRequest payer = PaymentPayerRequest.builder()
                    .email(email)
                    .build();

            PaymentCreateRequest paymentCreateRequest = PaymentCreateRequest.builder()
                    .transactionAmount(amount)
                    .description(description)
                    .paymentMethodId(paymentMethodId != null ? paymentMethodId : "visa")
                    .payer(payer)
                    .token(cardToken) // ← Aquí va el token real generado por el frontend
                    .installments(1)
                    .build();

            PaymentClient client = new PaymentClient();
            Payment payment = client.create(paymentCreateRequest);

            if (payment.getStatus() != null && "approved".equalsIgnoreCase(payment.getStatus().toString())) {
                return PaymentResultDTO.builder()
                        .success(true)
                        .transactionId(payment.getId().toString())
                        .paymentStatus(payment.getStatus().toString())
                        .message("Pago aprobado")
                        .build();
            } else {
                return PaymentResultDTO.builder()
                        .success(false)
                        .paymentStatus(payment.getStatus() != null ? payment.getStatus().toString() : "UNKNOWN")
                        .message("Pago no aprobado: "
                                + (payment.getStatusDetail() != null ? payment.getStatusDetail() : "Sin detalle"))
                        .build();
            }

        } catch (MPApiException e) {
            log.error("❌ Error API MercadoPago: {}", e.getMessage());
            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error en la pasarela: " + e.getMessage())
                    .build();
        } catch (MPException e) {
            log.error("❌ Error MercadoPago: {}", e.getMessage());
            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error al procesar el pago: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("❌ Error procesando pago con token: {}", e.getMessage());
            return PaymentResultDTO.builder()
                    .success(false)
                    .paymentStatus("REJECTED")
                    .message("Error procesando pago")
                    .build();
        }
    }

    /**
     * Determina el método de pago según el número de tarjeta
     */
    private String determinePaymentMethod(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return "visa";
        }

        // Mastercard: empieza con 51-55 o 2221-2720
        if (cardNumber.matches("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720).*")) {
            return "master";
        }

        // Visa: empieza con 4
        if (cardNumber.startsWith("4")) {
            return "visa";
        }

        // American Express: empieza con 34 o 37
        if (cardNumber.matches("^(34|37).*")) {
            return "amex";
        }

        return "visa"; // default
    }
}
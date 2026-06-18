package com.pulse_gym.ms_users.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.PaymentResultDTO;
import com.pulse_gym.lb_common.dto.RegistrarPagoRequestDTO;
import com.pulse_gym.lb_common.entity.user.Pago;
import com.pulse_gym.lb_common.entity.user.SocioMembresia;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumMetodoPago;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.PagoRepository;
import com.pulse_gym.ms_users.repository.SocioMembresiaRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;
import com.pulse_gym.ms_users.service.payment.MercadoPagoGateway;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagoService {

    /** Repositorio para operaciones con pagos */
    private final PagoRepository pagoRepository;

    /** Repositorio para operaciones con membresías asignadas */
    private final SocioMembresiaRepository socioMembresiaRepository;

    /** Repositorio para operaciones con usuarios */
    private final UsuarioPerfilRepository usuarioRepository;

    private final MercadoPagoGateway mercadoPagoGateway;

    /**
     * Registra un nuevo pago para una membresía asignada a un socio.
     * Valida que el usuario tenga rol autorizado (admin, entrenador o
     * recepcionista),
     * que la membresía asignada exista y que el método de pago sea válido.
     * 
     * @param requestDTO        DTO con los datos del pago (idSocioMembresia, monto,
     *                          metodoPago, etc.)
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario que registra el pago
     * @return Mensaje de confirmación con el socio, monto y método de pago
     */
    @Transactional
    public MessegeGlobalDTO registrarPago(RegistrarPagoRequestDTO requestDTO, String userRol, Long userIdAutenticado) {
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                .orElseThrow(() -> new RuntimeException(
                        "Asignación de membresía no encontrada con ID: " + requestDTO.getIdSocioMembresia()));

        EnumMetodoPago metodoPago;
        try {
            metodoPago = EnumMetodoPago.valueOf(requestDTO.getMetodoPago().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Método de pago no válido. Valores: EFECTIVO, TRANSFERENCIA_BANCOLOMBIA, TARJETA_CREDITO, TARJETA_DEBITO, OTRO");
        }

        UsuarioPerfil admin = usuarioRepository.findById(userIdAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario administrador no encontrado"));

        Pago pago = new Pago();
        pago.setSocioMembresia(socioMembresia);
        pago.setMonto(requestDTO.getMonto());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(metodoPago);
        pago.setNumeroComprobante(requestDTO.getNumeroComprobante());
        pago.setAdminRegistro(admin);
        pago.setObservaciones(requestDTO.getObservaciones());
        pago.setAnulado(false);

        pagoRepository.save(pago);

        return new MessegeGlobalDTO(String.format(
                "Pago registrado correctamente. Socio: %s, Monto: $%,.0f, Método: %s",
                socioMembresia.getSocio().getNombre(),
                pago.getMonto(),
                metodoPago.name()));
    }

    /**
     * Realiza un pago desde la aplicación móvil por parte de un socio.
     * Valida que el usuario tenga rol de socio, que la membresía le pertenezca,
     * que el método de pago sea válido para app (solo tarjetas) y que el monto sea
     * mayor a 0.
     * 
     * @param requestDTO        DTO con los datos del pago (idSocioMembresia, monto,
     *                          metodoPago, etc.)
     * @param userRol           Rol del usuario autenticado (debe ser socio)
     * @param userIdAutenticado ID del usuario autenticado
     * @param userEmail         Email del socio autenticado
     * @return Mensaje de confirmación con el socio, monto, método y comprobante
     */
    @Transactional
    public MessegeGlobalDTO realizarPagoApp(RegistrarPagoRequestDTO requestDTO, String userRol,
            Long userIdAutenticado, String userEmail) {
        ValidacionDeRoles.validarSocio(userRol);

        UsuarioPerfil socio = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + userEmail));

        SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                .orElseThrow(() -> new RuntimeException("Asignación de membresía no encontrada"));

        if (!socioMembresia.getSocio().getIdUsuario().equals(socio.getIdUsuario())) {
            throw new SecurityAuthorizationException("Acceso denegado. Solo puede pagar su propia membresía");
        }

        EnumMetodoPago metodoPago;
        try {
            metodoPago = EnumMetodoPago.valueOf(requestDTO.getMetodoPago().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Método de pago no válido. Valores: TARJETA_CREDITO, TARJETA_DEBITO, OTRO");
        }

        if (metodoPago == EnumMetodoPago.EFECTIVO || metodoPago == EnumMetodoPago.TRANSFERENCIA_BANCOLOMBIA) {
            throw new RuntimeException(
                    "Desde la app solo se permiten pagos con tarjeta (TARJETA_CREDITO, TARJETA_DEBITO)");
        }

        if (requestDTO.getMonto() == null || requestDTO.getMonto().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a 0");
        }

        // ============================================================
        // 🚀 INTEGRACIÓN CON MERCADO PAGO - PROCESAR PAGO
        // ============================================================
        log.info("🔹 Procesando pago con MercadoPago para el socio: {} (ID: {})", userEmail, socio.getIdUsuario());
        log.info("🔹 Monto: ${}, Método: {}", requestDTO.getMonto(), metodoPago);

        PaymentResultDTO paymentResult;

        // 🔥 NUEVO: Siempre usar el método con token en sandbox
        // Generamos un token simulado a partir del número de tarjeta
        String simulatedCardToken = requestDTO.getCardNumber().replaceAll("\\s+", "").replaceAll("-", "");

        log.info("🔹 Usando token simulado para sandbox: {}", simulatedCardToken);

        paymentResult = mercadoPagoGateway.processCardPaymentWithToken(
                simulatedCardToken, // Token simulado
                requestDTO.getMonto(),
                "Pago membresía Pulse Gym - " + socioMembresia.getMembresia().getNombre(),
                userEmail,
                determinePaymentMethod(requestDTO.getCardNumber()) // "visa" o "master"
        );

        // Verificar resultado del pago
        if (!paymentResult.getSuccess()) {
            log.error("❌ Pago rechazado por MercadoPago: {}", paymentResult.getMessage());
            throw new RuntimeException("El pago fue rechazado: " + paymentResult.getMessage());
        }

        log.info("✅ Pago APROBADO por MercadoPago. Transacción: {}", paymentResult.getTransactionId());

        // Crear el pago en nuestra base de datos
        Pago pago = new Pago();
        pago.setSocioMembresia(socioMembresia);
        pago.setMonto(requestDTO.getMonto());
        pago.setFechaPago(LocalDateTime.now());
        pago.setMetodoPago(metodoPago);
        pago.setNumeroComprobante(paymentResult.getTransactionId());
        pago.setAdminRegistro(null);
        pago.setObservaciones("Pago desde aplicación móvil - Transacción: " + paymentResult.getTransactionId() +
                (requestDTO.getObservaciones() != null ? " - " + requestDTO.getObservaciones() : ""));
        pago.setAnulado(false);

        Pago pagoGuardado = pagoRepository.save(pago);

        log.info("✅ Pago registrado exitosamente en BD. ID: {}", pagoGuardado.getIdPago());

        return new MessegeGlobalDTO(String.format(
                "✅ Pago realizado exitosamente desde la aplicación. Socio: %s, Monto: $%,.0f, Método: %s, Transacción: %s",
                socio.getNombre(),
                pago.getMonto(),
                metodoPago.name(),
                paymentResult.getTransactionId()));
    }

    // Método helper para determinar el método de pago
    private String determinePaymentMethod(String cardNumber) {
        String clean = cardNumber.replaceAll("\\s+", "").replaceAll("-", "");
        if (clean.matches("^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[0-1]|2720).*")) {
            return "master";
        }
        if (clean.startsWith("4")) {
            return "visa";
        }
        if (clean.matches("^(34|37).*")) {
            return "amex";
        }
        return "visa";
    }
}

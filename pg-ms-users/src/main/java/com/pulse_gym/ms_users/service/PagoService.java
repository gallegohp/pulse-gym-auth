package com.pulse_gym.ms_users.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.preference.Preference;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.PreferenceResponseDTO;
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

import jakarta.annotation.PostConstruct;
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

        @Value("${MERCADOPAGO_ACCESS_TOKEN}")
        private String mpAccessToken;

        @PostConstruct
        public void initMercadoPago() {
                // Quemamos directamente tu token de Sandbox para asegurar que ignore cualquier
                // variable vacía del entorno
                String tokenSeguro = "TEST-4168132953531234-061910-c114382583896dfa26bfe218e860956b-272097072";

                MercadoPagoConfig.setAccessToken(tokenSeguro);
                log.info("✅ SDK de Mercado Pago forzado e inicializado correctamente con Token de Sandbox.");
        }

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
        public MessegeGlobalDTO registrarPago(RegistrarPagoRequestDTO requestDTO, String userRol,
                        Long userIdAutenticado) {
                ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

                SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                                .orElseThrow(() -> new RuntimeException(
                                                "Asignación de membresía no encontrada con ID: "
                                                                + requestDTO.getIdSocioMembresia()));

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
        public PreferenceResponseDTO iniciarPagoMembresiaApp(RegistrarPagoRequestDTO requestDTO, String userRol,
                        String userEmail) {
                // 1. Reutilizamos tus validaciones de seguridad actuales
                ValidacionDeRoles.validarSocio(userRol);

                UsuarioPerfil socio = usuarioRepository.findByEmail(userEmail)
                                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

                SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                                .orElseThrow(() -> new RuntimeException("Asignación de membresía no encontrada"));

                if (!socioMembresia.getSocio().getIdUsuario().equals(socio.getIdUsuario())) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. No puedes pagar una membresía ajena.");
                }

                try {
                        PreferenceClient client = new PreferenceClient();

                        // 2. Forzamos el token directamente en las opciones de la petición para evitar
                        // fallos de entorno
                        String tokenSeguro = "TEST-4168132953531234-061910-c114382583896dfa26bfe218e860956b-272097072";
                        com.mercadopago.core.MPRequestOptions requestOptions = com.mercadopago.core.MPRequestOptions
                                        .builder()
                                        .accessToken(tokenSeguro)
                                        .build();

                        // Redondeamos y aseguramos 2 decimales exactos para evitar que la API falle
                        BigDecimal montoFormateado = requestDTO.getMonto().setScale(2, RoundingMode.HALF_UP);

                        // Configurar el ítem que se va a cobrar
                        PreferenceItemRequest item = PreferenceItemRequest.builder()
                                        .id(socioMembresia.getIdSocioMembresia().toString())
                                        .title("Pulse GYM - Membresia: " + socioMembresia.getMembresia().getNombre())
                                        .quantity(1)
                                        .unitPrice(montoFormateado)
                                        .currencyId("COP")
                                        .build();

                        // URLs de retorno
                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                        .success("http://localhost:5500/success.html")
                                        .failure("http://localhost:5500/failure.html")
                                        .pending("http://localhost:5500/pending.html")
                                        .build();

                        // Construir la petición de la preferencia
                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                        .items(List.of(item))
                                        .backUrls(backUrls)
                                        .externalReference(socioMembresia.getIdSocioMembresia().toString())
                                        .build();

                        // 3. Pasamos la petición JUNTO con las opciones que llevan el token seguro
                        Preference preference = client.create(preferenceRequest, requestOptions);

                        return new PreferenceResponseDTO(preference.getId(), preference.getSandboxInitPoint());

                } catch (MPApiException apiException) {
                        // Mantenemos tu log detallado por si Mercado Pago reporta algún error en los
                        // ítems
                        System.err.println("=== ERROR DETALLADO DE MERCADO PAGO ===");
                        System.err.println("Status Código: " + apiException.getStatusCode());
                        System.err.println("Cuerpo de Respuesta de MP: " + apiException.getApiResponse().getContent());
                        System.err.println("=======================================");

                        throw new RuntimeException("Mercado Pago falló: " + apiException.getApiResponse().getContent());

                } catch (Exception e) {
                        throw new RuntimeException("Error general: " + e.getMessage());
                }
        }
}

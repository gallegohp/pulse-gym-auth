package com.pulse_gym.ms_users.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
import com.pulse_gym.lb_common.enums.EnumEstadoPago;
import com.pulse_gym.lb_common.enums.EnumMetodoPago;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.PagoRepository;
import com.pulse_gym.ms_users.repository.SocioMembresiaRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

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

        /**
         * Token de acceso para la integración con la API de MercadoPago, configurado
         * desde variables de entorno
         */
        @Value("${MERCADOPAGO_ACCESS_TOKEN}")
        private String mpAccessToken;

        /**
         * Registra un nuevo pago para una membresía asignada a un socio.
         * Valida que el usuario tenga rol autorizado (admin, entrenador o
         * recepcionista),
         * que la membresía asignada exista, que el método de pago sea válido,
         * que la membresía tenga un precio válido y genera un comprobante automático si
         * no se proporciona.
         * 
         * @param requestDTO        DTO con los datos del pago (idSocioMembresia,
         *                          metodoPago, numeroComprobante, observaciones)
         * @param userRol           Rol del usuario autenticado
         * @param userIdAutenticado ID del usuario que registra el pago
         * @return Mensaje de confirmación con el socio, monto, método y comprobante
         */
        @Transactional
        public MessegeGlobalDTO registrarPago(RegistrarPagoRequestDTO requestDTO, String userRol,
                        Long userIdAutenticado) {
                ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

                SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                                .orElseThrow(() -> new RuntimeException(
                                                "Asignación de membresía no encontrada con ID: "
                                                                + requestDTO.getIdSocioMembresia()));

                if (requestDTO.getMetodoPago() == null) {
                        throw new RuntimeException(
                                        "Método de pago no válido o ausente. Valores: EFECTIVO, TRANSFERENCIA_BANCOLOMBIA, TARJETA_CREDITO, TARJETA_DEBITO, OTRO");
                }

                EnumMetodoPago metodoPago = requestDTO.getMetodoPago();

                UsuarioPerfil admin = usuarioRepository.findById(userIdAutenticado)
                                .orElseThrow(() -> new RuntimeException("Usuario administrador no encontrado"));

                BigDecimal montoMembresia = socioMembresia.getMembresia().getPrecioTotal();
                if (montoMembresia == null || montoMembresia.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new RuntimeException("La membresía asociada no tiene un precio válido asignado.");
                }

                String comprobanteFinal = requestDTO.getNumeroComprobante();

                if (comprobanteFinal == null || comprobanteFinal.trim().isEmpty()) {

                        String codigoUnico = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                        comprobanteFinal = "REC-" + codigoUnico;
                }

                Pago pago = new Pago();
                pago.setSocioMembresia(socioMembresia);
                pago.setMonto(montoMembresia);
                pago.setFechaPago(LocalDateTime.now());
                pago.setMetodoPago(metodoPago);
                pago.setNumeroComprobante(comprobanteFinal);
                pago.setAdminRegistro(admin);
                pago.setObservaciones(requestDTO.getObservaciones());
                pago.setAnulado(false);
                pago.setEstado(EnumEstadoPago.APROBADO);

                pagoRepository.save(pago);

                return new MessegeGlobalDTO(String.format(
                                "Pago registrado correctamente. Socio: %s, Monto: $%,.0f, Método: %s, Comprobante: %s",
                                socioMembresia.getSocio().getNombre(),
                                pago.getMonto(),
                                metodoPago.name(),
                                pago.getNumeroComprobante()));
        }

        /**
         * Inicia un pago de membresía desde la aplicación móvil integrando con
         * MercadoPago.
         * Valida que el usuario sea socio, que la membresía le pertenezca,
         * que el método de pago no sea efectivo, y genera una preferencia de pago en
         * MercadoPago.
         * 
         * @param requestDTO DTO con los datos del pago (idSocioMembresia, metodoPago)
         * @param userRol    Rol del usuario autenticado (debe ser socio)
         * @param userEmail  Email del socio autenticado
         * @return DTO con el ID de preferencia y URL de pago de MercadoPago
         */
        @Transactional
        public PreferenceResponseDTO iniciarPagoMembresiaApp(RegistrarPagoRequestDTO requestDTO, String userRol,
                        String userEmail) {

                if (requestDTO.getMetodoPago() == EnumMetodoPago.EFECTIVO) {
                        throw new IllegalArgumentException(
                                        "El método de pago en efectivo no está permitido para transacciones desde la aplicación móvil.");
                }

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

                        String tokenFinal = (this.mpAccessToken != null && !this.mpAccessToken.isEmpty())
                                        ? this.mpAccessToken
                                        : "TEST-4168132953531234-061910-c114382583896dfa26bfe218e860956b-272097072";

                        MPRequestOptions requestOptions = MPRequestOptions.builder()
                                        .accessToken(tokenFinal.trim())
                                        .build();

                        BigDecimal montoMembresia = socioMembresia.getMembresia().getPrecioTotal();
                        if (montoMembresia == null || montoMembresia.compareTo(BigDecimal.ZERO) <= 0) {
                                throw new RuntimeException("La membresía asociada no tiene un precio válido asignado.");
                        }
                        BigDecimal montoFormateado = montoMembresia.setScale(2, RoundingMode.HALF_UP);

                        PreferenceItemRequest item = PreferenceItemRequest.builder()
                                        .id(socioMembresia.getIdSocioMembresia().toString())
                                        .title("Pulse GYM - Membresia: " + socioMembresia.getMembresia().getNombre())
                                        .quantity(1)
                                        .unitPrice(montoFormateado)
                                        .currencyId("COP")
                                        .build();

                        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                        .success("http://localhost:5500/success.html")
                                        .failure("http://localhost:5500/failure.html")
                                        .pending("http://localhost:5500/pending.html")
                                        .build();

                        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                                        .items(List.of(item))
                                        .backUrls(backUrls)
                                        .externalReference(socioMembresia.getIdSocioMembresia().toString())
                                        .build();

                        Preference preference = client.create(preferenceRequest, requestOptions);

                        Pago nuevoPago = new Pago();
                        nuevoPago.setSocioMembresia(socioMembresia);
                        nuevoPago.setMonto(montoFormateado);
                        nuevoPago.setFechaPago(LocalDateTime.now());
                        nuevoPago.setMetodoPago(requestDTO.getMetodoPago());
                        nuevoPago.setEstado(EnumEstadoPago.PENDIENTE);
                        nuevoPago.setAnulado(false);

                        nuevoPago.setNumeroComprobante(preference.getId());

                        pagoRepository.save(nuevoPago);

                        return new PreferenceResponseDTO(preference.getId(), preference.getSandboxInitPoint());

                } catch (MPApiException apiException) {
                        System.err.println("=== ERROR DETALLADO DE MERCADO PAGO ===");
                        System.err.println("Status Código: " + apiException.getStatusCode());
                        System.err.println("Cuerpo de Respuesta de MP: " + apiException.getApiResponse().getContent());
                        System.err.println("=======================================");
                        throw new RuntimeException("Mercado Pago falló: " + apiException.getApiResponse().getContent());
                } catch (Exception e) {
                        throw new RuntimeException("Error general al inicializar pago: " + e.getMessage());
                }
        }
}
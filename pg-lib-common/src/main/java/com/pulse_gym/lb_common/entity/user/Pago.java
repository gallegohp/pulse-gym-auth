package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoPago;
import com.pulse_gym.lb_common.enums.EnumMetodoPago;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pago")
@Data
public class Pago {

    /** Identificador único del pago */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    /** Membresía asociada al pago */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_socio_membresia", nullable = false)
    private SocioMembresia socioMembresia;

    /** Monto total del pago */
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    /** Fecha y hora en que se realizó el pago */
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    /** Método de pago utilizado (efectivo, transferencia, tarjeta, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private EnumMetodoPago metodoPago;

    /** Número de comprobante o referencia del pago */
    @Column(name = "numero_comprobante", length = 50)
    private String numeroComprobante;

    /** Administrador que registró el pago */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_admin_registro")
    private UsuarioPerfil adminRegistro;

    /** Observaciones adicionales sobre el pago */
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    /** Indica si el pago ha sido anulado */
    @Column(name = "anulado", nullable = false)
    private Boolean anulado = false;

    /** Fecha y hora en que se anuló el pago */
    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;

    /** Motivo de la anulación del pago */
    @Column(name = "motivo_anulacion", columnDefinition = "TEXT")
    private String motivoAnulacion;

    /** Estado del pago (PENDIENTE, APROBADO, RECHAZADO, ANULADO) */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EnumEstadoPago estado;

    /**
     * Asigna automáticamente la fecha actual si no se especifica antes de persistir
     */
    @PrePersist
    protected void onCreate() {
        if (fechaPago == null) {
            fechaPago = LocalDateTime.now();
        }
    }

    /**
     * Verifica si el pago está anulado
     * 
     * @return true si el pago está anulado, false en caso contrario
     */
    public boolean isAnulado() {
        return anulado != null && anulado;
    }
}
package com.habimed.habimedWebService.detallePago.domain.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.habimed.habimedWebService.cita.domain.model.Cita;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddetallepago")
    private Integer idDetallePago;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcita", referencedColumnName = "idcita")
    @JsonBackReference
    private Cita cita;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPagoEnum metodoPago;

    @Column(name = "estado_pago", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPagoEnum estadoPago;

}


package com.habimed.habimedWebService.detallePago.dto;

import com.habimed.habimedWebService.detallePago.domain.model.EstadoPagoEnum;
import com.habimed.habimedWebService.detallePago.domain.model.MetodoPagoEnum;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetallePagoResponseDto {
    private Integer idDetallePago;
    private Integer idCita;
    private BigDecimal monto;
    private MetodoPagoEnum metodoPago;
    private EstadoPagoEnum estadoPago;
}

package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import lombok.Data;

@Data
public class UsuarioFilterDto {
    private Integer dniPersona;
    private TipoUsuarioEnum tipoUsuario;
    private String usuario;
    private Boolean estado;
}
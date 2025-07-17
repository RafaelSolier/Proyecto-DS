package com.habimed.habimedWebService.auth.dto;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import lombok.Data;

@Data
public class LoginResponse {
    private Long idUsuario;
    private String correo;
    private TipoUsuarioEnum tipoUsuario;
    private String mensaje;
}
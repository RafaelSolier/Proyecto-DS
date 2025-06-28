package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.persona.dto.PersonaInsertDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUsuarioRequest {
    private PersonaInsertDto persona;
    private UsuarioInsertDto usuario;
}
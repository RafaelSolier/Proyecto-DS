package com.habimed.habimedWebService.auth;

import com.habimed.habimedWebService.auth.dto.LoginRequest;
import com.habimed.habimedWebService.auth.dto.LoginResponse;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.dto.UsuarioResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("login")
    public ResponseEntity<UsuarioResponseDto> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 2. Autenticación manual (sin Spring Security)
            UsuarioResponseDto usuarioAutenticado = usuarioService.validarCredenciales(
                    loginRequest.getUsuario(),
                    loginRequest.getContrasenia()
            );

            if (usuarioAutenticado == null) {
                throw new BadRequestException("Credenciales inválidas");
            }

            // 3. Respuesta exitosa (sin token)

            return ResponseEntity.ok(usuarioAutenticado);

        } catch (Exception e) {
            throw new RuntimeException("Error en el servidor: " + e.getMessage());
        }
    }
}

package com.habimed.habimedWebService.auth;

import com.habimed.habimedWebService.auth.dto.LoginRequest;
import com.habimed.habimedWebService.auth.dto.LoginResponse;
import com.habimed.habimedWebService.auth.dto.RegisterRequest;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
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
            UsuarioResponseDto usuarioAutenticado = usuarioService.validarCredenciales(loginRequest);

            if (usuarioAutenticado == null) {
                throw new BadRequestException("Credenciales inválidas");
            }

            // 3. Respuesta exitosa (sin token)

            return ResponseEntity.ok(usuarioAutenticado);

        } catch (Exception e) {
            throw new RuntimeException("Error en el servidor: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDto> register(@RequestBody RegisterRequest registerRequest) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(registerRequest));
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }
}

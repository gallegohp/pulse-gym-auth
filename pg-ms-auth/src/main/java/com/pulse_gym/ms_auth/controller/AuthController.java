package com.pulse_gym.ms_auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RestablecerContraseña;
import com.pulse_gym.lb_common.entity.auth.User;
import com.pulse_gym.lb_common.dto.AuthUserDTO;
import com.pulse_gym.lb_common.dto.ContrasenaOlvidad;
import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
import com.pulse_gym.lb_common.dto.JwtDTO;
import com.pulse_gym.ms_auth.dto.LoginRequestDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.repository.UserAuthRepository;
import com.pulse_gym.ms_auth.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    /**
     * Inyeccion de AuthService para manejar la lógica de autenticación
     */
    private final AuthService authService;

    private final UserAuthRepository userAuthRepository;
    /**
     * Registro de usuario
     * 
     * @param requestDTO
     * @return ResponseEntity<RegisterResponseDTO>
     */
    @PostMapping("/register")
    public ResponseEntity<MessegeGlobalDTO> register(@RequestBody RegisterRequestDTO requestDTO) {
        try {
            MessegeGlobalDTO messegeGlobalDTO = authService.register(requestDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messegeGlobalDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Inicio de sesion del usuario
     * 
     * @param request
     * @return HttpGlobalResponse<JwtDTO>
     */
    @PostMapping("/login")
    public ResponseEntity<HttpGlobalResponse<JwtDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            HttpGlobalResponse<JwtDTO> response = authService.login(request);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Refresco del jwt
     * 
     * @param request
     * @return JwtDTO
     */
    @GetMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        JwtDTO response = new JwtDTO();

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Endpoint para solicitar recuperación de contraseña
     * 
     * @param requestDTO Contiene el username del usuario
     * @return Mensaje de confirmación
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<MessegeGlobalDTO> forgotPassword(@Valid @RequestBody ContrasenaOlvidad requestDTO) {
        try {
            MessegeGlobalDTO response = authService.forgotPassword(requestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessegeGlobalDTO("Error al procesar la solicitud"));
        }
    }

    /**
     * Endpoint para restablecer la contraseña con token
     * 
     * @param requestDTO Contiene token y nueva contraseña
     * @return Mensaje de éxito o error
     */
    @PostMapping("/reset-password")
    public ResponseEntity<MessegeGlobalDTO> resetPassword(@Valid @RequestBody RestablecerContraseña requestDTO) {
        try {
            MessegeGlobalDTO response = authService.resetPassword(requestDTO);
            HttpStatus status = response.getMessage().contains("exitosamente") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessegeGlobalDTO("Error al restablecer la contraseña"));
        }
    }

    @GetMapping("/api/internal/users/email/{email}")
    public ResponseEntity<AuthUserDTO> getUserByEmail(@PathVariable String email) {
        User user = userAuthRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        AuthUserDTO dto = new AuthUserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRol(user.getRol());
        dto.setEstado(user.getEstado());
        return ResponseEntity.ok(dto);
    }
}

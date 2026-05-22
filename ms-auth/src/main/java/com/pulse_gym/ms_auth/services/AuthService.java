package com.pulse_gym.ms_auth.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
import com.pulse_gym.lb_common.dto.JwtDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.User;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.services.JwtService;
import com.pulse_gym.ms_auth.dto.LoginRequestDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {

    /**
     * Repositorio de usuarios
     */
    private final UserAuthRepository userAuthRepository;

    /**
     * Codificador de contraseñas
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * DTO para mensajes globales
     */
    private final MessegeGlobalDTO messegeGlobalDTO;

    /**
     * Servicio para manejo de JWT
     */
    private final JwtService jwtService;

    /**
     * Registro de usuario
     * 
     * @param requestDTO
     * @return MessegeGlobalDTO
     */
    public MessegeGlobalDTO register(RegisterRequestDTO requestDTO) {

        if (userAuthRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            messegeGlobalDTO.setMessage("El correo ya esta en uso");
            return messegeGlobalDTO;
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setUsername(requestDTO.getUsername());
        user.setRol(requestDTO.getRol());
        user.setEstado(requestDTO.getEstado());
        user.setFechaRegistro(LocalDateTime.now());
        userAuthRepository.save(user);

        messegeGlobalDTO.setMessage("Se ha registrado correctamente");
        return messegeGlobalDTO;

    }

    /**
     * Inicio de sesion
     * 
     * @param requestDTO
     * @return HttpGlobalResponse<JwtDTO>
     */
    public HttpGlobalResponse<JwtDTO> login(LoginRequestDTO requestDTO) {
        HttpGlobalResponse<JwtDTO> response = new HttpGlobalResponse<>();
        Optional<User> userFound = userAuthRepository.findByEmail(requestDTO.getEmail());

        if (userFound.isEmpty()) {
            response.setMessege("Este usuario no se encuentra registrado");
            return response;
        }

        User user = userFound.get();

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            response.setMessege("Correo o contraseña son incorrectos");
            return response;
        }

        JwtDTO jwtDTO = new JwtDTO();
        // Pasar el nombre del rol como String, NO user.getRol().name()
        String jwt = jwtService.generateToken(user.getId(), user.getRol().name(), user.getEmail());
        jwtDTO.setJwt(jwt);
        response.setMessege("Inicio de sesion exitoso");
        response.setData(jwtDTO);
        return response;
    }

    /**
     * Refresco del jwt
     * 
     * @param token
     * @return JwtDTO
     * @throws Exception
     */
    public JwtDTO refreshToken(String token) throws Exception {
        JwtDTO responseDTO = new JwtDTO();
        String jwt = jwtService.refreshToken(token);
        responseDTO.setJwt(jwt);
        return responseDTO;
    }
}

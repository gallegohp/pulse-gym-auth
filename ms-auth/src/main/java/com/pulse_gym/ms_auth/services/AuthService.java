package com.pulse_gym.ms_auth.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.pulse_gym.lb_common.dto.JwtDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.services.JwtService;
import com.pulse_gym.ms_auth.dto.HttpGlobalResponse;
import com.pulse_gym.ms_auth.dto.LoginRequestDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.entity.User;
import com.pulse_gym.ms_auth.enums.EnumRol;
import com.pulse_gym.ms_auth.repository.UserAuthRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {
    

    private final UserAuthRepository userAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessegeGlobalDTO messegeGlobalDTO;

    private final JwtService jwtService;



    public MessegeGlobalDTO register(RegisterRequestDTO requestDTO) {

        if (userAuthRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            messegeGlobalDTO.setMessage("El correo ya esta en uso");
            return messegeGlobalDTO;
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setUsername(requestDTO.getUsername());
        user.setRol(EnumRol.fromId(requestDTO.getRol()));
        user.setEstado(requestDTO.getEstado());
        user.setFechaRegistro(LocalDateTime.now());
        userAuthRepository.save(user);

        messegeGlobalDTO.setMessage("Se ha registrado correctamente");
        return messegeGlobalDTO;

    }

    /**
     * Inicio de sesion
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
        String jwt = jwtService.generateToken(user.getId(),user.getRol().getId(), user.getEmail());
        jwtDTO.setJwt(jwt);
        response.setMessege("Inicio de sesion exitoso");
        response.setData(jwtDTO);
        return response;
    }

    /**
     * Refresco del jwt
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

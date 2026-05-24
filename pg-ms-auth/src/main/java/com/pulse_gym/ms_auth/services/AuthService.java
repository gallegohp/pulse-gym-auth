package com.pulse_gym.ms_auth.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.ContrasenaOlvidad;
import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
import com.pulse_gym.lb_common.dto.JwtDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RestablecerContraseña;
import com.pulse_gym.lb_common.entity.auth.PasswordResetToken;
import com.pulse_gym.lb_common.entity.auth.User;
import com.pulse_gym.lb_common.services.JwtService;
import com.pulse_gym.ms_auth.dto.LoginRequestDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.repository.PasswordResetTokenRepository;
import com.pulse_gym.ms_auth.repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    /** Repositorio de UserAuthRepository */
    private final UserAuthRepository userAuthRepository;

    /** */
    private final PasswordEncoder passwordEncoder;

    /** Servicio de Jwt */
    private final JwtService jwtService;

    /** Servicio de Email */
    private final EmailService emailService;

    /** Servicio de Restableer la contraseña */
    private final PasswordResetTokenRepository tokenRepository;
    @Value("${app.security.reset-token-expiration-minutes:10}")
    private long tokenExpirationMinutes;

    /**
     * Registra un nuevo usuario en el sistema
     * @param requestDTO Datos del usuario a registrar (email, password, username, rol, estado)
     * @return Mensaje de éxito si se registró correctamente, o error si el email ya existe
     */
    public MessegeGlobalDTO register(RegisterRequestDTO requestDTO) {

        if (userAuthRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            return new MessegeGlobalDTO("El correo ya esta en uso");
        }

        User user = new User();
        user.setEmail(requestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setUsername(requestDTO.getUsername());
        user.setRol(requestDTO.getRol());
        user.setEstado(requestDTO.getEstado());
        user.setFechaRegistro(LocalDateTime.now());
        userAuthRepository.save(user);

        return new MessegeGlobalDTO("Se ha registrado correctamente");
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

    /**
     * Solicita recuperación de contraseña
     * 
     * @param requestDTO Contiene el username del usuario
     * @return Mensaje de éxito o error
     */
    @Transactional
    public MessegeGlobalDTO forgotPassword(ContrasenaOlvidad requestDTO) {
        Optional<User> userOpt = userAuthRepository.findByUsername(requestDTO.getUsername());

        if (userOpt.isEmpty()) {
            return new MessegeGlobalDTO("Si el username existe, recibirás un email con instrucciones");
        }

        User user = userOpt.get();

        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmailSimple(user.getEmail(), user.getUsername(), token);

        return new MessegeGlobalDTO("Si el username existe, recibirás un email con instrucciones");
    }

    /**
     * Restablece la contraseña usando un token válido
     * 
     * @param requestDTO Contiene token, nueva contraseña y confirmación
     * @return Mensaje de éxito o error
     */
    @Transactional
    public MessegeGlobalDTO resetPassword(RestablecerContraseña requestDTO) {
        // Validar que las contraseñas coincidan
        if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmPassword())) {
            return new MessegeGlobalDTO("Las contraseñas no coinciden");
        }

        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(requestDTO.getToken());

        if (tokenOpt.isEmpty()) {
            return new MessegeGlobalDTO("Token inválido o expirado");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        if (resetToken.isUsed()) {
            return new MessegeGlobalDTO("Este token ya ha sido utilizado");
        }

        if (resetToken.isExpired()) {
            return new MessegeGlobalDTO("El token ha expirado");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userAuthRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return new MessegeGlobalDTO("Contraseña restablecida exitosamente");
    }
}

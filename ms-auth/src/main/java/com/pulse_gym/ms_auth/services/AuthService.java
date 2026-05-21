package com.pulse_gym.ms_auth.services;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.entity.User;
import com.pulse_gym.ms_auth.enums.EnumRol;
import com.pulse_gym.ms_auth.repository.UserAuthRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AuthService {
    

    private final UserAuthRepository userAuthRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessegeGlobalDTO messegeGlobalDTO;

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

}

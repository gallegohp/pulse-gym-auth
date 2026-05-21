package com.pulse_gym.ms_auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.ms_auth.dto.RegisterRequestDTO;
import com.pulse_gym.ms_auth.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    

    private final AuthService authService;

    private final MessegeGlobalDTO messegeGlobalDTO;

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
}

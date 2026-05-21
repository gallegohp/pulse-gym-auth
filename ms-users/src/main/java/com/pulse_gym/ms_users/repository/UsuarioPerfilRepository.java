package com.pulse_gym.ms_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.ms_users.entity.UsuarioPerfil;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    Optional<UsuarioPerfil> findByDocumentoIdentidad(String documentoIdentidad);

}
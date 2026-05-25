package com.pulse_gym.ms_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    /**
     * Busca un usuario por su documento de identidad.
     *
     * @param documentoIdentidad El documento del usuario
     * @return El usuario si existe, o vacío si no se encuentra
     */
    Optional<UsuarioPerfil> findByDocumentoIdentidad(String documentoIdentidad);

}
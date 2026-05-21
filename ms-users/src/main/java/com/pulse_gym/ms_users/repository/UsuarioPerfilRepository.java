package com.pulse_gym.ms_users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.ms_users.entity.UsuarioPerfil;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Long> {

    /**
     * Realiza una consulta en la base de datos para buscar un usuario específico
     * mediante su número de documento de identidad.
     *
     * @param documentoIdentidad El número de documento único que se desea buscar.
     * @return Un {@link Optional} que contiene el {@link UsuarioPerfil} si es
     *         encontrado,
     *         o un contenedor vacío si no existe ningún registro con ese documento.
     */
    Optional<UsuarioPerfil> findByDocumentoIdentidad(String documentoIdentidad);

}
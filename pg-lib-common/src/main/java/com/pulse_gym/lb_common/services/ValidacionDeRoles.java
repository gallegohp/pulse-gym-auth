package com.pulse_gym.lb_common.services;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;

@Service
public class ValidacionDeRoles {
        /**
         * Valida que el usuario tenga rol de administrador
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es administrador
         */
        public static void validarAdmin(String currentRole) {
                if (currentRole == null || !EnumRol.administrador.name().equals(currentRole)) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario tenga rol de entrenador
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es entrenador
         */
        public static void validarEntrenador(String currentRole) {
                if (currentRole == null || !EnumRol.entrenador.name().equals(currentRole)) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de entrenador. Rol actual: " + currentRole);
                }
        }

        /**
         * Valida que el usuario tenga rol de recepcionista
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es recepcionista
         */
        public static void validarRecepcionista(String currentRole) {
                if (currentRole == null || !EnumRol.recepcionista.name().equals(currentRole)) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de recepcionista. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario tenga rol de socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es socio
         */
        public static void validarSocio(String currentRole) {
                if (currentRole == null || !EnumRol.socio.name().equals(currentRole)) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de socio. Rol actual: " + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador o entrenador
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es administrador ni
         *                                        entrenador
         */
        public static void validarAdminOEntrenador(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.entrenador.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador o entrenador. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador o recepcionista
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es administrador ni
         *                                        recepcionista
         */
        public static void validarAdminORecepcionista(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador o recepcionista. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es administrador ni socio
         */
        public static void validarAdminOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea entrenador o recepcionista
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es entrenador ni
         *                                        recepcionista
         */
        public static void validarEntrenadorORecepcionista(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de entrenador o recepcionista. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea entrenador o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es entrenador ni socio
         */
        public static void validarEntrenadorOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de entrenador o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea recepcionista o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no es recepcionista ni socio
         */
        public static void validarRecepcionistaOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.recepcionista.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de recepcionista o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador, entrenador o recepcionista
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no está en la lista
         *                                        permitida
         */
        public static void validarAdminOEntrenadorORecepcionista(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador, entrenador o recepcionista. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador, entrenador o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no está en la lista
         *                                        permitida
         */
        public static void validarAdminOEntrenadorOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador, entrenador o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea administrador, recepcionista o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no está en la lista
         *                                        permitida
         */
        public static void validarAdminORecepcionistaOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de administrador, recepcionista o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario sea entrenador, recepcionista o socio
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol no está en la lista
         *                                        permitida
         */
        public static void validarEntrenadorORecepcionistaOSocio(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere rol de entrenador, recepcionista o socio. Rol actual: "
                                                        + currentRole);
                }
        }

        /**
         * Valida que el usuario tenga cualquier rol (administrador, entrenador,
         * recepcionista o socio)
         * Es decir, solo valida que el usuario esté autenticado (tenga un rol válido)
         * 
         * @param currentRole Rol actual del usuario
         * @throws SecurityAuthorizationException Si el rol es null o no es un rol
         *                                        válido
         */
        public static void validarCualquierRol(String currentRole) {
                if (currentRole == null ||
                                (!EnumRol.administrador.name().equals(currentRole) &&
                                                !EnumRol.entrenador.name().equals(currentRole) &&
                                                !EnumRol.recepcionista.name().equals(currentRole) &&
                                                !EnumRol.socio.name().equals(currentRole))) {
                        throw new SecurityAuthorizationException(
                                        "Acceso denegado. Se requiere un rol válido (administrador, entrenador, recepcionista o socio). Rol actual: "
                                                        + currentRole);
                }
        }

}

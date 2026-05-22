// package com.pulse_gym.ms_users.security;

// import org.springframework.stereotype.Component;
// import org.springframework.web.context.request.RequestAttributes;
// import org.springframework.web.context.request.RequestContextHolder;

// @Component
// public class SecurityContext {

//     public String getCurrentRole() {
//         Object role = RequestContextHolder.currentRequestAttributes()
//                 .getAttribute("X-User-Rol", RequestAttributes.SCOPE_REQUEST);
        
//         System.out.println("SECURITY CONTEXT - Rol: " + role);
//         return role != null ? role.toString() : null;
//     }
// }
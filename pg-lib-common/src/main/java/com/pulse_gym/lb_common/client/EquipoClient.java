package com.pulse_gym.lb_common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pulse_gym.lb_common.dto.ConsultaEquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoResponseWrapperDTO;

@FeignClient(name = "pg-ms-operation", url = "${microservicio.operacion.url:http://pg-ms-operation:8082}")
public interface EquipoClient {

    /**
     * Consulta equipos según los criterios de búsqueda
     * 
     * @param request DTO con los filtros de búsqueda
     * @return Wrapper con la lista de equipos que coinciden con los criterios
     */
    @PostMapping("/api/equipos/consultar")
    EquipoResponseWrapperDTO consultarEquipos(@RequestBody ConsultaEquipoRequestDTO request);
}
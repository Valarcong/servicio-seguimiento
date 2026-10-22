package pe.edu.cibertec.servicioseguimiento.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.servicioseguimiento.client.dto.AsignacionResponseDto;
import pe.edu.cibertec.servicioseguimiento.client.dto.TareaResponseDto;

import java.util.List;

@FeignClient(name = "servicio-academico")
public interface AcademicoClient {

    @GetMapping("/api/v1/asignaciones/{idAsignacion}")
    AsignacionResponseDto obtenerAsignacionPorId(
            @PathVariable Integer idAsignacion
    );

    @GetMapping("/api/v1/asignaciones/mis-asignaciones")
    List<AsignacionResponseDto> listarMisAsignaciones();

    @GetMapping("/api/v1/tareas/{idTarea}")
    TareaResponseDto obtenerTareaPorId(
            @PathVariable Integer idTarea
    );
}
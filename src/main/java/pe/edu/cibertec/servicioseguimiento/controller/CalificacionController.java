package pe.edu.cibertec.servicioseguimiento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.servicioseguimiento.dto.CalificacionDto;
import pe.edu.cibertec.servicioseguimiento.entity.Calificacion;
import pe.edu.cibertec.servicioseguimiento.service.CalificacionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<Calificacion>> listarCalificaciones() {

        return ResponseEntity.ok(
                calificacionService.listarCalificaciones()
        );
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerCalificacionPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionPorId(id)
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<Calificacion>> listarPorMatricula(
            @PathVariable Integer idMatricula) {

        return ResponseEntity.ok(
                calificacionService.listarPorMatriculaDocente(
                        idMatricula
                )
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @GetMapping("/tarea/{idTarea}")
    public ResponseEntity<List<Calificacion>> listarPorTarea(
            @PathVariable Integer idTarea) {

        return ResponseEntity.ok(
                calificacionService.listarPorTareaDocente(
                        idTarea
                )
        );
    }

    @PreAuthorize("hasRole('ALUMNO')")
    @GetMapping("/mis-calificaciones")
    public ResponseEntity<List<Calificacion>> listarMisCalificaciones(
            Authentication authentication) {

        Integer idAlumno =
                (Integer) authentication.getDetails();

        return ResponseEntity.ok(
                calificacionService.listarMisCalificaciones(
                        idAlumno
                )
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PostMapping
    public ResponseEntity<Calificacion> registrarCalificacion(
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.registrarCalificacion(dto)
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarCalificacion(
            @PathVariable Integer id,
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.actualizarCalificacion(
                        id,
                        dto
                )
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @PatchMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarParcialmente(
            @PathVariable Integer id,
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.actualizarParcialmente(
                        id,
                        dto
                )
        );
    }

    @PreAuthorize("hasRole('DOCENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarCalificacion(
            @PathVariable Integer id) {

        calificacionService.eliminarCalificacion(id);

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Calificación eliminada correctamente"
                )
        );
    }
}
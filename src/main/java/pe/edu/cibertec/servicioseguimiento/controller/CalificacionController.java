package pe.edu.cibertec.servicioseguimiento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<Calificacion>> listarCalificaciones() {
        return ResponseEntity.ok(
                calificacionService.listarCalificaciones()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerCalificacionPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                calificacionService.obtenerCalificacionPorId(id)
        );
    }

    @GetMapping("/matricula/{idMatricula}")
    public ResponseEntity<List<Calificacion>> listarPorMatricula(
            @PathVariable Integer idMatricula) {

        return ResponseEntity.ok(
                calificacionService.listarPorMatricula(idMatricula)
        );
    }

    @GetMapping("/tarea/{idTarea}")
    public ResponseEntity<List<Calificacion>> listarPorTarea(
            @PathVariable Integer idTarea) {

        return ResponseEntity.ok(
                calificacionService.listarPorTarea(idTarea)
        );
    }

    @PostMapping
    public ResponseEntity<Calificacion> registrarCalificacion(
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.registrarCalificacion(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarCalificacion(
            @PathVariable Integer id,
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.actualizarCalificacion(id, dto)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarParcialmente(
            @PathVariable Integer id,
            @RequestBody CalificacionDto dto) {

        return ResponseEntity.ok(
                calificacionService.actualizarParcialmente(id, dto)
        );
    }

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
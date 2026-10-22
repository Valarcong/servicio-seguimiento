package pe.edu.cibertec.servicioseguimiento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.servicioseguimiento.dto.MatriculaDto;
import pe.edu.cibertec.servicioseguimiento.entity.Matricula;
import pe.edu.cibertec.servicioseguimiento.service.MatriculaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping
    public ResponseEntity<List<Matricula>> listarMatriculas() {
        return ResponseEntity.ok(
                matriculaService.listarMatriculas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> obtenerMatriculaPorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                matriculaService.obtenerMatriculaPorId(id)
        );
    }

    @GetMapping("/alumno/{idAlumno}")
    public ResponseEntity<List<Matricula>> listarPorAlumno(
            @PathVariable Integer idAlumno) {

        return ResponseEntity.ok(
                matriculaService.listarPorAlumno(idAlumno)
        );
    }

    @GetMapping("/asignacion/{idAsignacion}")
    public ResponseEntity<List<Matricula>> listarPorAsignacion(
            @PathVariable Integer idAsignacion) {

        return ResponseEntity.ok(
                matriculaService.listarPorAsignacion(idAsignacion)
        );
    }

    @PostMapping
    public ResponseEntity<Matricula> registrarMatricula(
            @RequestBody MatriculaDto dto) {

        return ResponseEntity.ok(
                matriculaService.registrarMatricula(dto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Matricula> actualizarMatricula(
            @PathVariable Integer id,
            @RequestBody MatriculaDto dto) {

        return ResponseEntity.ok(
                matriculaService.actualizarMatricula(id, dto)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Matricula> actualizarParcialmente(
            @PathVariable Integer id,
            @RequestBody MatriculaDto dto) {

        return ResponseEntity.ok(
                matriculaService.actualizarParcialmente(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarMatricula(
            @PathVariable Integer id) {

        matriculaService.eliminarMatricula(id);

        return ResponseEntity.ok(
                Map.of(
                        "mensaje",
                        "Matrícula eliminada correctamente"
                )
        );
    }
}
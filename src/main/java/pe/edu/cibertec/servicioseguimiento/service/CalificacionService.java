package pe.edu.cibertec.servicioseguimiento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.servicioseguimiento.client.AcademicoClient;
import pe.edu.cibertec.servicioseguimiento.client.dto.TareaResponseDto;
import pe.edu.cibertec.servicioseguimiento.dto.CalificacionDto;
import pe.edu.cibertec.servicioseguimiento.entity.Calificacion;
import pe.edu.cibertec.servicioseguimiento.entity.Matricula;
import pe.edu.cibertec.servicioseguimiento.repository.CalificacionRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {

    private final CalificacionRepository calificacionRepository;
    private final MatriculaService matriculaService;
    private final AcademicoClient academicoClient;

    public List<Calificacion> listarCalificaciones() {
        return calificacionRepository.findAll();
    }

    public Calificacion obtenerCalificacionPorId(Integer id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Calificación no encontrada: " + id
                        )
                );
    }

    public List<Calificacion> listarPorMatricula(Integer idMatricula) {
        return calificacionRepository
                .findByMatriculaIdMatricula(idMatricula);
    }

    public List<Calificacion> listarPorTarea(Integer idTarea) {
        return calificacionRepository.findByIdTarea(idTarea);
    }

    public Calificacion registrarCalificacion(CalificacionDto dto) {

        Matricula matricula =
                matriculaService.obtenerMatriculaPorId(
                        dto.getIdMatricula()
                );

        validarTarea(dto.getIdTarea(), matricula);

        if (calificacionRepository
                .existsByMatriculaIdMatriculaAndIdTarea(
                        dto.getIdMatricula(),
                        dto.getIdTarea())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La matrícula ya tiene una calificación registrada para esta tarea"
            );
        }

        Calificacion calificacion = new Calificacion();
        calificacion.setMatricula(matricula);
        calificacion.setIdTarea(dto.getIdTarea());
        calificacion.setNota(dto.getNota());
        calificacion.setFechaRegistro(dto.getFechaRegistro());
        calificacion.setActivo(true);

        return calificacionRepository.save(calificacion);
    }

    public Calificacion actualizarCalificacion(
            Integer id,
            CalificacionDto dto) {

        Calificacion calificacion =
                obtenerCalificacionPorId(id);

        Matricula matricula =
                matriculaService.obtenerMatriculaPorId(
                        dto.getIdMatricula()
                );

        validarTarea(dto.getIdTarea(), matricula);

        boolean cambioRelacion =
                !calificacion.getMatricula()
                        .getIdMatricula()
                        .equals(dto.getIdMatricula())
                        ||
                        !calificacion.getIdTarea()
                                .equals(dto.getIdTarea());

        if (cambioRelacion
                && calificacionRepository
                .existsByMatriculaIdMatriculaAndIdTarea(
                        dto.getIdMatricula(),
                        dto.getIdTarea())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La matrícula ya tiene una calificación registrada para esta tarea"
            );
        }

        calificacion.setMatricula(matricula);
        calificacion.setIdTarea(dto.getIdTarea());
        calificacion.setNota(dto.getNota());
        calificacion.setFechaRegistro(dto.getFechaRegistro());

        return calificacionRepository.save(calificacion);
    }

    public Calificacion actualizarParcialmente(
            Integer id,
            CalificacionDto dto) {

        Calificacion calificacion =
                obtenerCalificacionPorId(id);

        Integer nuevaMatricula =
                dto.getIdMatricula() != null
                        ? dto.getIdMatricula()
                        : calificacion.getMatricula().getIdMatricula();

        Integer nuevaTarea =
                dto.getIdTarea() != null
                        ? dto.getIdTarea()
                        : calificacion.getIdTarea();

        Matricula matricula =
                matriculaService.obtenerMatriculaPorId(
                        nuevaMatricula
                );

        validarTarea(nuevaTarea, matricula);

        boolean cambioRelacion =
                !calificacion.getMatricula()
                        .getIdMatricula()
                        .equals(nuevaMatricula)
                        ||
                        !calificacion.getIdTarea()
                                .equals(nuevaTarea);

        if (cambioRelacion
                && calificacionRepository
                .existsByMatriculaIdMatriculaAndIdTarea(
                        nuevaMatricula,
                        nuevaTarea)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La matrícula ya tiene una calificación registrada para esta tarea"
            );
        }

        if (dto.getIdMatricula() != null) {
            calificacion.setMatricula(matricula);
        }

        if (dto.getIdTarea() != null) {
            calificacion.setIdTarea(dto.getIdTarea());
        }

        if (dto.getNota() != null) {
            calificacion.setNota(dto.getNota());
        }

        if (dto.getFechaRegistro() != null) {
            calificacion.setFechaRegistro(
                    dto.getFechaRegistro()
            );
        }

        return calificacionRepository.save(calificacion);
    }

    public void eliminarCalificacion(Integer id) {

        Calificacion calificacion =
                obtenerCalificacionPorId(id);

        validarTarea(
                calificacion.getIdTarea(),
                calificacion.getMatricula()
        );

        calificacionRepository.delete(calificacion);
    }

    private void validarTarea(
            Integer idTarea,
            Matricula matricula) {

        TareaResponseDto tarea =
                academicoClient.obtenerTareaPorId(idTarea);

        if (!Boolean.TRUE.equals(tarea.getActivo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La tarea no se encuentra activa"
            );
        }

        if (tarea.getAsignacionDocente() == null
                || tarea.getAsignacionDocente()
                .getIdAsignacion() == null
                || !tarea.getAsignacionDocente()
                .getIdAsignacion()
                .equals(matricula.getIdAsignacion())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La tarea no pertenece a la asignación de la matrícula"
            );
        }
    }

    public List<Calificacion> listarMisCalificaciones(
            Integer idAlumno) {

        return matriculaService.listarPorAlumno(idAlumno)
                .stream()
                .flatMap(matricula ->
                        calificacionRepository
                                .findByMatriculaIdMatricula(
                                        matricula.getIdMatricula()
                                )
                                .stream()
                )
                .toList();
    }

    public List<Calificacion> listarPorMatriculaDocente(
            Integer idMatricula) {

        Matricula matricula =
                matriculaService.obtenerMatriculaPorId(
                        idMatricula
                );

        validarAsignacionDocente(
                matricula.getIdAsignacion()
        );

        return calificacionRepository
                .findByMatriculaIdMatricula(
                        idMatricula
                );
    }

    public List<Calificacion> listarPorTareaDocente(
            Integer idTarea) {

        TareaResponseDto tarea =
                academicoClient.obtenerTareaPorId(
                        idTarea
                );

        if (!Boolean.TRUE.equals(
                tarea.getActivo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La tarea no se encuentra activa"
            );
        }

        return calificacionRepository
                .findByIdTarea(idTarea);
    }

    private void validarAsignacionDocente(
            Integer idAsignacion) {

        boolean perteneceAlDocente =
                academicoClient.listarMisAsignaciones()
                        .stream()
                        .anyMatch(asignacion ->
                                asignacion.getIdAsignacion()
                                        .equals(idAsignacion)
                                        && Boolean.TRUE.equals(
                                        asignacion.getActivo()
                                )
                        );

        if (!perteneceAlDocente) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "La asignación no pertenece al docente autenticado"
            );
        }
    }

}
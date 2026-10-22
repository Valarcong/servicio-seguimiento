package pe.edu.cibertec.servicioseguimiento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
        return calificacionRepository.findByMatriculaIdMatricula(idMatricula);
    }

    public List<Calificacion> listarPorTarea(Integer idTarea) {
        return calificacionRepository.findByIdTarea(idTarea);
    }

    public Calificacion registrarCalificacion(CalificacionDto dto) {

        Matricula matricula =
                matriculaService.obtenerMatriculaPorId(dto.getIdMatricula());

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
                matriculaService.obtenerMatriculaPorId(dto.getIdMatricula());

        boolean cambioRelacion =
                !calificacion.getMatricula()
                        .getIdMatricula()
                        .equals(dto.getIdMatricula())
                        || !calificacion.getIdTarea()
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

        boolean cambioRelacion =
                !calificacion.getMatricula()
                        .getIdMatricula()
                        .equals(nuevaMatricula)
                        || !calificacion.getIdTarea()
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
            Matricula matricula =
                    matriculaService.obtenerMatriculaPorId(
                            dto.getIdMatricula()
                    );

            calificacion.setMatricula(matricula);
        }

        if (dto.getIdTarea() != null) {
            calificacion.setIdTarea(dto.getIdTarea());
        }

        if (dto.getNota() != null) {
            calificacion.setNota(dto.getNota());
        }

        if (dto.getFechaRegistro() != null) {
            calificacion.setFechaRegistro(dto.getFechaRegistro());
        }

        return calificacionRepository.save(calificacion);
    }

    public void eliminarCalificacion(Integer id) {
        Calificacion calificacion =
                obtenerCalificacionPorId(id);

        calificacionRepository.delete(calificacion);
    }
}
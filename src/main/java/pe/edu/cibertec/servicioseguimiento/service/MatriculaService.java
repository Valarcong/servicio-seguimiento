package pe.edu.cibertec.servicioseguimiento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.servicioseguimiento.client.AcademicoClient;
import pe.edu.cibertec.servicioseguimiento.client.UsuarioClient;
import pe.edu.cibertec.servicioseguimiento.client.dto.AsignacionResponseDto;
import pe.edu.cibertec.servicioseguimiento.client.dto.UsuarioResponseDto;
import pe.edu.cibertec.servicioseguimiento.dto.MatriculaDto;
import pe.edu.cibertec.servicioseguimiento.entity.Matricula;
import pe.edu.cibertec.servicioseguimiento.repository.MatriculaRepository;
import pe.edu.cibertec.servicioseguimiento.client.dto.TareaResponseDto;
import pe.edu.cibertec.servicioseguimiento.dto.CursoAlumnoDto;
import pe.edu.cibertec.servicioseguimiento.dto.TareaAlumnoDto;
import pe.edu.cibertec.servicioseguimiento.entity.Calificacion;
import pe.edu.cibertec.servicioseguimiento.repository.CalificacionRepository;



import java.util.List;

@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final UsuarioClient usuarioClient;
    private final AcademicoClient academicoClient;
    private final CalificacionRepository calificacionRepository;

    public List<Matricula> listarMatriculas() {
        return matriculaRepository.findAll();
    }

    public Matricula obtenerMatriculaPorId(Integer id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Matrícula no encontrada: " + id
                        )
                );
    }

    public List<Matricula> listarPorAlumno(Integer idAlumno) {
        return matriculaRepository.findByIdAlumno(idAlumno);
    }

    public List<Matricula> listarPorAsignacion(Integer idAsignacion) {
        return matriculaRepository.findByIdAsignacion(idAsignacion);
    }

    public Matricula registrarMatricula(MatriculaDto dto) {

        validarAlumno(dto.getIdAlumno());
        validarAsignacion(dto.getIdAsignacion());

        if (matriculaRepository.existsByIdAlumnoAndIdAsignacion(
                dto.getIdAlumno(),
                dto.getIdAsignacion())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El alumno ya se encuentra matriculado en esta asignación"
            );
        }

        Matricula matricula = new Matricula();

        matricula.setIdAlumno(dto.getIdAlumno());
        matricula.setIdAsignacion(dto.getIdAsignacion());
        matricula.setFechaMatricula(dto.getFechaMatricula());
        matricula.setActivo(true);

        return matriculaRepository.save(matricula);
    }

    public Matricula actualizarMatricula(
            Integer id,
            MatriculaDto dto) {

        Matricula matricula =
                obtenerMatriculaPorId(id);

        validarAlumno(dto.getIdAlumno());
        validarAsignacion(dto.getIdAsignacion());

        boolean cambioAsignacion =
                !matricula.getIdAlumno()
                        .equals(dto.getIdAlumno())
                        || !matricula.getIdAsignacion()
                        .equals(dto.getIdAsignacion());

        if (cambioAsignacion
                && matriculaRepository.existsByIdAlumnoAndIdAsignacion(
                dto.getIdAlumno(),
                dto.getIdAsignacion())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El alumno ya se encuentra matriculado en esta asignación"
            );
        }

        matricula.setIdAlumno(dto.getIdAlumno());
        matricula.setIdAsignacion(dto.getIdAsignacion());
        matricula.setFechaMatricula(dto.getFechaMatricula());

        return matriculaRepository.save(matricula);
    }

    public Matricula actualizarParcialmente(
            Integer id,
            MatriculaDto dto) {

        Matricula matricula =
                obtenerMatriculaPorId(id);

        if (dto.getIdAlumno() != null) {
            validarAlumno(dto.getIdAlumno());
        }

        if (dto.getIdAsignacion() != null) {
            validarAsignacion(dto.getIdAsignacion());
        }

        Integer nuevoAlumno =
                dto.getIdAlumno() != null
                        ? dto.getIdAlumno()
                        : matricula.getIdAlumno();

        Integer nuevaAsignacion =
                dto.getIdAsignacion() != null
                        ? dto.getIdAsignacion()
                        : matricula.getIdAsignacion();

        boolean cambioAsignacion =
                !matricula.getIdAlumno()
                        .equals(nuevoAlumno)
                        || !matricula.getIdAsignacion()
                        .equals(nuevaAsignacion);

        if (cambioAsignacion
                && matriculaRepository.existsByIdAlumnoAndIdAsignacion(
                nuevoAlumno,
                nuevaAsignacion)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El alumno ya se encuentra matriculado en esta asignación"
            );
        }

        if (dto.getIdAlumno() != null) {
            matricula.setIdAlumno(
                    dto.getIdAlumno()
            );
        }

        if (dto.getIdAsignacion() != null) {
            matricula.setIdAsignacion(
                    dto.getIdAsignacion()
            );
        }

        if (dto.getFechaMatricula() != null) {
            matricula.setFechaMatricula(
                    dto.getFechaMatricula()
            );
        }

        return matriculaRepository.save(matricula);
    }

    public void eliminarMatricula(Integer id) {

        Matricula matricula =
                obtenerMatriculaPorId(id);

        matriculaRepository.delete(matricula);
    }

    public List<Matricula> listarPorAsignacionDocente(
            Integer idAsignacion) {

        AsignacionResponseDto asignacion =
                academicoClient.listarMisAsignaciones()
                        .stream()
                        .filter(item ->
                                item.getIdAsignacion()
                                        .equals(idAsignacion)
                        )
                        .findFirst()
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "La asignación no pertenece al docente autenticado"
                                )
                        );

        if (!Boolean.TRUE.equals(
                asignacion.getActivo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La asignación no se encuentra activa"
            );
        }

        return matriculaRepository
                .findByIdAsignacion(idAsignacion);
    }

    public List<CursoAlumnoDto> listarMisCursos(
            Integer idAlumno) {

        return matriculaRepository
                .findByIdAlumno(idAlumno)
                .stream()
                .filter(matricula ->
                        Boolean.TRUE.equals(
                                matricula.getActivo()
                        )
                )
                .map(this::construirCursoAlumno)
                .toList();
    }

    private CursoAlumnoDto construirCursoAlumno(
            Matricula matricula) {

        AsignacionResponseDto asignacion =
                academicoClient.obtenerAsignacionPorId(
                        matricula.getIdAsignacion()
                );

        CursoAlumnoDto dto =
                new CursoAlumnoDto();

        dto.setIdMatricula(
                matricula.getIdMatricula()
        );

        dto.setIdAsignacion(
                matricula.getIdAsignacion()
        );

        dto.setFechaMatricula(
                matricula.getFechaMatricula()
        );

        if (asignacion.getCurso() != null) {

            dto.setIdCurso(
                    asignacion.getCurso()
                            .getIdCurso()
            );

            dto.setCodigoCurso(
                    asignacion.getCurso()
                            .getCodigo()
            );

            dto.setNombreCurso(
                    asignacion.getCurso()
                            .getNombre()
            );

            dto.setDescripcionCurso(
                    asignacion.getCurso()
                            .getDescripcion()
            );
        }

        if (asignacion.getPeriodo() != null) {

            dto.setIdPeriodo(
                    asignacion.getPeriodo()
                            .getIdPeriodo()
            );

            dto.setPeriodo(
                    asignacion.getPeriodo()
                            .getNombre()
            );

            dto.setFechaInicioPeriodo(
                    asignacion.getPeriodo()
                            .getFechaInicio()
            );

            dto.setFechaFinPeriodo(
                    asignacion.getPeriodo()
                            .getFechaFin()
            );
        }

        List<Calificacion> calificaciones =
                calificacionRepository
                        .findByMatriculaIdMatricula(
                                matricula.getIdMatricula()
                        );

        List<TareaAlumnoDto> tareas =
                academicoClient
                        .listarTareasPorAsignacion(
                                matricula.getIdAsignacion()
                        )
                        .stream()
                        .filter(tarea ->
                                Boolean.TRUE.equals(
                                        tarea.getActivo()
                                )
                        )
                        .map(tarea ->
                                construirTareaAlumno(
                                        tarea,
                                        calificaciones
                                )
                        )
                        .toList();

        dto.setTareas(tareas);

        return dto;
    }

    private TareaAlumnoDto construirTareaAlumno(
            TareaResponseDto tarea,
            List<Calificacion> calificaciones) {

        TareaAlumnoDto dto =
                new TareaAlumnoDto();

        dto.setIdTarea(
                tarea.getIdTarea()
        );

        dto.setTitulo(
                tarea.getTitulo()
        );

        dto.setDescripcion(
                tarea.getDescripcion()
        );

        dto.setFechaPublicacion(
                tarea.getFechaPublicacion()
        );

        dto.setFechaEntrega(
                tarea.getFechaEntrega()
        );

        Calificacion calificacion =
                calificaciones
                        .stream()
                        .filter(item ->
                                Boolean.TRUE.equals(
                                        item.getActivo()
                                )
                                        &&
                                        item.getIdTarea()
                                                .equals(
                                                        tarea.getIdTarea()
                                                )
                        )
                        .findFirst()
                        .orElse(null);

        if (calificacion != null) {

            dto.setIdCalificacion(
                    calificacion.getIdCalificacion()
            );

            dto.setNota(
                    calificacion.getNota()
            );

            dto.setFechaRegistro(
                    calificacion.getFechaRegistro()
            );
        }

        return dto;
    }

    private void validarAlumno(Integer idAlumno) {

        UsuarioResponseDto usuario =
                usuarioClient.obtenerUsuarioPorId(
                        idAlumno
                );

        if (!Boolean.TRUE.equals(
                usuario.getActivo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El alumno no se encuentra activo"
            );
        }

        boolean tieneRolAlumno =
                usuario.getRoles()
                        .stream()
                        .anyMatch(rol ->
                                "ALUMNO".equals(
                                        rol.getNombre()
                                )
                        );

        if (!tieneRolAlumno) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario indicado no tiene rol ALUMNO"
            );
        }
    }

    private void validarAsignacion(
            Integer idAsignacion) {

        AsignacionResponseDto asignacion =
                academicoClient.obtenerAsignacionPorId(
                        idAsignacion
                );

        if (!Boolean.TRUE.equals(
                asignacion.getActivo())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La asignación no se encuentra activa"
            );
        }
    }
}
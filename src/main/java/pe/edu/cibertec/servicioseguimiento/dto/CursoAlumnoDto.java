package pe.edu.cibertec.servicioseguimiento.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CursoAlumnoDto {

    private Integer idMatricula;
    private Integer idAsignacion;
    private LocalDate fechaMatricula;

    private Integer idCurso;
    private String codigoCurso;
    private String nombreCurso;
    private String descripcionCurso;

    private Integer idPeriodo;
    private String periodo;
    private LocalDate fechaInicioPeriodo;
    private LocalDate fechaFinPeriodo;

    private List<TareaAlumnoDto> tareas;
}
package pe.edu.cibertec.servicioseguimiento.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MatriculaDto {

    private Integer idAlumno;
    private Integer idAsignacion;
    private LocalDate fechaMatricula;
}
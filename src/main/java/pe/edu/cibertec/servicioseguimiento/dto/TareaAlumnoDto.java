package pe.edu.cibertec.servicioseguimiento.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TareaAlumnoDto {

    private Integer idTarea;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaEntrega;

    private Integer idCalificacion;
    private BigDecimal nota;
    private LocalDateTime fechaRegistro;
}
package pe.edu.cibertec.servicioseguimiento.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalificacionDto {

    private Integer idMatricula;
    private Integer idTarea;
    private BigDecimal nota;
    private LocalDateTime fechaRegistro;
}
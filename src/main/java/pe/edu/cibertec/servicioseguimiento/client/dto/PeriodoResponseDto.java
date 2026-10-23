package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodoResponseDto {

    private Integer idPeriodo;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
}
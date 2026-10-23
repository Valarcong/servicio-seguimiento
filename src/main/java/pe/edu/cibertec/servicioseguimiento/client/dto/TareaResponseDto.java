package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TareaResponseDto {

    private Integer idTarea;
    private AsignacionResponseDto asignacionDocente;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaEntrega;
    private Boolean activo;
}
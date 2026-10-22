package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TareaResponseDto {

    private Integer idTarea;
    private AsignacionResponseDto asignacionDocente;
    private Boolean activo;
}
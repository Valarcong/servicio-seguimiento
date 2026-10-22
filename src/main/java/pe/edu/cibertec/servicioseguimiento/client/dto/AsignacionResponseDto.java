package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsignacionResponseDto {

    private Integer idAsignacion;
    private Integer idDocente;
    private Boolean activo;
}
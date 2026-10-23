package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoResponseDto {

    private Integer idCurso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
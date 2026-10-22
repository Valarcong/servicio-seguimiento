package pe.edu.cibertec.servicioseguimiento.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UsuarioResponseDto {

    private Integer idUsuario;
    private String nombreUsuario;
    private Set<RolResponseDto> roles;
    private Boolean activo;
}
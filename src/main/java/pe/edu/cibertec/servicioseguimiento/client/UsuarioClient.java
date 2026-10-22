package pe.edu.cibertec.servicioseguimiento.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.servicioseguimiento.client.dto.UsuarioResponseDto;

@FeignClient(name = "servicio-usuarios")
public interface UsuarioClient {

    @GetMapping("/api/v1/usuarios/{idUsuario}")
    UsuarioResponseDto obtenerUsuarioPorId(
            @PathVariable Integer idUsuario
    );
}
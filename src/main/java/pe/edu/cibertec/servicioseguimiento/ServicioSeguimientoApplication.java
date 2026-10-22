package pe.edu.cibertec.servicioseguimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ServicioSeguimientoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicioSeguimientoApplication.class, args);
    }

}

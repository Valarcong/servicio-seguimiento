package pe.edu.cibertec.servicioseguimiento.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes =
                    (ServletRequestAttributes)
                            RequestContextHolder.getRequestAttributes();

            if (attributes != null) {

                String authorization =
                        attributes.getRequest()
                                .getHeader(HttpHeaders.AUTHORIZATION);

                if (authorization != null
                        && !authorization.isBlank()) {

                    requestTemplate.header(
                            HttpHeaders.AUTHORIZATION,
                            authorization
                    );
                }
            }
        };
    }
}
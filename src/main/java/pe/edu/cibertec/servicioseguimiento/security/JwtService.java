package pe.edu.cibertec.servicioseguimiento.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.List;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String claveSecreta;

    public String obtenerNombreUsuario(String token) {
        return obtenerTodosLosClaims(token).getSubject();
    }

    public Integer obtenerIdUsuario(String token) {

        Number idUsuario =
                obtenerTodosLosClaims(token)
                        .get("idUsuario", Number.class);

        return idUsuario.intValue();
    }

    @SuppressWarnings("unchecked")
    public List<String> obtenerRoles(String token) {

        return obtenerTodosLosClaims(token)
                .get("roles", List.class);
    }

    public boolean validarToken(String token) {
        return !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {

        return obtenerTodosLosClaims(token)
                .getExpiration()
                .before(new java.util.Date());
    }

    private Claims obtenerTodosLosClaims(String token) {

        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClave() {

        byte[] keyBytes =
                Decoders.BASE64.decode(claveSecreta);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
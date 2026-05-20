package com.medicore.api.security;


// Interfaz con los datos del usuario autenticado
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component; // Registra esta clase como bean de Spring

import javax.crypto.SecretKey; // Clave secreta para firmar y verificar el token
import java.util.Date;         // Para manejar fechas de emisión y expiración
import java.util.List;         // Para la lista de roles del usuario

/**
 * Componente utilitario para la generación y validación de tokens JWT en MediCore.
 * Usa HMAC-SHA con una clave secreta configurada en {@code application.properties}.
 */
@Component
public class JwtUtil {

    // Clave secreta leída desde application.properties (jwt.secret), usada para firmar el token
    @Value("${jwt.secret}")
    private String secret;

    // Tiempo de expiración en milisegundos leído desde application.properties (jwt.expiration)
    @Value("${jwt.expiration}")
    private long expiration;

    // Decodifica la clave secreta de BASE64 y genera una clave HMAC-SHA para firmar/verificar tokens
    private SecretKey signingKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // Genera un token JWT a partir de los datos del usuario autenticado
    public String generateToken(UserDetails userDetails) {
        // Extrae los roles del usuario como lista de strings (ej. ["ROLE_ADMIN", "ROLE_USER"])
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())                          // Establece el subject (nombre de usuario)
                .claim("roles", roles)                                       // Agrega los roles como claim personalizado
                .issuedAt(new Date())                                        // Fecha y hora de emisión del token
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Fecha de expiración = ahora + tiempo configurado
                .signWith(signingKey())                                      // Firma el token con la clave secreta
                .compact();                                                  // Construye y serializa el token como String
    }

    // Extrae el nombre de usuario (subject) del token JWT
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Valida que el token pertenezca al usuario y que no haya expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    // Verifica si la fecha de expiración del token es anterior a la fecha actual
    private boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    // Parsea y verifica la firma del token, retornando los claims (payload) del mismo
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())    // Configura la clave para verificar la firma
                .build()
                .parseSignedClaims(token)   // Parsea el token y lanza excepción si la firma es inválida
                .getPayload();              // Retorna el payload con todos los claims
    }
}
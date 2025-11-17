package br.com.ingridemanuelle.tcc.sistema_agendamento_backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private final long TEMPO_EXPIRACAO_MS = 1000 * 60 * 120;

    public String gerarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        var roles = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TEMPO_EXPIRACAO_MS))
                .signWith(secretKey)
                .compact();
    }

    public String extrairEmail(String token) {
        return extrairClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extrairRoles(String token) {
        Claims claims = extrairTodosClaims(token);
        return claims.get("roles", List.class);
    }

    public Boolean validarToken(String token, UserDetails userDetails) {
        final String email = extrairEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }

    private boolean isTokenExpirado(String token) {
        return extrairDataExpiracao(token).before(new Date());
    }

    private Date extrairDataExpiracao(String token) {
        return extrairClaim(token, Claims::getExpiration);
    }

    private <T> T extrairClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrairTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extrairTodosClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
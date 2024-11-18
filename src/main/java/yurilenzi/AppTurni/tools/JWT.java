package yurilenzi.AppTurni.tools;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWT {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Utente utente){
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5))
                .subject(utente.getUsername())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public void verifyToken(String accesToken){
        try{
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(accesToken);
        } catch (Exception e) {
            throw new UnauthorizedException("Problemi con il token, rifare il login");
        }

    }

    public String getEmailFromToken(String accesToken){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseSignedClaims(accesToken)
                .getPayload()
                .getSubject();
    }

}

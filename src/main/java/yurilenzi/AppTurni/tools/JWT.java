package yurilenzi.AppTurni.tools;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {
    @Value("${jwt.secret}")
    private String secret;

}

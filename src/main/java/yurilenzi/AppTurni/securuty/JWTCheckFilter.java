package yurilenzi.AppTurni.securuty;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.exceptions.UnauthorizedException;
import yurilenzi.AppTurni.payloads.ErrorResponseDTO;
import yurilenzi.AppTurni.services.UtenteService;
import yurilenzi.AppTurni.tools.JWT;

import java.io.IOException;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {

    @Autowired
    JWT jwt;

    @Autowired
    UtenteService utenteService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, "Inserire il token corretto");
            return;
        }

        Utente currentUtente;
        try {
            String accesToken = authHeader.substring(7);
            jwt.verifyToken(accesToken);
            currentUtente = utenteService.findByEmail(jwt.getEmailFromToken(accesToken));
        } catch (UnauthorizedException | NotFoundException e) {
            writeUnauthorized(response, e instanceof NotFoundException ? "Problemi con il token, rifare il login" : e.getMessage());
            return;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    // Le eccezioni lanciate in un filtro non arrivano a @RestControllerAdvice: la risposta 401 si scrive qui
    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponseDTO(message)));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}

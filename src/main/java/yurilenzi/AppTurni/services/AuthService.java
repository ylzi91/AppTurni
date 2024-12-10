package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.UnauthorizedException;
import yurilenzi.AppTurni.payloads.LoginDTO;
import yurilenzi.AppTurni.payloads.LoginResponseDTO;
import yurilenzi.AppTurni.tools.JWT;

@Service
public class AuthService {
    @Autowired
    private JWT jwt;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PasswordEncoder bcrypt;

    public LoginResponseDTO checkCredentialAndResponseToken(LoginDTO body){
        Utente found = utenteService.findByEmail(body.email());
        if(bcrypt.matches(body.password(), found.getPassword())){
            String accesToken = jwt.createToken(found);
            return new LoginResponseDTO(accesToken, found.getRole());
        }
        else throw new UnauthorizedException("Credenziali errate");

    }
}

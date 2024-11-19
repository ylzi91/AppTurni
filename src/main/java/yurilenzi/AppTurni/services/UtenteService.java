package yurilenzi.AppTurni.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Role;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.exceptions.SameException;
import yurilenzi.AppTurni.payloads.NewUtenteDTO;
import yurilenzi.AppTurni.repositories.UtenteRepository;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordEncoder bcrypt;

    @Value("${admin.password}")
    private String password;

    public Utente saveUtenteBase(NewUtenteDTO body){
        if(utenteRepository.findById(body.email()).isPresent())
            throw new SameException("La mail: " + body.email() + " già in uso");
        else
            return utenteRepository.save(new Utente(body.email(), body.nome(), body.cognome(), bcrypt.encode(body.password())));
    }
    public Utente saveUtenteCapo(NewUtenteDTO body){
        if(utenteRepository.findById(body.email()).isPresent())
            throw new SameException("La mail: " + body.email() + " già in uso");
        else
            return utenteRepository.save(new Utente(body.email(), body.nome(), body.cognome(), bcrypt.encode(body.password()), Role.CAPO));
    }

    public Utente findByEmail(String email){
        Utente found = null;
        found = utenteRepository.findById(email).orElseThrow(() -> new NotFoundException("La email " + email + " non è stata trovata"));
        return found;
    }

    public void findByRoleAndCreateAdmin(){
        if(!utenteRepository.existsByRole(Role.ADMIN))
            utenteRepository.save(new Utente("ylzi91@gmail.com", "ADMIN", "ADMIN", bcrypt.encode(password), Role.ADMIN));
    }
}

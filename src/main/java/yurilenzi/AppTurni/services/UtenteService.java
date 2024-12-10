package yurilenzi.AppTurni.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import yurilenzi.AppTurni.entities.Role;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.exceptions.SameException;
import yurilenzi.AppTurni.payloads.GenericResponseDTO;
import yurilenzi.AppTurni.payloads.NewUtenteDTO;
import yurilenzi.AppTurni.payloads.UpadatePassword;
import yurilenzi.AppTurni.payloads.UpdateUtenteDTO;
import yurilenzi.AppTurni.repositories.UtenteRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UtenteService {
    @Autowired
    UtenteRepository utenteRepository;

    @Autowired
    PasswordEncoder bcrypt;

    @Autowired
    Cloudinary cloudinary;

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

    public List<Utente> findAllUtenti(){
        return utenteRepository.findByRole(Role.USER);
    }

    public GenericResponseDTO uploadAvatar(Utente utente, MultipartFile file) {

        String url = null;
        try {
            // Carica il file su Cloudinary e ottieni l'URL dell'immagine
            url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
            utente.setImmagine(url);
            utenteRepository.save(utente);
        } catch (IOException e) {
            throw new BadRequestException("Errore durante l'upload dell'immagine.");
        }

        return new GenericResponseDTO(url) ;
    }

    public Utente updateNomeCognome(Utente currentUtente, UpdateUtenteDTO body){
        currentUtente.setNome(body.nome());
        currentUtente.setCognome(body.cognome());
        return utenteRepository.save(currentUtente);
    }

    public void updatePassword(Utente currentUtente, UpadatePassword body){
        if(bcrypt.matches(body.oldPassword(), currentUtente.getPassword())){
            currentUtente.setPassword(bcrypt.encode(body.newPassword()));
            utenteRepository.save(currentUtente);
        }
        else{
            throw new BadRequestException("La vecchia password non corrisponde");
        }
    }



}

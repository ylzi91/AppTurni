package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.payloads.NewUtenteTurnoDTO;
import yurilenzi.AppTurni.repositories.UtenteTurnoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UtenteTurnoService {
    @Autowired
    UtenteTurnoRepository utenteTurnoRepository;
    @Autowired
    UtenteService utenteService;
    @Autowired
    TurnoService turnoService;

    public UtenteTurno salvaUtenteTurno(NewUtenteTurnoDTO body){
        Utente foundUtente = utenteService.findByEmail(body.emailDipendente());
        Turno foundTurno = turnoService.findByNomeTurno(body.nomeTurno());
        if(utenteTurnoRepository.existsByUtente(foundUtente) && utenteTurnoRepository.existsByTurno(foundTurno) && utenteTurnoRepository.existsBygiornoTurno(LocalDate.parse(body.dataTurno())))
            throw new BadRequestException("Il giorno " + body.dataTurno() + " è già stato assegnato il turno " + body.nomeTurno() + " al dipendete " + foundUtente.getNome() + " " + foundUtente.getCognome());
        return utenteTurnoRepository.save(new UtenteTurno(LocalDate.parse(body.dataTurno()), foundUtente, foundTurno));
    }

    public List<UtenteTurno> vediTurniUtente(Utente utente){
        return utenteTurnoRepository.findByUtente(utente);
    }

    public UtenteTurno trovaTurno(Utente utente, Turno turno, LocalDate giornoTurno){
        return utenteTurnoRepository.findByUtenteAndTurnoAndGiornoTurno(utente, turno, giornoTurno).getFirst();
    }
    public UtenteTurno findByid(Long idUtenteTurno){
        UtenteTurno found = null;
        found = utenteTurnoRepository.findById(idUtenteTurno).orElseThrow(()-> new NotFoundException("Utente turno non trovato"));
        return found;
    }
}

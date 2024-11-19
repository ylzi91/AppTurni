package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewUtenteTurnoDTO;
import yurilenzi.AppTurni.repositories.UtenteTurnoRepository;

import java.time.LocalDate;

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
}

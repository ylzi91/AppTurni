package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.EmptyArrayException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.payloads.NewUtenteTurnoDTO;
import yurilenzi.AppTurni.repositories.UtenteTurnoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UtenteTurnoService {
    @Autowired
    UtenteTurnoRepository utenteTurnoRepository;
    @Autowired
    UtenteService utenteService;
    @Autowired
    TurnoService turnoService;
    @Autowired
    FerieService ferieService;

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

    public List<UtenteTurno> saveFerie(Long idFerie, String emailUtente){
        List<UtenteTurno> utenteTurnoList = new ArrayList<>();
        Utente foundUtente = utenteService.findByEmail(emailUtente);
        Turno takeTurnoFerie = turnoService.checkAndSaveTurnoFerie();
        List<LocalDate> dateList = ferieService.aggiornaTurniConFerie(idFerie);
        dateList.forEach(localDate -> {
            if(utenteTurnoRepository.findByUtenteAndTurnoAndGiornoTurno(foundUtente, takeTurnoFerie, localDate).isEmpty())
                utenteTurnoList.add(utenteTurnoRepository.save(new UtenteTurno(localDate, foundUtente, takeTurnoFerie)));
        });
        if(utenteTurnoList.isEmpty())
            throw new EmptyArrayException("Hai già dato le ferie in questi giorni");
        return utenteTurnoList;
    }
}

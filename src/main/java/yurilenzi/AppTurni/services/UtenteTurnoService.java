package yurilenzi.AppTurni.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.*;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.EmptyArrayException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.payloads.GenericResponseDTO;
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

    @Transactional
    public List<UtenteTurno> salvaUtenteTurno(List<NewUtenteTurnoDTO> body){
        List<Utente> foundUtenti = new ArrayList<>();
        List<Turno> foundTurni = new ArrayList<>();
        List<UtenteTurno> newUtenteTurni = new ArrayList<>();
        body.forEach(mybody -> {
            foundUtenti.add(utenteService.findByEmail(mybody.emailDipendente()));
            foundTurni.add(turnoService.findByNomeTurno(mybody.nomeTurno()));
            if(mybody.idFromDb() == null){
                if(utenteTurnoRepository.existsByUtenteAndTurnoAndGiornoTurno(foundUtenti.getLast(), foundTurni.getLast(), LocalDate.parse(mybody.dataTurno())))
                    throw new BadRequestException("Il giorno " + mybody.dataTurno() + " è già stato assegnato il turno " + mybody.nomeTurno() + " al dipendete " + foundUtenti.getLast().getNome() + " " + foundUtenti.getLast().getCognome());
                else
                    newUtenteTurni.add(new UtenteTurno(LocalDate.parse(mybody.dataTurno()), foundUtenti.getLast(), foundTurni.getLast()));
            }
             else{
                  this.findByid(mybody.idFromDb());
                  newUtenteTurni.add(new UtenteTurno(mybody.idFromDb(), LocalDate.parse(mybody.dataTurno()), foundUtenti.getLast(), foundTurni.getLast()));

            }

        });
//        List<Utente> foundUtenti = body.stream().map(mybody-> utenteService.findByEmail(mybody.emailDipendente())).toList();
//        List<Turno> foundTurni = body.stream().map(mybody -> turnoService.findByNomeTurno(mybody.nomeTurno())).toList();
//        if(utenteTurnoRepository.existsByUtenteAndTurnoAndGiornoTurno(foundUtente, foundTurno, LocalDate.parse(body.dataTurno())))
//            throw new BadRequestException("Il giorno " + body.dataTurno() + " è già stato assegnato il turno " + body.nomeTurno() + " al dipendete " + foundUtente.getNome() + " " + foundUtente.getCognome());
//        return utenteTurnoRepository.save(new UtenteTurno(LocalDate.parse(body.dataTurno()), foundUtente, foundTurno));
            return utenteTurnoRepository.saveAll(newUtenteTurni);
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

    public GenericResponseDTO saveFerie(Long idFerie, HttpServletRequest request){
        String method = request.getMethod();
        List<UtenteTurno> utenteTurnoList = new ArrayList<>();
        Ferie foundFerie = ferieService.findById(idFerie);
        if(!foundFerie.getStatoRichiesta().equals(StatoRichiesta.APPROVATO_CAPO))
            throw new BadRequestException("Le ferie non sono state approvate");
        Turno takeTurnoFerie = turnoService.checkAndSaveTurnoFerie();
        List<LocalDate> dateList = ferieService.aggiornaTurniConFerie(foundFerie);
        dateList.forEach(localDate -> {
            if(utenteTurnoRepository.findByUtenteAndTurnoAndGiornoTurno(foundFerie.getUtente(), takeTurnoFerie, localDate).isEmpty()){
                if("POST".equalsIgnoreCase(method))
                    utenteTurnoList.add(utenteTurnoRepository.save(new UtenteTurno(localDate, foundFerie.getUtente(), takeTurnoFerie)));
                else if ("GET".equalsIgnoreCase(method)) {
                    utenteTurnoList.add(new UtenteTurno(localDate, foundFerie.getUtente(), takeTurnoFerie));
                }
            }
        });
        if("POST".equalsIgnoreCase(method)){
            if(utenteTurnoList.isEmpty()){
                throw new EmptyArrayException("Hai già dato le ferie in questi giorni");

            }
        }
        if("GET".equalsIgnoreCase(method)){
            if(dateList.size() > utenteTurnoList.size()){
                throw new EmptyArrayException("Mancano turni ferie");
            }
        }
            return new GenericResponseDTO("Aggiornato Corretamente");

    }


    public Double oreTotaliUtente(String email, LocalDate dataInizio, LocalDate datafine){
        Utente foundUtente = utenteService.findByEmail(email);
        if(utenteTurnoRepository.filterByDateAndSum(foundUtente, dataInizio, datafine) == null)
            return (double) 0;
        else
            return utenteTurnoRepository.filterByDateAndSum(foundUtente, dataInizio, datafine);
    }

    public List<UtenteTurno> vediPerSettimana(LocalDate dataInizio){
        LocalDate dataFine = dataInizio.plusDays(6);
        return utenteTurnoRepository.findByGiornoTurnoBetween(dataInizio, dataFine);
    }
    public List<UtenteTurno> vediPerSettimanaEmail(LocalDate dataInizio, String email){
        LocalDate dataFine = dataInizio.plusDays(6);
        return utenteTurnoRepository.findByGiornoTurnoBetweenAndUtenteEmail(dataInizio, dataFine, email);
    }
    public List<UtenteTurno> vediUtentiTurni(){
        return utenteTurnoRepository.findAll();
    }

    public void deleteUtenteTurni(List<NewUtenteTurnoDTO> body){
        List<UtenteTurno> utenteTurnoList = new ArrayList<>();
        body.forEach(mb -> {
            if (mb.idFromDb() != null){
                utenteTurnoList.add(this.findByid(mb.idFromDb()));
            }
        });
        utenteTurnoRepository.deleteAllInBatch(utenteTurnoList);
    }
}

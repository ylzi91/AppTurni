package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Ferie;
import yurilenzi.AppTurni.entities.StatoRichiesta;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.EmptyArrayException;
import yurilenzi.AppTurni.exceptions.ForbiddenRequestException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.payloads.NewFerieDTO;
import yurilenzi.AppTurni.payloads.UpdateStatoRichiestaDTO;
import yurilenzi.AppTurni.repositories.FerieRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FerieService {

    @Autowired
    FerieRepository ferieRepository;

    public Ferie chiediFerie(NewFerieDTO body, Utente currentUser){
        LocalDate dataInizo = LocalDate.parse(body.dataInizio());
        LocalDate dataFine = LocalDate.parse(body.dataFine());
        if(dataInizo.isEqual(dataFine) || dataInizo.isBefore(dataFine)){
            return ferieRepository.save(new Ferie(dataInizo, dataFine, currentUser));
        }
        else
            throw new BadRequestException("La data inizio deve essere prima o uguale della data fine");
    }

    public List<Ferie> vediMieFerie(Utente currentUtente){
        if(ferieRepository.findByUtente(currentUtente).isEmpty())
            throw new EmptyArrayException("Non hai richieste di ferie");
        return ferieRepository.findByUtente(currentUtente);
    }

    public List<Ferie> vediFerie(StatoRichiesta statoRichiesta){
        if(statoRichiesta == null){
            return ferieRepository.findAll();
        }
        if(ferieRepository.findByStatoRichiesta(statoRichiesta).isEmpty())
            throw new EmptyArrayException("Non ci sono ferie da approvare");
        else
            return ferieRepository.findByStatoRichiesta(statoRichiesta);
    }

    public Ferie findById(Long idFerie){
        Ferie found;
        found = ferieRepository.findById(idFerie).orElseThrow(() -> new NotFoundException("Ferie con id " + idFerie + " non trovata"));
        return found;
    }

    public Ferie approvaDisapprovaFerie(UpdateStatoRichiestaDTO body, Long idFerie){
        if(body.statoRichiesta() == StatoRichiesta.APPROVATO_CAPO || body.statoRichiesta() == StatoRichiesta.RIFIUTATO_CAPO){
            Ferie foundFerie = this.findById(idFerie);
            foundFerie.setStatoRichiesta(body.statoRichiesta());
            return ferieRepository.save(foundFerie);
        }
        else
            throw new ForbiddenRequestException("Puoi solo approvare o disapprovare la richiesta");
    }

    public List<LocalDate> aggiornaTurniConFerie(Ferie foundFerie){
        List<LocalDate> dateList = new ArrayList<>();
        for(LocalDate date = foundFerie.getDataInizio();
            !date.isAfter(foundFerie.getDataFine());
            date = date.plusDays(1)){
            dateList.add(date);
        }
        return dateList;
    }

    public void eliminaFerie (Utente currentUtente, Long idFerie){
        Ferie foundFerie = this.findById(idFerie);
        if(foundFerie.getStatoRichiesta().equals(StatoRichiesta.IN_CORSO) && foundFerie.getUtente().getEmail().equals(currentUtente.getEmail())){
            ferieRepository.delete(foundFerie);
        }
        else {
            throw new BadRequestException("La richiesta ha già avuto risposta o non sei autorizzato a eliminare");
        }
    }
}

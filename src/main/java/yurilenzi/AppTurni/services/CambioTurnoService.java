package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.*;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.EmptyArrayException;
import yurilenzi.AppTurni.exceptions.ForbiddenRequestException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.payloads.GenericResponseDTO;
import yurilenzi.AppTurni.payloads.NewCambioTurnoDTO;
import yurilenzi.AppTurni.payloads.UpdateStatoRichiestaDTO;
import yurilenzi.AppTurni.repositories.CambioTurnoRepository;
import yurilenzi.AppTurni.repositories.UtenteTurnoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CambioTurnoService {
    @Autowired
    CambioTurnoRepository cambioTurnoRepository;
    @Autowired
    UtenteTurnoService utenteTurnoService;
    @Autowired
    UtenteService utenteService;
    @Autowired
    TurnoService turnoService;
    @Autowired
    UtenteTurnoRepository utenteTurnoRepository;

    public CambioTurno salvaCambioTurno(NewCambioTurnoDTO body, Utente currentUtente){
        if(currentUtente.getEmail().equals(body.emailRis()))
            throw new BadRequestException("Non puoi chiedere un cambio a te stesso XD");
        Turno foundTurnRich = turnoService.findByNomeTurno(body.nomeTurnoRich());
        Utente foundUtenteRisp = utenteService.findByEmail(body.emailRis());
        Turno foundTurnRisp = turnoService.findByNomeTurno(body.nomeTurnoRis());

        UtenteTurno foundUtenteTurnoRich = utenteTurnoService.trovaTurno(currentUtente, foundTurnRich, LocalDate.parse(body.dataTurnoRich()));
        UtenteTurno foundUtenteTurnoRis = utenteTurnoService.trovaTurno(foundUtenteRisp, foundTurnRisp, LocalDate.parse(body.dataTurnoRis()));

        String nomeTurnoRich = foundUtenteTurnoRich.getTurno().getNomeTurno();
        String nomeTurnoRis = foundUtenteTurnoRis.getTurno().getNomeTurno();
        LocalDate dataTurnoRich = foundUtenteTurnoRich.getGiornoTurno();
        LocalDate dataTurnoRis = foundUtenteTurnoRis.getGiornoTurno();

        if(nomeTurnoRich.equals(nomeTurnoRis) && dataTurnoRich.equals(dataTurnoRis))
            throw new BadRequestException("Stai chiedendo un cambio per lo stesso turno e la stessa data");
        bloccaSeTurnoDuplicato(foundUtenteTurnoRich, foundUtenteTurnoRis);
        bloccaSeTurnoDuplicato(foundUtenteTurnoRis, foundUtenteTurnoRich);
        return cambioTurnoRepository.save(new CambioTurno(LocalDate.now(), foundUtenteTurnoRich, foundUtenteTurnoRis));
    }

    public List<CambioTurno> vediCambiTurniIngresso(Utente currentUtente){
        List<CambioTurno> cambi = cambioTurnoRepository.findByUtenteRispondenteTurno(currentUtente);
        if(cambi.isEmpty())
            throw new EmptyArrayException("Non ci sono cambi turno da approvare");
        return cambi;
    }

    public List<CambioTurno> vediCambiTurniIngressoInCorso(Utente currentUtente){
        List<CambioTurno> cambi = cambioTurnoRepository.findByUtenteRispondenteTurnoAndInCorso(currentUtente, StatoRichiesta.IN_CORSO);
        if(cambi.isEmpty())
            throw new EmptyArrayException("Non ci sono cambi turno da approvare");
        return cambi;
    }
    public List<CambioTurno> vediCambiTurnoUscita(Utente currentUtente){
        List<CambioTurno> cambi = cambioTurnoRepository.findByUtenteRichiedenteTurno(currentUtente);
        if(cambi.isEmpty())
            throw new EmptyArrayException("Non hai effettuato nessuna richiesta di cambio");
        return cambi;
    }

    public CambioTurno findById(Long idCambio){
        CambioTurno found = null;
        found = cambioTurnoRepository.findById(idCambio).orElseThrow(()-> new  NotFoundException("Id cambio non trovato"));
        return found;
    }

    public CambioTurno approvaDisapprovaCambio(Long idCambio, Utente currentUser, UpdateStatoRichiestaDTO body){
        CambioTurno foundCambio = this.findById(idCambio);
        if(currentUser.getEmail().equals(foundCambio.getUtenteRispondenteTurno().getUtente().getEmail())){
            if(body.statoRichiesta() == StatoRichiesta.APPROVATA_RISPONDENTE || body.statoRichiesta() == StatoRichiesta.RIFIUTATA_RISPONDENTE)
                foundCambio.setStatoRichiesta(body.statoRichiesta());
            else
                throw new ForbiddenRequestException("Puoi approvare solo con APPROVATA_RISPONDENTE o RIFIUTATO");
        }

        else
            throw new ForbiddenRequestException("Non hai il permesso di approvare questo turno");
        return cambioTurnoRepository.save(foundCambio);
    }

    public List<CambioTurno> vediTuttiICambiAccettati(){
        List<CambioTurno> cambi = cambioTurnoRepository.findByStatoRichiestaOrStatoRichiestaOrStatoRichiesta(
                StatoRichiesta.APPROVATA_RISPONDENTE,
                StatoRichiesta.APPROVATO_CAPO,
                StatoRichiesta.RIFIUTATO_CAPO);
        cambi.stream()
                .filter(cambio -> cambio.getStatoRichiesta() == StatoRichiesta.APPROVATA_RISPONDENTE)
                .forEach(cambio -> cambio.setAvvisi(calcolaAvvisiDoppioTurno(cambio)));
        return cambi;
    }

    // Lo stesso turno due volte nello stesso giorno non è mai ammesso (come in salvaUtenteTurno)
    private void bloccaSeTurnoDuplicato(UtenteTurno cede, UtenteTurno riceve){
        boolean duplicato = utenteTurnoRepository
                .findByUtenteAndGiornoTurno(cede.getUtente(), riceve.getGiornoTurno())
                .stream()
                .filter(ut -> ut.getId() != cede.getId())
                .anyMatch(ut -> ut.getTurno().getNomeTurno().equals(riceve.getTurno().getNomeTurno()));
        if(duplicato)
            throw new BadRequestException(cede.getUtente().getNome() + " " + cede.getUtente().getCognome()
                    + " ha già il turno " + riceve.getTurno().getNomeTurno() + " il " + riceve.getGiornoTurno());
    }

    // Il cambio resta possibile, ma il capo viene avvisato se uno dei due finirebbe con più turni nello stesso giorno
    private List<String> calcolaAvvisiDoppioTurno(CambioTurno cambio){
        List<String> avvisi = new ArrayList<>();
        UtenteTurno rich = cambio.getUtenteRichiedenteTurno();
        UtenteTurno ris = cambio.getUtenteRispondenteTurno();
        avvisoSeDoppio(rich, ris).ifPresent(avvisi::add);
        avvisoSeDoppio(ris, rich).ifPresent(avvisi::add);
        return avvisi;
    }

    // Controlla cosa succede a chi cede "cede" e riceve il turno "riceve"
    private Optional<String> avvisoSeDoppio(UtenteTurno cede, UtenteTurno riceve){
        if(cede.getGiornoTurno().equals(riceve.getGiornoTurno()))
            return Optional.empty(); // stesso giorno: nessun turno in più
        List<String> altriTurni = utenteTurnoRepository
                .findByUtenteAndGiornoTurno(cede.getUtente(), riceve.getGiornoTurno())
                .stream()
                .filter(ut -> ut.getId() != cede.getId())
                .map(ut -> ut.getTurno().getNomeTurno())
                .toList();
        if(altriTurni.isEmpty())
            return Optional.empty();
        return Optional.of(cede.getUtente().getNome() + " " + cede.getUtente().getCognome()
                + " avrebbe più turni il " + riceve.getGiornoTurno() + " (già " + String.join(", ", altriTurni)
                + ", in arrivo " + riceve.getTurno().getNomeTurno() + ")");
    }

    @Transactional
    public CambioTurno approvaDisapprovaCambioCapo(Long idCambio, UpdateStatoRichiestaDTO body){
        CambioTurno foundCambioTurno = this.findById(idCambio);
        if(foundCambioTurno.getStatoRichiesta() != StatoRichiesta.APPROVATA_RISPONDENTE)
            throw new ForbiddenRequestException("Non puoi accedere a questa richiesta al momento");
        if(body.statoRichiesta() != StatoRichiesta.APPROVATO_CAPO && body.statoRichiesta() != StatoRichiesta.RIFIUTATO_CAPO)
            throw new ForbiddenRequestException("Puoi solo approvare o rifiutare la richiesta");

        if(body.statoRichiesta() == StatoRichiesta.APPROVATO_CAPO){
            bloccaSeTurnoDuplicato(foundCambioTurno.getUtenteRichiedenteTurno(), foundCambioTurno.getUtenteRispondenteTurno());
            bloccaSeTurnoDuplicato(foundCambioTurno.getUtenteRispondenteTurno(), foundCambioTurno.getUtenteRichiedenteTurno());
            UtenteTurno utenteTurnoRich = utenteTurnoService.findByid(foundCambioTurno.getUtenteRichiedenteTurno().getId());
            UtenteTurno utenteTurnoRis = utenteTurnoService.findByid(foundCambioTurno.getUtenteRispondenteTurno().getId());

            LocalDate giornoAppoggio = utenteTurnoRich.getGiornoTurno();
            Turno turnoAppoggio = utenteTurnoRich.getTurno();

            utenteTurnoRich.setGiornoTurno(utenteTurnoRis.getGiornoTurno());
            utenteTurnoRich.setTurno(utenteTurnoRis.getTurno());
            utenteTurnoRis.setGiornoTurno(giornoAppoggio);
            utenteTurnoRis.setTurno(turnoAppoggio);

            utenteTurnoRepository.save(utenteTurnoRich);
            utenteTurnoRepository.save(utenteTurnoRis);
        }

        foundCambioTurno.setStatoRichiesta(body.statoRichiesta());
        return cambioTurnoRepository.save(foundCambioTurno);
    }

}

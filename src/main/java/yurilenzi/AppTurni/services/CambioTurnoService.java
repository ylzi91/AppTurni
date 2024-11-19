package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

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
        return cambioTurnoRepository.save(new CambioTurno(LocalDate.now(), foundUtenteTurnoRich, foundUtenteTurnoRis));
    }

    public List<CambioTurno> vediCambiTurniIngresso(Utente currentUtente){
        if(cambioTurnoRepository.findByUtenteRispondenteTurno(currentUtente).isEmpty())
            throw new EmptyArrayException("Non ci sono cambi turno da approvare");
        return cambioTurnoRepository.findByUtenteRispondenteTurno(currentUtente);
    }
    public List<CambioTurno> vediCambiTurnoUscita(Utente currentUtente){
        if(cambioTurnoRepository.findByUtenteRichiedenteTurno(currentUtente).isEmpty())
            throw new EmptyArrayException("Non hai effettuato nessuna richiesta di cambio");
        return cambioTurnoRepository.findByUtenteRichiedenteTurno(currentUtente);
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
        return cambioTurnoRepository.findByStatoRichiestaOrStatoRichiestaOrStatoRichiesta(
                StatoRichiesta.APPROVATA_RISPONDENTE,
                StatoRichiesta.APPROVATO_CAPO,
                StatoRichiesta.RIFIUTATO_CAPO);
    }

    public CambioTurno approvaDisapprovaCambioCapo(Long idCambio, UpdateStatoRichiestaDTO body){
        CambioTurno foundCambioTurno = this.findById(idCambio);
        if(foundCambioTurno.getStatoRichiesta() == StatoRichiesta.APPROVATA_RISPONDENTE){
            if(body.statoRichiesta() == StatoRichiesta.APPROVATO_CAPO || body.statoRichiesta() == StatoRichiesta.RIFIUTATO_CAPO)
                if(body.statoRichiesta() == StatoRichiesta.APPROVATO_CAPO ){

                    UtenteTurno utenteTurnoRichSwitchRis = utenteTurnoService.findByid(foundCambioTurno.getUtenteRichiedenteTurno().getId());
                    UtenteTurno utenteTurnoRisSwitchRich = utenteTurnoService.findByid(foundCambioTurno.getUtenteRispondenteTurno().getId());
                    UtenteTurno utenteAppoggio = new UtenteTurno();

                    utenteAppoggio.setGiornoTurno(utenteTurnoRichSwitchRis.getGiornoTurno());
                    utenteAppoggio.setTurno(utenteTurnoRichSwitchRis.getTurno());

                    utenteTurnoRichSwitchRis.setGiornoTurno(utenteTurnoRisSwitchRich.getGiornoTurno());
                    utenteTurnoRisSwitchRich.setGiornoTurno(utenteAppoggio.getGiornoTurno());

                    utenteTurnoRichSwitchRis.setTurno(utenteTurnoRisSwitchRich.getTurno());
                    utenteTurnoRisSwitchRich.setTurno(utenteAppoggio.getTurno());

                    utenteTurnoRepository.save(utenteTurnoRichSwitchRis);
                    utenteTurnoRepository.save(utenteTurnoRisSwitchRich);
                }
        } else {
                throw new ForbiddenRequestException("Non puoi accedere a questa richiesta al momento");
            }

        foundCambioTurno.setStatoRichiesta(body.statoRichiesta());
        cambioTurnoRepository.save(foundCambioTurno);
        return foundCambioTurno;

    }


}

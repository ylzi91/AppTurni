package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.exceptions.EmptyArrayException;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.exceptions.SameException;
import yurilenzi.AppTurni.payloads.NewTurnoDTO;
import yurilenzi.AppTurni.payloads.UpdateTurnoDTO;
import yurilenzi.AppTurni.repositories.TurnoRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;


    public Turno creaTurno(NewTurnoDTO body){
        String nomeTurno = body.nomeTurno().toUpperCase();
        if(turnoRepository.findById(nomeTurno).isPresent())
            throw new SameException("Il turno con nome " + nomeTurno + " è già presente");
        if(body.oraFine() == null || body.oraInizio() == null){
            return turnoRepository.save(new Turno(nomeTurno));
        }
        else {
            LocalTime oraInizio = LocalTime.parse(body.oraInizio());
            LocalTime oraFine = LocalTime.parse(body.oraFine());
            if(Boolean.TRUE.equals(body.isAfterMidnight())){
                LocalTime midnight = LocalTime.MIDNIGHT;
                return turnoRepository.save(new Turno(nomeTurno, oraInizio, oraFine, midnight));
            }
            if(oraInizio.isBefore(oraFine))
                return turnoRepository.save(new Turno(nomeTurno, oraInizio, oraFine));
            else
                throw new BadRequestException("L'ora fine deve essere successiva a l'ora di inizio");
        }
    }

    public Turno findByNomeTurno(String nomeTurno){
        Turno found = null;
        found = turnoRepository.findById(nomeTurno).orElseThrow(()-> new NotFoundException(nomeTurno + " non è stato trovato"));
        return found;
    }

    public Turno checkAndSaveTurnoFerie(){
        return turnoRepository.findById("F")
                .orElseGet(() -> turnoRepository.save(new Turno("F")));
    }

    public List<Turno> vediTurni(){
        List<Turno> turni = turnoRepository.findAll();
        if(turni.isEmpty())
            throw new EmptyArrayException("Non ci sono turni");
        return turni;
    }


    public Turno modificaOrarioTurno(UpdateTurnoDTO body, String nomeTurno){
        Turno foundTurno = this.findByNomeTurno(nomeTurno);
        foundTurno.setOraInizio(LocalTime.parse(body.oraInizio()));
        foundTurno.setOraFine(LocalTime.parse(body.oraFine()));
        foundTurno.setDurataTurno(LocalTime.parse(body.oraInizio()), LocalTime.parse(body.oraFine()));
        return turnoRepository.save(foundTurno);
    }

    public void eliminaTurno(String nomeTurno){
        Turno found = findByNomeTurno(nomeTurno);
        turnoRepository.delete(found);
    }

}

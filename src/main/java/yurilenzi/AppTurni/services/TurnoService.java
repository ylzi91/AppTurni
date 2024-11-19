package yurilenzi.AppTurni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.exceptions.NotFoundException;
import yurilenzi.AppTurni.exceptions.SameException;
import yurilenzi.AppTurni.payloads.NewTurnoDTO;
import yurilenzi.AppTurni.repositories.TurnoRepository;

import java.time.LocalTime;

@Service
public class TurnoService {
    @Autowired
    private TurnoRepository turnoRepository;


    public Turno creaTurno(NewTurnoDTO body){
        if(turnoRepository.findById(body.nomeTurno()).isPresent())
            throw new SameException("Il turno con nome " + body.nomeTurno() + " è già presente");
        if(body.oraFine() == null || body.oraInizio() == null){
            return turnoRepository.save(new Turno(body.nomeTurno()));
        }
        else {
            return turnoRepository.save(new Turno(body.nomeTurno(), LocalTime.parse(body.oraInizio()), LocalTime.parse(body.oraFine())));
        }
    }

    public Turno findByNomeTurno(String nomeTurno){
        Turno found = null;
        found = turnoRepository.findById(nomeTurno).orElseThrow(()-> new NotFoundException(nomeTurno + " non è stato trovato"));
        return found;
    }

}

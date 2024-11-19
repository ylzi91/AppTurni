package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;

import java.time.LocalDate;

@Repository
public interface UtenteTurnoRepository extends JpaRepository <UtenteTurno, Long> {
    boolean existsByUtente(Utente utente);
    boolean existsByTurno(Turno turno);
    boolean existsBygiornoTurno(LocalDate dataTurno);
}

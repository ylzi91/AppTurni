package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UtenteTurnoRepository extends JpaRepository <UtenteTurno, Long> {
    boolean existsByUtente(Utente utente);
    boolean existsByTurno(Turno turno);
    boolean existsBygiornoTurno(LocalDate dataTurno);
    boolean existsByUtenteAndTurnoAndGiornoTurno(Utente utente, Turno turno, LocalDate dataTurno);
    List<UtenteTurno> findByUtente(Utente utente);

    List<UtenteTurno> findByUtenteAndTurnoAndGiornoTurno(Utente utente, Turno turno, LocalDate giornoTurno);

    @Query("select sum(ut.turno.durataTurno) from UtenteTurno ut where ut.giornoTurno >= :dataInizio and ut.giornoTurno <= :dataFine and ut.utente = :utente ")
    Double filterByDateAndSum(
            @Param("utente") Utente utente,
            @Param("dataInizio") LocalDate dataInizio,
            @Param("dataFine") LocalDate dataFine);

    List<UtenteTurno> findByGiornoTurnoBetween(LocalDate dataInizio, LocalDate dataFine);

    List<UtenteTurno> findByGiornoTurnoBetweenAndUtenteEmail(LocalDate dataInizio, LocalDate dataFine, String email);
}

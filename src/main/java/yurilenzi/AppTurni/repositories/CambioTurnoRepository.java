package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yurilenzi.AppTurni.entities.CambioTurno;
import yurilenzi.AppTurni.entities.StatoRichiesta;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;

import java.util.List;

public interface CambioTurnoRepository extends JpaRepository <CambioTurno, Long> {

    @Query("select ct from CambioTurno ct where ct.utenteRispondenteTurno.utente = :utente")
    List<CambioTurno> findByUtenteRispondenteTurno(Utente utente);
    @Query("select ct from CambioTurno ct where ct.utenteRichiedenteTurno.utente = :utente")
    List<CambioTurno> findByUtenteRichiedenteTurno(Utente utente);

    List<CambioTurno> findByStatoRichiestaOrStatoRichiestaOrStatoRichiesta(StatoRichiesta statoRichiesta, StatoRichiesta statoRichiesta2, StatoRichiesta statoRichiesta3);
}

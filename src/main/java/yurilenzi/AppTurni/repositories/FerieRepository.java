package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Ferie;
import yurilenzi.AppTurni.entities.StatoRichiesta;
import yurilenzi.AppTurni.entities.Utente;

import java.util.List;

@Repository
public interface FerieRepository extends JpaRepository<Ferie, Long> {

    List<Ferie> findByUtente(Utente currentUtente);
    List<Ferie> findByStatoRichiesta(StatoRichiesta statoRichiesta);

}

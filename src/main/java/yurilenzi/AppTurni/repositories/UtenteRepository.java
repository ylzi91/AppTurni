package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Role;
import yurilenzi.AppTurni.entities.Utente;

import java.util.List;

@Repository
public interface UtenteRepository extends JpaRepository <Utente, String> {
    boolean existsByRole(Role role);
    List<Utente> findByRole(Role role);
}

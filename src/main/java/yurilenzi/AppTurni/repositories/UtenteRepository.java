package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import yurilenzi.AppTurni.entities.Role;
import yurilenzi.AppTurni.entities.Utente;

public interface UtenteRepository extends JpaRepository <Utente, String> {
    boolean existsByRole(Role role);
}

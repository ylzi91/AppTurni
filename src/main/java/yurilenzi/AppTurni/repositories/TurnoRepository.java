package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Turno;

@Repository
public interface TurnoRepository extends JpaRepository <Turno, String> {
}

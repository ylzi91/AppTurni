package yurilenzi.AppTurni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import yurilenzi.AppTurni.entities.Turno;

@Repository
public interface TurnoRepository extends JpaRepository <Turno, String> {



}

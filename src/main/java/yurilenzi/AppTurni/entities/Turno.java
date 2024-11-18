package yurilenzi.AppTurni.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;


@Entity
@Table(name = "turni")
@Getter
@Setter
@NoArgsConstructor
public class Turno {
    @Id
    private String nomeTurno;
    private LocalTime oraInizio, oraFine;

    public Turno(String nomeTurno, LocalTime oraInizio, LocalTime oraFine) {
        this.nomeTurno = nomeTurno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
    }
}

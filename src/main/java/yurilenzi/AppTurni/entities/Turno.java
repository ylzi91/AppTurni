package yurilenzi.AppTurni.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


@Entity
@Table(name = "turni")
@Getter
@Setter
@NoArgsConstructor
public class Turno {
    @Id
    private String nomeTurno;
    private LocalTime oraInizio, oraFine;
    private double durataTurno;

    public Turno(String nomeTurno, LocalTime oraInizio, LocalTime oraFine) {
        this.nomeTurno = nomeTurno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        long minutes = oraInizio.until(oraFine, ChronoUnit.MINUTES);
        this.durataTurno = (double) minutes / 60;
    }
    public Turno(String nomeTurno){
        this.nomeTurno = nomeTurno;
        this.durataTurno = 0;
    }
}

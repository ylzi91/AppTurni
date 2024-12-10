package yurilenzi.AppTurni.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


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

    @OneToMany(mappedBy = "turno", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<UtenteTurno> utenteTurnoList;

    public Turno(String nomeTurno, LocalTime oraInizio, LocalTime oraFine) {
        this.nomeTurno = nomeTurno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        long minutes = oraInizio.until(oraFine, ChronoUnit.MINUTES);
        this.durataTurno = (double) minutes / 60;
    }
    public Turno(String nomeTurno, LocalTime oraInizio, LocalTime oraFine, LocalTime midnight) {
        this.nomeTurno = nomeTurno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        long minutesMidnight = oraInizio.until(midnight.minusSeconds(1), ChronoUnit.MINUTES);
        long minutesMattino = midnight.until(oraFine, ChronoUnit.MINUTES);
        this.durataTurno = Math.ceil((double) (minutesMidnight + minutesMattino) / 60);
    }
    public Turno(String nomeTurno){
        this.nomeTurno = nomeTurno;
        this.durataTurno = 0;
    }

    public void setDurataTurno(LocalTime oraInizio, LocalTime oraFine) {
        long minutes = oraInizio.until(oraFine, ChronoUnit.MINUTES);
        this.durataTurno = (double) minutes / 60;
    }
}

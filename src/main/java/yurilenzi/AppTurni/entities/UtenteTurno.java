package yurilenzi.AppTurni.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "utenti_turni")
@Getter
@Setter
@NoArgsConstructor
public class UtenteTurno {
    @Id
    @GeneratedValue
    private long id;
    private LocalDate giornoTurno;
    @ManyToOne
    private Utente utente;
    @ManyToOne
    private Turno turno;

    public UtenteTurno(LocalDate giornoTurno, Utente utente, Turno turno) {
        this.giornoTurno = giornoTurno;
        this.utente = utente;
        this.turno = turno;
    }
}

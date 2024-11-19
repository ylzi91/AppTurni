package yurilenzi.AppTurni.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ferie {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private LocalDate dataInizio, dataFine;
    @Enumerated(EnumType.STRING)
    private StatoRichiesta statoRichiesta;
    @ManyToOne
    private Utente utente;

    public Ferie(LocalDate dataInizio, LocalDate dataFine, Utente utente) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.statoRichiesta = StatoRichiesta.IN_CORSO;
        this.utente = utente;
    }
}

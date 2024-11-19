package yurilenzi.AppTurni.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table (name = "cambi_turni")
@Getter
@Setter
@NoArgsConstructor
public class CambioTurno {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private LocalDate dataRichiesta;
    @Enumerated(EnumType.STRING)
    private StatoRichiesta statoRichiesta;
    @OneToOne
    private UtenteTurno utenteRichiedente;
    @OneToOne
    private UtenteTurno utenteRispondente;

    public CambioTurno(LocalDate dataRichiesta, UtenteTurno utenteRichiedente, UtenteTurno utenteRispondente) {
        this.dataRichiesta = dataRichiesta;
        this.statoRichiesta = StatoRichiesta.IN_CORSO;
        this.utenteRichiedente = utenteRichiedente;
        this.utenteRispondente = utenteRispondente;
    }
}

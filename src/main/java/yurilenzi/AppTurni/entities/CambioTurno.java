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
    private UtenteTurno utenteRichiedenteTurno;
    @OneToOne
    private UtenteTurno utenteRispondenteTurno;

    public CambioTurno(LocalDate dataRichiesta, UtenteTurno utenteRichiedenteTurno, UtenteTurno utenteRispondenteTurno) {
        this.dataRichiesta = dataRichiesta;
        this.statoRichiesta = StatoRichiesta.IN_CORSO;
        this.utenteRichiedenteTurno = utenteRichiedenteTurno;
        this.utenteRispondenteTurno = utenteRispondenteTurno;
    }
}

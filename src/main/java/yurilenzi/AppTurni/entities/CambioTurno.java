package yurilenzi.AppTurni.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne
    private UtenteTurno utenteRichiedenteTurno;
    @ManyToOne
    private UtenteTurno utenteRispondenteTurno;
    // Non salvato a database: avvisi per il capo calcolati al momento della lettura
    @Transient
    private List<String> avvisi = new ArrayList<>();

    public CambioTurno(LocalDate dataRichiesta, UtenteTurno utenteRichiedenteTurno, UtenteTurno utenteRispondenteTurno) {
        this.dataRichiesta = dataRichiesta;
        this.statoRichiesta = StatoRichiesta.IN_CORSO;
        this.utenteRichiedenteTurno = utenteRichiedenteTurno;
        this.utenteRispondenteTurno = utenteRispondenteTurno;
    }
}

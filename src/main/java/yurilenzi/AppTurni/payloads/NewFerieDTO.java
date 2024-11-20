package yurilenzi.AppTurni.payloads;

import java.time.LocalDate;

public record NewFerieDTO(
        String dataInizio,
        String dataFine
) {
}

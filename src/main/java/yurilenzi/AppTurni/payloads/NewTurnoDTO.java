package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record NewTurnoDTO(
        @NotEmpty(message = "Il nome turno non può essere vuoto")
        String nomeTurno,
        String oraInizio,
        String oraFine
) {
}

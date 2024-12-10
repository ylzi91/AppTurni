package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UpdateTurnoDTO(
        @NotEmpty(message = "Ora inizio è obbligatoria")
        String oraInizio,
        @NotEmpty(message = "Ora fine è obbligatoria")
        String oraFine
) {
}

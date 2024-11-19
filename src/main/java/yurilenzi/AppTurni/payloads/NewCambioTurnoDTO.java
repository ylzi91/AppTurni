package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewCambioTurnoDTO(
        @NotEmpty(message = "nomeTurnoRich non può essere vuoto")
        String nomeTurnoRich,
        @NotEmpty(message = "dataTurnoRich non può essere vuoto")
        String dataTurnoRich,
        @NotEmpty(message = "emailRis non può essere vuoto")
        String emailRis,
        @NotEmpty(message = "nomeTurnoRis non può essere vuoto")
        String nomeTurnoRis,
        @NotEmpty(message = "dataTurnoRis non può essere vuoto")
        String dataTurnoRis
) {
}

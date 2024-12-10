package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.NotEmpty;

public record NewUtenteTurnoDTO(
        @NotEmpty(message = "Il campo data non può essere vuoto")
        String dataTurno,
        @NotEmpty(message = "Il campo email non può essere vuoto")
        String emailDipendente,
        @NotEmpty(message = "Il campo nome turno non può essere vuoto")
        String nomeTurno,
        Long idFromDb
) {
}

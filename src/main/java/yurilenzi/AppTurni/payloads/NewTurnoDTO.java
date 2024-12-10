package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record NewTurnoDTO(
        @NotEmpty(message = "Il nome turno non può essere vuoto")
        @Size(min = 1, max = 2, message = "Il nome può essere al massimo di uno o due caratteri")
        String nomeTurno,
        String oraInizio,
        String oraFine,
        Boolean isAfterMidnight
) {
        public NewTurnoDTO(
                String nomeTurno,
                String oraInizio,
                String oraFine
        ) {
                this(nomeTurno, oraInizio, oraFine,false);
        }
}

package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewUtenteDTO(
        @NotEmpty(message = "Email obbligatoria")
        @Email(message = "Email non valida")
        String email,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3 ,max = 20, message = "Il nome deve essere compreso fra 3 e 20 caratteri")
        String nome,
        @NotEmpty(message = "Cognome obbligatorio")
        @Size(min = 3 ,max = 20, message = "Il cognome deve essere compreso fra 3 e 20 caratteri")
        String cognome,
        @NotEmpty(message = "Password obbligatoria")
        String password
) {
}

package yurilenzi.AppTurni.payloads;

import jakarta.validation.constraints.NotEmpty;

public record UpadatePassword(@NotEmpty(message = "Vecchia password obbligatoria")
                              String oldPassword,
                              @NotEmpty(message = "Nuova password obbligatoria")
                              String newPassword) {
}

package yurilenzi.AppTurni.payloads;

import yurilenzi.AppTurni.entities.Role;

public record LoginResponseDTO(String accessToken, Role role) {
}

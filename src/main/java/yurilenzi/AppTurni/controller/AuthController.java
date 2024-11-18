package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.payloads.LoginDTO;
import yurilenzi.AppTurni.payloads.LoginResponseDTO;
import yurilenzi.AppTurni.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO loginUtente(@RequestBody LoginDTO body){
        return authService.checkCredentialAndResponseToken(body);
    }
}

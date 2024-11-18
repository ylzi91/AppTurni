package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewUtenteDTO;
import yurilenzi.AppTurni.services.UtenteService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    UtenteService utenteService;

    @PostMapping("/utente")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public Utente saveNewUtenteBase(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
       return utenteService.saveUtenteBase(body);
    }
    @PostMapping("/capo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Utente saveNewUtenteCapo(@RequestBody @Validated NewUtenteDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        return utenteService.saveUtenteCapo(body);
    }

}

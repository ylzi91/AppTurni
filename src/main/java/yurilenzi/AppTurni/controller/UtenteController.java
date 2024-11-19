package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewUtenteDTO;
import yurilenzi.AppTurni.services.UtenteService;
import yurilenzi.AppTurni.services.UtenteTurnoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    @Autowired
    UtenteService utenteService;

    @Autowired
    UtenteTurnoService utenteTurnoService;

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

    @GetMapping("/me/turni")
    public List<UtenteTurno> vediMieiTurni(@AuthenticationPrincipal Utente currentUtente){
        return utenteTurnoService.vediTurniUtente(currentUtente);
    }



}

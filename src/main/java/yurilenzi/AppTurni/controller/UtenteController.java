package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.GenericResponseDTO;
import yurilenzi.AppTurni.payloads.NewUtenteDTO;
import yurilenzi.AppTurni.payloads.UpadatePassword;
import yurilenzi.AppTurni.payloads.UpdateUtenteDTO;
import yurilenzi.AppTurni.services.UtenteService;
import yurilenzi.AppTurni.services.UtenteTurnoService;

import java.io.File;
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

    @GetMapping
    public List<Utente> getAllUtenti(){
        return utenteService.findAllUtenti();
    }

    @GetMapping("/me")
    public Utente utenteLoggato(@AuthenticationPrincipal Utente currentUtente){
        return utenteService.findByEmail(currentUtente.getEmail());
    }

    @GetMapping("/{email}")
    public Utente findByEmail(@PathVariable String email){
        return utenteService.findByEmail(email);
    }

    @PatchMapping("/me/avatar")
    @ResponseStatus(HttpStatus.OK)
    public GenericResponseDTO uploadAvatar(@AuthenticationPrincipal Utente currentAuthenticatedUser,
                                           @RequestParam("avatar") MultipartFile file) {
        return utenteService.uploadAvatar(currentAuthenticatedUser, file);
    }

    @PutMapping("/me/nome_cognome")
    public Utente updateNomeCognome(@AuthenticationPrincipal Utente currentUtente, @RequestBody @Validated UpdateUtenteDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        return utenteService.updateNomeCognome(currentUtente, body);
    }
    @PatchMapping("/me/password")
    public void updatePassword(@AuthenticationPrincipal Utente currentUtente, @RequestBody UpadatePassword body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        utenteService.updatePassword(currentUtente, body);
    }



}

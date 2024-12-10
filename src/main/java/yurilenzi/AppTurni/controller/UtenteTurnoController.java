package yurilenzi.AppTurni.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import yurilenzi.AppTurni.payloads.GenericResponseDTO;
import yurilenzi.AppTurni.payloads.NewResponseSumDTO;
import yurilenzi.AppTurni.payloads.NewUtenteTurnoDTO;
import yurilenzi.AppTurni.services.TurnoService;
import yurilenzi.AppTurni.services.UtenteTurnoService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utenteturno")
public class UtenteTurnoController {
    @Autowired
    UtenteTurnoService utenteTurnoService;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public List<UtenteTurno> aggiungiNuovoTurno(@RequestBody @Validated List<NewUtenteTurnoDTO> body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult
                    .getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        return utenteTurnoService.salvaUtenteTurno(body);
    }
    @PostMapping("/{idFerie}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public GenericResponseDTO saveFerie(@PathVariable Long idFerie, HttpServletRequest request){
        return utenteTurnoService.saveFerie(idFerie, request);
    }

    @GetMapping("/{idFerie}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public GenericResponseDTO verificaFerie(@PathVariable Long idFerie, HttpServletRequest request){
        return utenteTurnoService.saveFerie(idFerie, request);
    }

    @GetMapping("/ore_totali_utente")
    public NewResponseSumDTO oreTotali(
            @RequestParam String email,
            @RequestParam LocalDate dataInizio,
            @RequestParam LocalDate dataFine
            ){
        return new NewResponseSumDTO(utenteTurnoService.oreTotaliUtente(email, dataInizio, dataFine));
    }

    @GetMapping("/mie_ore_totali")
    public NewResponseSumDTO oreMieTotali(
            @RequestParam LocalDate dataInizio,
            @RequestParam LocalDate dataFine,
            @AuthenticationPrincipal Utente currentUtente
    ){
        return new NewResponseSumDTO((utenteTurnoService.oreTotaliUtente(currentUtente.getEmail(), dataInizio,dataFine)));
    }

    @GetMapping("/settimanali")
    public List<UtenteTurno> turniSettiamanli(
            @RequestParam String dataInizio,
            @RequestParam(required = false) String email
    ){
        if(email == null)
            return utenteTurnoService.vediPerSettimana(LocalDate.parse(dataInizio));
        else
            return utenteTurnoService.vediPerSettimanaEmail(LocalDate.parse(dataInizio), email);
    }

    @GetMapping
    public List<UtenteTurno> vediTuttiITurni(){
        return utenteTurnoService.vediUtentiTurni();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public void eliminaUtenteTurni(@RequestBody List<NewUtenteTurnoDTO> body){
        utenteTurnoService.deleteUtenteTurni(body);
    }

}

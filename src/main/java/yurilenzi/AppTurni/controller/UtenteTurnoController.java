package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.entities.UtenteTurno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewUtenteTurnoDTO;
import yurilenzi.AppTurni.services.TurnoService;
import yurilenzi.AppTurni.services.UtenteTurnoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/utenteturno")
public class UtenteTurnoController {
    @Autowired
    UtenteTurnoService utenteTurnoService;


    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public UtenteTurno aggiungiNuovoTurno(@RequestBody @Validated NewUtenteTurnoDTO body, BindingResult validationResult){
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
    @PostMapping("/{email}/{idFerie}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public List<UtenteTurno> saveFerie(@PathVariable String email, @PathVariable Long idFerie){
        return utenteTurnoService.saveFerie(idFerie, email);
    }
}

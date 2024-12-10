package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewTurnoDTO;
import yurilenzi.AppTurni.payloads.UpdateTurnoDTO;
import yurilenzi.AppTurni.services.TurnoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/turni")
public class TurnoController {

    @Autowired
    TurnoService turnoService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public Turno creaTurno(@RequestBody @Validated NewTurnoDTO body, BindingResult validationResult){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        return turnoService.creaTurno(body);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public List<Turno> vediTurni(){
        return turnoService.vediTurni();
    }

    @PatchMapping("/{nomeTurno}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public Turno modificaTurno(@RequestBody @Validated UpdateTurnoDTO body, BindingResult validationResult, @PathVariable String nomeTurno){
        if(validationResult.hasErrors()){
            String message = validationResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(". "));
            throw new BadRequestException(message);
        }
        return turnoService.modificaOrarioTurno(body, nomeTurno.toUpperCase());
    }

    @DeleteMapping("/{nomeTurno}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public void deleteTurno(@PathVariable String nomeTurno){
        turnoService.eliminaTurno(nomeTurno);
    }


}

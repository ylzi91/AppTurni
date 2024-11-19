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
import yurilenzi.AppTurni.entities.Turno;
import yurilenzi.AppTurni.exceptions.BadRequestException;
import yurilenzi.AppTurni.payloads.NewTurnoDTO;
import yurilenzi.AppTurni.services.TurnoService;

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

}

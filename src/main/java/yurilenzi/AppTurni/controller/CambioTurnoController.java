package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.entities.CambioTurno;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.payloads.NewCambioTurnoDTO;
import yurilenzi.AppTurni.payloads.UpdateStatoRichiestaDTO;
import yurilenzi.AppTurni.services.CambioTurnoService;

import java.util.List;

@RestController
@RequestMapping("/cambioturno")
public class CambioTurnoController {
    @Autowired
    private CambioTurnoService cambioTurnoService;

    @PostMapping("/me")
    public CambioTurno richiediCambioTurno(@RequestBody NewCambioTurnoDTO body, @AuthenticationPrincipal Utente currentUtente){
        return cambioTurnoService.salvaCambioTurno(body, currentUtente);
    }

    @GetMapping("/me/ingresso")
    public List<CambioTurno> vediRichiesteInIngresso(@AuthenticationPrincipal Utente currentUtente){
        return cambioTurnoService.vediCambiTurniIngresso(currentUtente);
    }
    @GetMapping("/me/uscita")
    public List<CambioTurno> vediRichiesteInUscita(@AuthenticationPrincipal Utente currentUtente){
        return cambioTurnoService.vediCambiTurnoUscita(currentUtente);
    }
    @PatchMapping("/me/ingresso/{idCambioTurno}")
    public CambioTurno approvaDisapprovaCambio(@PathVariable Long idCambioTurno, @AuthenticationPrincipal Utente currentUtente, @RequestBody UpdateStatoRichiestaDTO body){
        return cambioTurnoService.approvaDisapprovaCambio(idCambioTurno, currentUtente, body);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public List<CambioTurno> vediTuttiICambioTurno(){
        return cambioTurnoService.vediTuttiICambiAccettati();
    }

    @PatchMapping("/{idCambioTurno}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public CambioTurno approvaDisapprovaCambioCapo(@PathVariable Long idCambioTurno, @RequestBody UpdateStatoRichiestaDTO body){
        return cambioTurnoService.approvaDisapprovaCambioCapo(idCambioTurno, body);
    }
}

package yurilenzi.AppTurni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yurilenzi.AppTurni.entities.Ferie;
import yurilenzi.AppTurni.entities.Utente;
import yurilenzi.AppTurni.payloads.NewFerieDTO;
import yurilenzi.AppTurni.payloads.UpdateStatoRichiestaDTO;
import yurilenzi.AppTurni.services.FerieService;

import java.util.List;

@RestController
@RequestMapping("/ferie")
public class FerieController {
    @Autowired
    FerieService ferieService;

    @PostMapping
    public Ferie chiediFerie(@RequestBody NewFerieDTO body, @AuthenticationPrincipal Utente currentUtente){
        return ferieService.chiediFerie(body, currentUtente);
    }

    @PatchMapping("/aod/{idFerie}")
    public Ferie approvaDisapporvaFerie(@RequestBody UpdateStatoRichiestaDTO body, @PathVariable Long idFerie){
        return ferieService.approvaDisapprovaFerie(body, idFerie);
    }

    @GetMapping("/me")
    public List<Ferie> vediMieFerie(@AuthenticationPrincipal Utente currentUtente){
        return ferieService.vediMieFerie(currentUtente);
    }

    @GetMapping("/daapprovare")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CAPO')")
    public List<Ferie> vediTutteLeRichiesteDiFerie(){
        return ferieService.vediFerieDaApprovarre();
    }

}

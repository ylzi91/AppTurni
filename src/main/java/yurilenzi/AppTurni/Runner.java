package yurilenzi.AppTurni;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import yurilenzi.AppTurni.services.UtenteService;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    UtenteService utenteService;

    @Override
    public void run(String... args) throws Exception {
        utenteService.findByRoleAndCreateAdmin();
    }
}

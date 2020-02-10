package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.service.CasopisService;
import com.example.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveraClanarine implements JavaDelegate {

    @Autowired
    private CasopisService casopisService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u ProveraClanarine");

        Integer casopisId = Integer.parseInt((String) delegateExecution.getVariable("izborCasopisId"));
        System.out.println("casopisId: " + casopisId);
        Casopis casopis = casopisService.findOneById(casopisId);

        String autorUsername = (String) delegateExecution.getVariable("autorUsername");
        System.out.println("autorUsername: " + autorUsername);

        boolean autorPlatio = false;

        if(casopis.getKorisniciPlatili().size() != 0) {
            for (Korisnik korisnikPlatio : casopis.getKorisniciPlatili()) {
                if (korisnikPlatio.getUsername().equals(autorUsername)) {
                    autorPlatio = true;
                    break;
                }
            }
        }

        if(autorPlatio){
            delegateExecution.setVariable("clanarina", true);
            System.out.println("autor ima aktivnu clanarinu u ovom casopisu");
        }else{
            delegateExecution.setVariable("clanarina", false);
            System.out.println("autor nema aktivnu clanarinu u ovom casopisu");
        }

        System.out.println("izasao iz ProveraClanarine");

    }
}

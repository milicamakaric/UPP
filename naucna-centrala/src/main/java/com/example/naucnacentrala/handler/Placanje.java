package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.service.CasopisService;
import com.example.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Placanje implements JavaDelegate {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u Placanje");

        String glavniProcesId = (String) delegateExecution.getVariable("glavniProcesId");
        System.out.println("glavniProcesId: " + glavniProcesId);

        Integer casopisId = Integer.parseInt((String) runtimeService.getVariable(glavniProcesId, "izborCasopisId"));
        System.out.println("casopisId: " + casopisId);
        Casopis casopis = casopisService.findOneById(casopisId);

        String autorUsername = (String) runtimeService.getVariable(glavniProcesId, "autorUsername");
        Korisnik autor = korisnikService.findOneByUsername(autorUsername);
        System.out.println("autorUsername: " + autorUsername);

        casopis.getKorisniciPlatili().add(autor);
        casopisService.save(casopis);

        System.out.println("izasao iz Placanje");

    }
}

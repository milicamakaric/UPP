package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Recenzent;
import com.example.naucnacentrala.model.Role;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.service.RoleService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PotvrdaRecenzenta implements JavaDelegate {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RoleService roleService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("Usao u PotvrdaRecenzentaService");

        String username = (String) delegateExecution.getVariable("username");
        System.out.println("username: " + username);

        try{
            Korisnik pronadjen = korisnikService.findOneByUsername(username);

            Role role = roleService.findOneByName("ROLE_RECENZENT");
            pronadjen.getRoles().add(role);
            pronadjen.setRecenzent(Recenzent.ODOBREN);

            korisnikService.save(pronadjen);

            System.out.println("Korisnik uspesno potvrdjen kao recenzent");
        }catch(NullPointerException e){
            System.out.println("Nema ovog korisnika");
        }







    }
}

package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.NaucnaOblast;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrednikNaucneOblasti implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u UrednikNaucneOblasti");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);
        NaucnaOblast radNaucnaOblast = rad.getNaucnaOblast();

        boolean pronadjen = false;

        for(Korisnik urednik: rad.getCasopis().getUrednici()){
            System.out.println("urednik: " + urednik.getUsername());
            for(NaucnaOblast urednikNaucnaOblast: urednik.getNaucneOblasti()){
                if(urednikNaucnaOblast.getId() == radNaucnaOblast.getId()){
                    System.out.println("pronadjen urednik: " + urednik.getUsername());
                    delegateExecution.setVariable("urednik_oblasti", urednik.getUsername());
                    pronadjen = true;
                    break;
                }
            }
            if(pronadjen){
                System.out.println("pronadjen je");
                break;
            }
        }

        if(!pronadjen){
            String glavniUrednikUsername = radService.findOneById(radId).getCasopis().getGlavniUrednik().getUsername();
            System.out.println("nije pronadjen; glavni urednik username: " + glavniUrednikUsername);
            delegateExecution.setVariable("urednik_oblasti", glavniUrednikUsername);
        }

        System.out.println("izasao iz UrednikNaucneOblasti");
    }
}

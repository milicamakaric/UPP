package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KreiranjeListaRecenzenata implements JavaDelegate {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u KreiranjeListaRecenzenata");

        List<FormSubmissionDto> izborRecenzenata = (List<FormSubmissionDto>) delegateExecution.getVariable("izborRecenzenata");
        List<String> izabraniRecenzenti = new ArrayList<>();

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        for(FormSubmissionDto dto: izborRecenzenata){
            if(dto.getFieldId().equals("select_box1")){
                Integer recenzentId = Integer.parseInt(dto.getFieldValue());
                Korisnik recenzent = korisnikService.findOneById(recenzentId);
                System.out.println("izabran recenzent: " + recenzent.getUsername());
                rad.getRecenzenti().add(recenzent);
                izabraniRecenzenti.add(recenzent.getUsername());
            }
        }

        rad = radService.save(rad);
        delegateExecution.setVariable("izabraniRecenzenti", izabraniRecenzenti);

        System.out.println("izasao iz KreiranjeListaRecenzenata");

    }
}

package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.NaucnaOblast;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DobavljanjeRecenzenata implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u DobavljanjeRecenzenata");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        System.out.println("radId: " + radId);
        List<Korisnik> recenzenti = radService.findOneById(radId).getCasopis().getRecenzenti();

        NaucnaOblast radNaucnaOblast = radService.findOneById(radId).getNaucnaOblast();
        List<Korisnik> recenzentiNaucnaOblast = new ArrayList<>();

        for(Korisnik recenzent: recenzenti){
            for(NaucnaOblast recenzentNaucnaOblast: recenzent.getNaucneOblasti()){
                if(radNaucnaOblast.getId() == recenzentNaucnaOblast.getId()){
                    System.out.println("dodat recenzent: " + recenzent.getUsername());
                    recenzentiNaucnaOblast.add(recenzent);
                }
            }
        }

        if(recenzentiNaucnaOblast.size()  < 2){
            System.out.println("postoji manje od 2 recenzenta za casopis: " + radService.findOneById(radId).getCasopis().getNaziv());
//            delegateExecution.setVariableLocal("postoje_recenzentii ", false);
            delegateExecution.setVariable("ima_recenzenata", true);
        }else{
            System.out.println("postoji vise od 2 recenzenta za casopis: " + radService.findOneById(radId).getCasopis().getNaziv());
            delegateExecution.setVariable("recenzenti_lista", recenzentiNaucnaOblast);
            delegateExecution.setVariable("ima_recenzenata", true);
        }

        List<String> odradiliRecenzenti = new ArrayList<>();
        List<String> komentarAutoru = new ArrayList<>();
        List<String> preporuke = new ArrayList<>();
        List<String> komentarUredniku = new ArrayList<>();
        delegateExecution.setVariable("odradiliRecenzenti", odradiliRecenzenti);
        delegateExecution.setVariable("komentarAutoru", komentarAutoru);
        delegateExecution.setVariable("preporuke", preporuke);
        delegateExecution.setVariable("komentarUredniku", komentarUredniku);

        System.out.println("izasao iz DobavljanjeRecenzenata");

    }
}

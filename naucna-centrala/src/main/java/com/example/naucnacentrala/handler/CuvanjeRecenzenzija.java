package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuvanjeRecenzenzija implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjeRecenzenzija");

        String trenutniRecenzent = (String) delegateExecution.getVariable("oneRecenzent");
        System.out.println("trenutni recenzent: " + trenutniRecenzent);

        List<String> odradiliRecenzenti = (List<String>) delegateExecution.getVariable("odradiliRecenzenti");
        System.out.println("odradili recenzenti size pre: " + odradiliRecenzenti.size());
        odradiliRecenzenti.add(trenutniRecenzent);
        System.out.println("odradili recenzenti size posle: " + odradiliRecenzenti.size());
        delegateExecution.setVariable("odradiliRecenzenti", odradiliRecenzenti);

        List<String> komentarAutoru = (List<String>) delegateExecution.getVariable("komentarAutoru");
        List<String> preporuke = (List<String>) delegateExecution.getVariable("preporuke");
        List<String> komentarUredniku = (List<String>) delegateExecution.getVariable("komentarUredniku");

        List<FormSubmissionDto> recenziranjeData = (List<FormSubmissionDto>) delegateExecution.getVariable("recenziranjeData");

        for(FormSubmissionDto data: recenziranjeData){
            if(data.getFieldId().equals("komentar_autoru")){
                komentarAutoru.add(data.getFieldValue());
            }else if(data.getFieldId().equals("preporuka")){
                preporuke.add(data.getFieldValue());
            }else if(data.getFieldId().equals("komentar_uredniku")){
                komentarUredniku.add(data.getFieldValue());
            }
        }

        delegateExecution.setVariable("komentarAutoru", komentarAutoru);
        delegateExecution.setVariable("preporuke", preporuke);
        delegateExecution.setVariable("komentarUredniku", komentarUredniku);

        System.out.println("izasao iz CuvanjeRecenzenzija");

    }
}

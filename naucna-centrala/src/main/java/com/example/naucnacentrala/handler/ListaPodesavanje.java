package com.example.naucnacentrala.handler;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListaPodesavanje implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u ListaPodesavanje");

        List<String> odradiliRecenzenti = (List<String>) delegateExecution.getVariable("odradiliRecenzenti");
        for(String recenzent: odradiliRecenzenti){
            System.out.println("odradio: " + recenzent);
        }
        delegateExecution.setVariable("izabraniRecenzenti", odradiliRecenzenti);

        List<String> komentarAutoru = new ArrayList<>();
        List<String> preporuke = new ArrayList<>();
        List<String> komentarUredniku = new ArrayList<>();
        odradiliRecenzenti = new ArrayList<>();
        delegateExecution.setVariable("komentarAutoru", komentarAutoru);
        delegateExecution.setVariable("preporuke", preporuke);
        delegateExecution.setVariable("komentarUredniku", komentarUredniku);
        delegateExecution.setVariable("odradiliRecenzenti", odradiliRecenzenti);

        System.out.println("izasao iz ListaPodesavanje");

    }
}

package com.example.naucnacentrala.handler;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class CuvanjeNovogRecenzenta implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjeNovogRecenzenta");

        String noviRecenzentUsername = (String) delegateExecution.getVariable("noviRecenzentUsername");
        delegateExecution.setVariable("oneRecenzent", noviRecenzentUsername);

        System.out.println("izasao iz CuvanjeNovogRecenzenta");

    }
}

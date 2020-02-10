package com.example.naucnacentrala.handler;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class Provera implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u Provera");

        System.out.println("procesInstanceId: " + delegateExecution.getProcessInstanceId());
        String glavniProcesId = (String) delegateExecution.getVariable("glavniProcesId");
        System.out.println("glavniProcesId: " + glavniProcesId);

        System.out.println("izasao iz Provera");
    }
}

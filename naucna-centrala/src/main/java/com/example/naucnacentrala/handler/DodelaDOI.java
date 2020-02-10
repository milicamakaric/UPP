package com.example.naucnacentrala.handler;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class DodelaDOI implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u DodelaDOI");

        System.out.println("izasao iz DodelaDOI");

    }
}

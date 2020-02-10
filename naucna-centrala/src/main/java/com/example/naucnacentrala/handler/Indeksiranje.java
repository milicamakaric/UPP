package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Indeksiranje implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u Indeksiranje");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        rad.setPrihvacen(true);
        rad = radService.save(rad);

        System.out.println("izasao iz Indeksiranje");

    }
}

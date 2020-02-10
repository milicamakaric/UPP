package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GlavniUrednik implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u GlavniUrednik");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        String mailGlavniUrednik = rad.getCasopis().getGlavniUrednik().getUsername();
        String mailAutor = rad.getAutor().getUsername();

        List<String> mailList = new ArrayList<>();
        mailList.add(mailGlavniUrednik);
        mailList.add(mailAutor);

        delegateExecution.setVariable("lista_mejl", mailList);

        System.out.println("izasao iz GlavniUrednik");

    }
}

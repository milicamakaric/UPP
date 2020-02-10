package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuvanjeKoautor implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjeKoautor");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        System.out.println("radId: " + radId);
        Rad rad = radService.findOneById(radId);

        Korisnik koautor = new Korisnik();

        List<FormSubmissionDto> koautorData = (List<FormSubmissionDto>) delegateExecution.getVariable("koautorData");

        for(FormSubmissionDto dto: koautorData){
            if(dto.getFieldId().equals("koautor_ime")){
                koautor.setIme(dto.getFieldValue());
            }else if(dto.getFieldId().equals("koautor_email")){
                koautor.setEmail(dto.getFieldValue());
            }else if(dto.getFieldId().equals("koautor_grad")){
                koautor.setGrad(dto.getFieldValue());
            }else if(dto.getFieldId().equals("koautor_drzava")){
                koautor.setDrzava(dto.getFieldValue());
            }
        }

        rad.getKoautori().add(koautor);
        rad = radService.save(rad);

        System.out.println("izasao iz CuvanjeKoautor");

    }
}

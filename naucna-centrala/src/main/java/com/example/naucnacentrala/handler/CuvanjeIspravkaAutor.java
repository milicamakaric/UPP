package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuvanjeIspravkaAutor implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjeIspravkaAutor");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);
        List<FormSubmissionDto> ispravkaAutor = (List<FormSubmissionDto>) delegateExecution.getVariable("ispravkaAutor");

        for(FormSubmissionDto data: ispravkaAutor){
            if(data.getFieldId().equals("ispravka_prilog_PDF")){
                rad.setPdfLokacija(data.getFieldValue());
                delegateExecution.setVariable("ispravka_prilog_PDF", data.getFieldValue());
            }else if(data.getFieldId().equals("komentar_odgovor")){
                delegateExecution.setVariable("komentar_odgovor", data.getFieldValue());
            }
        }

        rad = radService.save(rad);

        System.out.println("izasao iz CuvanjeIspravkaAutor");

    }
}

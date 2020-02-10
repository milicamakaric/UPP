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
public class CuvanjeNoviPdf implements JavaDelegate {

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjeNoviPdf");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        List<FormSubmissionDto> korigovanjePdf = (List<FormSubmissionDto>) delegateExecution.getVariable("korigovanjePdf");

        for(FormSubmissionDto dto: korigovanjePdf){
            if(dto.getFieldId().equals("prilog_PDF")){
                rad.setPdfLokacija(dto.getFieldValue());
                delegateExecution.setVariable("pdf", dto.getFieldValue());
            }
        }

        rad = radService.save(rad);

        System.out.println("izasao iz CuvanjeNoviPdf");

    }
}

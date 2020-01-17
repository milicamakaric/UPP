package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.service.CasopisService;
import com.example.naucnacentrala.service.KorisnikService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoviUrednici implements JavaDelegate {

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u NoviUrednici");

        Integer casopisId = (Integer) delegateExecution.getVariable("casopisId");
        Casopis casopis = casopisService.findOneById(casopisId);
        List<FormSubmissionDto> ispravkaDto = (List<FormSubmissionDto>) delegateExecution.getVariable("ispravkaUrednici");

        List<Korisnik> noviUrednici = new ArrayList<>();

        for(FormSubmissionDto dto: ispravkaDto){
            if(dto.getFieldId().equals("urednici_novi")){
                Korisnik urednik = korisnikService.findOneByUsername(dto.getFieldValue());
                noviUrednici.add(urednik);
            }
        }

        casopis.setUrednici(noviUrednici);
        casopis = casopisService.save(casopis);

        System.out.println("izasao iz NoviUrednici");

    }
}

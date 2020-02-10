package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.NaucnaOblast;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.CasopisService;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.service.NaucnaOblastService;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CuvanjePodatakaRad implements JavaDelegate {

    @Autowired
    private NaucnaOblastService naucnaOblastService;

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u CuvanjePodatakaRad");

        Integer casopisId = Integer.parseInt((String) delegateExecution.getVariable("izborCasopisId"));
        Casopis casopis = casopisService.findOneById(casopisId);

        String autorUsername = (String) delegateExecution.getVariable("autorUsername");
        Korisnik autor = korisnikService.findOneByUsername(autorUsername);

        List<FormSubmissionDto> radInfo = (List<FormSubmissionDto>) delegateExecution.getVariable("radInfo");

        Rad rad = new Rad();

//        TODO pdf sacuvati

        for(FormSubmissionDto dto: radInfo){
            if(dto.getFieldId().equals("naslov")){
                rad.setNaslov(dto.getFieldValue());
            }else if(dto.getFieldId().equals("kljucni_pojmovi")){
                List<String> pojmovi = Arrays.asList(dto.getFieldValue().split("\\s*;\\s*"));
                rad.setKljucniPojmovi(pojmovi);
            }else if(dto.getFieldId().equals("apstrakt")){
                rad.setApstrakt(dto.getFieldValue());
            }else if(dto.getFieldId().equals("naucna_oblast")){
                NaucnaOblast oblast = naucnaOblastService.findOneById(Integer.parseInt(dto.getFieldValue()));
                rad.setNaucnaOblast(oblast);
            }else if(dto.getFieldId().equals("pdf")){
                rad.setPdfLokacija(dto.getFieldValue());
            }
        }

        rad.setPrihvacen(false);
        rad.setAutor(autor);
        rad.setCasopis(casopis);

        rad = radService.save(rad);
        delegateExecution.setVariable("radId", rad.getId());
        System.out.println("id novog rada: " + rad.getId());

        casopis.getRadovi().add(rad);
        casopis = casopisService.save(casopis);

        System.out.println("izasao iz CuvanjePodatakaRad");

    }
}

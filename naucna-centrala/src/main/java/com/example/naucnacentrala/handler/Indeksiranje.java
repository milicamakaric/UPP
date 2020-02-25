package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.model.RadES;
import com.example.naucnacentrala.repository.RadESRepository;
import com.example.naucnacentrala.service.RadESService;
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

    @Autowired
    private RadESRepository radESRepository;

    @Autowired
    private RadESService radESService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u Indeksiranje");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        rad.setPrihvacen(true);
        rad = radService.save(rad);

        RadES radES = new RadES();
        radES.setId(rad.getId());
        radES.setRadId(rad.getId());
        radES.setNaslov(rad.getNaslov());
        radES.setNaslovCasopisa(rad.getCasopis().getNaziv());
        radES.setApstrakt(rad.getApstrakt());

        String kljucniPojmovi = "";

        for(String kljucniPojam: rad.getKljucniPojmovi()){
            kljucniPojmovi += kljucniPojam + ";";
        }

        radES.setKljucniPojmovi(kljucniPojmovi);
        radES.setNaucnaOblast(rad.getNaucnaOblast().getNaziv());
        radES.setPdfLokacija(radService.getPath(rad.getId()));

        String tekst = radESService.parsePDF(rad);
        radES.setTekst(tekst);

        String autori = "";
        autori += rad.getAutor().getIme() + " " + rad.getAutor().getPrezime();

        for(Korisnik koautor: rad.getKoautori()){
            autori += ", " + koautor.getIme();
        }

        radES.setAutori(autori);

        radES = radESRepository.save(radES);

        System.out.println("izasao iz Indeksiranje");

    }
}

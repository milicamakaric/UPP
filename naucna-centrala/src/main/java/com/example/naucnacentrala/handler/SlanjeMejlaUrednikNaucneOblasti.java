package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SlanjeMejlaUrednikNaucneOblasti implements JavaDelegate {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u SlanjeMejlaUrednikNaucneOblasti");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        String urednikUsername = (String) delegateExecution.getVariable("urednik_oblasti");
        Korisnik urednikNaucneOblasti = korisnikService.findOneByUsername(urednikUsername);
        System.out.println("urednik naucne oblasti username: " + urednikUsername);

        System.out.println("Slanje mejla na: " + urednikNaucneOblasti.getEmail() + "...");

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(urednikNaucneOblasti.getEmail());
            message.setSubject("Prijava novog rada u sistem (urednik naucne oblasti)");

            message.setText("Postovani/a, " + urednikNaucneOblasti.getIme() + " " + urednikNaucneOblasti.getPrezime() + "\n\n" +
                            "U sistem je prijavljen novi rad pod nazivom " + rad.getNaslov() + ".\n\n Srdacno,\n Naucna centrala");
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

        System.out.println("izasao iz SlanjeMejlaUrednikNaucneOblasti");

    }

}

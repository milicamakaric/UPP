package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SlanjeMejlaNoviRecenzent implements JavaDelegate {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private KorisnikService korisnikService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u SlanjeMejlaNoviRecenzent");

        String urednikUsername = (String) delegateExecution.getVariable("urednik_oblasti");
        Korisnik urednikNaucneOblasti = korisnikService.findOneByUsername(urednikUsername);
        System.out.println("urednik naucne oblasti username: " + urednikUsername);

        String trenutniRecenzent = (String) delegateExecution.getVariable("oneRecenzent");
        System.out.println("trenutni recenzent: " + trenutniRecenzent);
        Korisnik recenzent = korisnikService.findOneByUsername(trenutniRecenzent);

        System.out.println("Slanje mejla na: " + urednikNaucneOblasti.getEmail() + "...");

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(urednikNaucneOblasti.getEmail());
            message.setSubject("Ponovni izbor recenzenta");

            message.setText("Postovani/a, " + urednikNaucneOblasti.getIme() + " " + urednikNaucneOblasti.getPrezime() + "\n\n" +
                    "Recenzent " + recenzent.getIme() + " " + recenzent.getPrezime() + " nije obavio recenziranje u zadatom roku. Molimo Vas, izaberite novog recenzenta.\n\n Srdacno,\n Naucna centrala");
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

        System.out.println("izasao iz SlanjeMejlaNoviRecenzent");

    }

}

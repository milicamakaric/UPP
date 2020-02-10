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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlanjeMejlaRad implements JavaDelegate {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u SlanjeMejlaRad");

        String username = (String) delegateExecution.getVariable("mail");
        System.out.println("Slanje mejla: " + username + "...");

        Korisnik korisnik = korisnikService.findOneByUsername(username);

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(korisnik.getEmail());
            message.setSubject("Prijava novog rada u sistem");

            message.setText("Postovani/a, " + korisnik.getIme() + " " + korisnik.getPrezime() + "\n\n" +
                    "U sistem je prijavljen novi rad pod nazivom " + rad.getNaslov() + ".\n\n Srdacno,\n Naucna centrala");
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

        System.out.println("izasao iz SlanjeMejlaRad");

    }
}

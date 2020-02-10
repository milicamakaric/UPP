package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SlanjeMejlaOdbijanjeRada implements JavaDelegate {

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private RadService radService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u SlanjeMejlaOdbijanjeRada");

        Integer radId = (Integer) delegateExecution.getVariable("radId");
        Rad rad = radService.findOneById(radId);
        Korisnik autor = radService.findOneById(radId).getAutor();

        System.out.println("Slanje mejla na: " + autor.getEmail() + "...");

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(autor.getEmail());
            message.setSubject("Odbijanje rada");

            message.setText("Postovani/a, " + autor.getIme() + " " + autor.getPrezime() + "\n\n" +
                    "Vas rad pod nazivom " + rad.getNaslov() + " je odbijen.\n\n Srdacno,\n Naucna centrala");
            emailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Done");

        System.out.println("izasao iz SlanjeMejlaOdbijanjeRada");

    }

}

package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.NaplacujeClanarina;
import com.example.naucnacentrala.service.CasopisService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAccess implements JavaDelegate {

    @Autowired
    private CasopisService casopisService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        System.out.println("usao u OpenAccess");

        Integer casopisId = Integer.parseInt((String) delegateExecution.getVariable("izborCasopisId"));
        System.out.println("casopisId: " + casopisId);

        Casopis casopis = casopisService.findOneById(casopisId);
        if(casopis.getNaplataClanarine().equals(NaplacujeClanarina.NAPLATA_AUTORIMA)){
            delegateExecution.setVariable("open_access", true);
            System.out.println("open access true");
        }else{
            delegateExecution.setVariable("open_access", false);
            System.out.println("open access false");
        }

        System.out.println("izasao iz OpenAccess");

    }
}

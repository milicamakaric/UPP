package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostavljanjeAssigneeUrednikOblasti implements TaskListener {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RadService radService;

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("usao u PostavljanjeAssigneeUrednikOblasti");

        String urednikOblastiUsername = (String) runtimeService.getVariable(delegateTask.getProcessInstanceId(), "urednik_oblasti");
        System.out.println("urednikOblastiUsername: " + urednikOblastiUsername);

        delegateTask.setAssignee(urednikOblastiUsername);

        System.out.println("izasao iz PostavljanjeAssigneeUrednikOblasti");
    }
}

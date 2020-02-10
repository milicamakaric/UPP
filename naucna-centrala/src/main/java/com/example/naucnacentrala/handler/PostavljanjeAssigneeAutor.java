package com.example.naucnacentrala.handler;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostavljanjeAssigneeAutor implements TaskListener {

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("usao u PostavljanjeAssigneeAutor");

        String username = (String) runtimeService.getVariable(delegateTask.getProcessInstanceId(), "autorUsername");
        System.out.println("autor username: " + username);

        delegateTask.setAssignee(username);

        System.out.println("izasao iz PostavljanjeAssigneeAutor");
    }
}

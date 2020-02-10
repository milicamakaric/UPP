package com.example.naucnacentrala.handler;

import com.example.naucnacentrala.service.RadService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostavljanjeAssigneeGlavniUrednik implements TaskListener {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RadService radService;

    @Override
    public void notify(DelegateTask delegateTask) {

        System.out.println("usao u PostavljanjeAssigneeGlavniUrednik");

        Integer radId = (Integer) runtimeService.getVariable(delegateTask.getProcessInstanceId(), "radId");
        String glavniUrednikUsername = radService.findOneById(radId).getCasopis().getGlavniUrednik().getUsername();
        System.out.println("glavni urednik username: " + glavniUrednikUsername);

        delegateTask.setAssignee(glavniUrednikUsername);

        System.out.println("izasao iz PostavljanjeAssigneeGlavniUrednik");
    }
}

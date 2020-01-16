package com.example.naucnacentrala.controller;

import com.example.naucnacentrala.dto.FormFieldsDto;
import com.example.naucnacentrala.dto.FormSubmissionDto;
import com.example.naucnacentrala.model.Casopis;
import com.example.naucnacentrala.model.Korisnik;
import com.example.naucnacentrala.model.NaucnaOblast;
import com.example.naucnacentrala.security.TokenUtils;
import com.example.naucnacentrala.service.CasopisService;
import com.example.naucnacentrala.service.KorisnikService;
import com.example.naucnacentrala.utils.Utils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/casopis")
public class CasopisController {

    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CasopisService casopisService;

    @GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
        System.out.println("usao u metodu casopis/get");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Proces_kreiranja_novog_casopisa");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        //System.out.println("size:" + taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().size());

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();


        return new FormFieldsDto(task.getId(), properties, processInstance.getId());
    }

    @PostMapping(path = "/postCasopisData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postCasopisData(@RequestBody List<FormSubmissionDto> casopisDto, @PathVariable String taskId, HttpServletRequest request) {
        System.out.println("usao u metodu casopis/postCasopisData");

        HashMap<String, Object> map = Utils.mapListToDto(casopisDto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "casopis", casopisDto);

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        runtimeService.setVariable(processInstanceId, "glavniUrednikUsername", username);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getUredniciRecenzenti/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getUredniciRecenzenti(@PathVariable("processInstanceId") String processInstanceId) {
        System.out.println("usao u metodu casopis/getUredniciRecenzenti");

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        System.out.println("taskId:: " + task.getId());

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        Integer casopisId = (Integer) runtimeService.getVariable(processInstanceId, "casopisId");
        System.out.println("casopisId: " + casopisId);

        Casopis casopis = casopisService.findOneById(casopisId);
        List<NaucnaOblast> naucneOblasti = casopis.getNaucneOblasti();
        System.out.println("naucne oblasti size: " + naucneOblasti.size());

        List<Korisnik> urednici = korisnikService.findAllByNaucneOblasti(naucneOblasti, "urednik");
        System.out.println("size urednici: " + urednici.size());
        for(FormField field : properties){
            if(field.getId().equals("urednici")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(Korisnik urednik: urednici){
                    enumType.getValues().put(urednik.getUsername(), urednik.getIme() + " " + urednik.getPrezime());
                }
                break;
            }
        }

        List<Korisnik> recenzenti = korisnikService.findAllByNaucneOblasti(naucneOblasti, "recenzent");
        System.out.println("size recenzenti: " + recenzenti.size());
        for(FormField field : properties){
            if(field.getId().equals("recenzenti")){
                EnumFormType enumType = (EnumFormType) field.getType();
                for(Korisnik recenzent: recenzenti){
                    enumType.getValues().put(recenzent.getUsername(), recenzent.getIme() + " " + recenzent.getPrezime());
                }
                break;
            }
        }


        return new FormFieldsDto(task.getId(), properties, processInstance.getId());
    }

    @PostMapping(path = "/postUredniciRecenzentiData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postUredniciRecenzentiData(@RequestBody List<FormSubmissionDto> uredniciRecenzentiDto, @PathVariable String taskId) {
        System.out.println("usao u metodu casopis/postUredniciRecenzentiData");

        HashMap<String, Object> map = Utils.mapListToDto(uredniciRecenzentiDto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "uredniciRecenzentiDto", uredniciRecenzentiDto);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getTasks", produces = "application/json")
    @PreAuthorize("hasAuthority('PROVERA_CASOPISA')")
    public @ResponseBody ResponseEntity<List<FormFieldsDto>> getTasks() {

        System.out.println("usao u metodu casopis/getTasks");

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("admin").list();

        List<FormFieldsDto> formFieldsDtos = new ArrayList<>();
        for(Task task: tasks){
            if(task.getName().equals("Provera podataka casopisaa")) {
                FormFieldsDto formFieldsDto = new FormFieldsDto();
                formFieldsDto.setTaskId(task.getId());
                System.out.println("taskId:" + task.getId());

                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormField> properties = taskFormData.getFormFields();

                formFieldsDto.setFormFields(properties);

                formFieldsDtos.add(formFieldsDto);
            }
        }

        return new ResponseEntity(formFieldsDtos,  HttpStatus.OK);

    }

    @PostMapping(path = "/postAdminCasopisData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postAdminCasopisData(@RequestBody List<FormSubmissionDto> adminCasopisDto, @PathVariable String taskId) {
        System.out.println("usao u metodu casopis/postCasopisData");

        HashMap<String, Object> map = Utils.mapListToDto(adminCasopisDto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "adminCasopisDto", adminCasopisDto);

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/getUrednikTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<FormFieldsDto>> getUrednikTasks(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getUrednikTasks");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username urednik: " + username);

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).list();

        List<FormFieldsDto> formFieldsDtos = new ArrayList<>();
        for(Task task: tasks){
            if(task.getName().contains("Ispravljanje")) {
                FormFieldsDto formFieldsDto = new FormFieldsDto();
                formFieldsDto.setTaskId(task.getId());

                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                List<FormField> properties = taskFormData.getFormFields();

                formFieldsDto.setFormFields(properties);

                formFieldsDtos.add(formFieldsDto);
            }

        }

        return new ResponseEntity(formFieldsDtos,  HttpStatus.OK);

    }

    @PostMapping(path = "/postIspravkaData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postIspravkaData(@RequestBody List<FormSubmissionDto> ispravkaDto, @PathVariable String taskId) {
        System.out.println("usao u metodu casopis/postIspravkaData");

        HashMap<String, Object> map = Utils.mapListToDto(ispravkaDto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();

        for(FormSubmissionDto dto: ispravkaDto){
            if(dto.getFieldId().equals("stari_naziv") || dto.getFieldId().equals("naziv")){
                runtimeService.setVariable(processInstanceId, "ispravkaNaziv", ispravkaDto);
                break;
            }
            if(dto.getFieldId().equals("stari_ISSN_broj") || dto.getFieldId().equals("ISSN_broj")){
                runtimeService.setVariable(processInstanceId, "ispravkaISSN", ispravkaDto);
                break;
            }
            if(dto.getFieldId().equals("stara_naplata_clanarine") || dto.getFieldId().equals("naplata_clanarine")){
                runtimeService.setVariable(processInstanceId, "ispravkaNaplata", ispravkaDto);
                break;
            }
        }

        formService.submitTaskForm(taskId, map);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

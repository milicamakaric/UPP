package com.example.naucnacentrala.controller;

import com.example.naucnacentrala.dto.*;
import com.example.naucnacentrala.model.*;
import com.example.naucnacentrala.security.TokenUtils;
import com.example.naucnacentrala.service.*;
import com.example.naucnacentrala.utils.Utils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/rad")
public class RadController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private PreporukaService preporukaService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CasopisService casopisService;

    @Autowired
    private RadService radService;

    @Autowired
    private NacinPlacanjaService nacinPlacanjaService;

    @Autowired
    private NaucnaOblastService naucnaOblastService;

    @GetMapping(path = "/get", produces = "application/json")
    @PreAuthorize("hasAuthority('NOVI_RAD')")
    public @ResponseBody FormFieldsDto get() {
        System.out.println("usao u metodu rad/get");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Proces_obrade_podnetog_teksta");
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        List<CasopisDTO> casopisi = getCasopisiDTO();
        for(FormField field : properties){
            if(field.getId().equals("izabrani_casopis")){
                EnumFormType enumType = (EnumFormType) field.getType();
                enumType.getValues().clear();
                for(CasopisDTO casopis: casopisi){
                    enumType.getValues().put(casopis.getId().toString(), casopis.getName());
                }
                break;
            }
        }

        return new FormFieldsDto(task.getId(), properties, processInstance.getId());
    }

    @PostMapping(path = "/postIzborCasopisa/{taskId}", produces = "application/json")
    public @ResponseBody StatusDTO postIzborCasopisa(@RequestBody List<FormSubmissionDto> izborCasopisa, @PathVariable String taskId, HttpServletRequest request) {
        System.out.println("usao u metodu rad/postIzborCasopisa");

        HashMap<String, Object> map = Utils.mapListToDto(izborCasopisa);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "izborCasopisId", izborCasopisa.get(0).getFieldValue());

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        runtimeService.setVariable(processInstanceId, "autorUsername", username);
        System.out.println("casopisId: " + izborCasopisa.get(0).getFieldValue() + "; autorUsername: " + username);
        runtimeService.setVariable(processInstanceId,"glavniProcesId", processInstanceId);

        formService.submitTaskForm(taskId, map);

        boolean openAccess = (boolean) runtimeService.getVariable(processInstanceId, "open_access");
        System.out.println("openAccess: " + openAccess);

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setStatus("false");
        statusDTO.setMessage("false");

        if(openAccess){
            boolean clanarina = (boolean) runtimeService.getVariable(processInstanceId, "clanarina");
            System.out.println("clanarina: " + clanarina);

            if(!clanarina){
                statusDTO.setStatus("true");
                ProcessInstance subprocess = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstanceId).singleResult();
                System.out.println("subprocessId: " + subprocess.getId());
                statusDTO.setMessage(subprocess.getId());
            }
        }

        System.out.println("izasao iz rad/postIzborCasopisa");

        return statusDTO;
    }

    @GetMapping(path = "/placanje/{potprocesId}", produces = "application/json")
    @PreAuthorize("hasAuthority('NOVI_RAD')")
    public @ResponseBody FormFieldsDto getPlacanje(@PathVariable String potprocesId) {
        System.out.println("usao u metodu rad/placanje");

        Task task = taskService.createTaskQuery().processInstanceId(potprocesId).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        List<NacinPlacanja> naciniPlacanja = nacinPlacanjaService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("nacin_placanja")){
                EnumFormType enumType = (EnumFormType) field.getType();
                enumType.getValues().clear();
                for(NacinPlacanja nacin: naciniPlacanja){
                    enumType.getValues().put(nacin.getId().toString(), nacin.getNaziv());
                }
                break;
            }
        }

        return new FormFieldsDto(task.getId(), properties, potprocesId);
    }

    @PostMapping(path = "/postPlacanje/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postPlacanje(@RequestBody List<FormSubmissionDto> placanje, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postPlacanje");

        HashMap<String, Object> map = Utils.mapListToDto(placanje);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        System.out.println("potprocesId: " + processInstanceId);
        runtimeService.setVariable(processInstanceId, "placanje", placanje.get(0).getFieldValue());

        formService.submitTaskForm(taskId, map);

        System.out.println("izasao iz rad/postPlacanje");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/radInfo/{procesId}", produces = "application/json")
    @PreAuthorize("hasAuthority('NOVI_RAD')")
    public @ResponseBody FormFieldsDto getRadInfo(@PathVariable String procesId) {
        System.out.println("usao u metodu rad/radInfo");

        Task task = taskService.createTaskQuery().processInstanceId(procesId).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        List<NaucnaOblast> naucneOblasti = naucnaOblastService.findAll();
        for(FormField field : properties){
            if(field.getId().equals("naucna_oblast")){
                EnumFormType enumType = (EnumFormType) field.getType();
                enumType.getValues().clear();
                for(NaucnaOblast oblast: naucneOblasti){
                    enumType.getValues().put(oblast.getId().toString(), oblast.getNaziv());
                }
                break;
            }
        }

        return new FormFieldsDto(task.getId(), properties, procesId);
    }

    @PostMapping(path = "/postRadInfo/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postRadInfo(@RequestBody List<FormSubmissionDto> radInfo, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postRadInfo");

        HashMap<String, Object> map = Utils.mapListToDto(radInfo);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "radInfo", radInfo);

        formService.submitTaskForm(taskId, map);

        Long preostaloKoautora = (Long) runtimeService.getVariable(processInstanceId, "koautori_broj");
        runtimeService.setVariable(processInstanceId, "preostaloKoautora", preostaloKoautora);

        System.out.println("izasao iz rad/postRadInfo");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/koautor/{procesId}", produces = "application/json")
    @PreAuthorize("hasAuthority('NOVI_RAD')")
    public @ResponseBody FormFieldsDto getKoautor(@PathVariable String procesId, HttpServletRequest request) {
        System.out.println("usao u metodu rad/koautor");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).taskName("Koautorii").list();
        System.out.println("tasks size: " + tasks.size());
        Task task = tasks.get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        return new FormFieldsDto(task.getId(), properties, procesId);
    }

    @PostMapping(path = "/postKoautorData/{taskId}", produces = "application/json")
    public @ResponseBody StatusDTO postKoautorData(@RequestBody List<FormSubmissionDto> koautorData, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postKoautorData");

        HashMap<String, Object> map = Utils.mapListToDto(koautorData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "koautorData", koautorData);

        formService.submitTaskForm(taskId, map);

        StatusDTO statusDTO = new StatusDTO();

        Long preostaloKoautora = (Long) runtimeService.getVariable(processInstanceId, "preostaloKoautora");
        System.out.println("preostaloKoautora pre: " + preostaloKoautora);
        preostaloKoautora -=1;
        runtimeService.setVariable(processInstanceId, "preostaloKoautora", preostaloKoautora);
        preostaloKoautora = (Long) runtimeService.getVariable(processInstanceId, "preostaloKoautora");
        System.out.println("preostaloKoautora posle: " + preostaloKoautora);

        if(preostaloKoautora > 0){
            System.out.println("Treba jos koautora da se dodaje");
            statusDTO.setStatus("ima");
        }else{
            System.out.println("Nema vise koautora za dodavanje");
            statusDTO.setStatus("nema");
        }

        System.out.println("izasao iz rad/postKoautorData");

        return statusDTO;
    }

    @GetMapping(path = "/getGlavniUrednikTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<FormFieldsDto>> getGlavniUrednikTasks(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getGlavniUrednikTasks");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username glavni urednik: " + username);

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).taskName("Pregled radova").list();

        List<FormFieldsDto> formFieldsDtos = new ArrayList<>();
        for(Task task: tasks){
            FormFieldsDto formFieldsDto = new FormFieldsDto();
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            formFieldsDto.setFormFields(properties);
            formFieldsDtos.add(formFieldsDto);

        }

        return new ResponseEntity(formFieldsDtos,  HttpStatus.OK);

    }

    @PostMapping(path = "/postGlavniUrednikData/{taskId}", produces = "application/json")
    public @ResponseBody StatusDTO postGlavniUrednikData(@RequestBody List<FormSubmissionDto> glavniUrednikData, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postGlavniUrednikData");

        HashMap<String, Object> map = Utils.mapListToDto(glavniUrednikData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "glavniUrednikData", glavniUrednikData);
        Integer radId = (Integer) runtimeService.getVariable(processInstanceId, "radId");
        System.out.println("radId: " + radId);

        formService.submitTaskForm(taskId, map);

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setMessage(radId.toString());

        for(FormSubmissionDto dto: glavniUrednikData){
            if(dto.getFieldId().equals("rad_relevantan")){
                if(dto.getFieldValue().equals("true")){
                    System.out.println("Rad je relevantan");
                    statusDTO.setStatus("relevantan");
                }else{
                    System.out.println("rad nije relevantan");
                    statusDTO.setStatus("nije relevantan");
                }
            }
        }

        System.out.println("izasao iz postGlavniUrednikData");

        return statusDTO;
    }

    @GetMapping(path = "/getRadPdf/{procesId}", produces = "application/json")
    public @ResponseBody FormFieldsDto getRadPdf(@PathVariable String procesId) {

        System.out.println("usao u metodu casopis/getRadPdf");

        Task task = taskService.createTaskQuery().processInstanceId(procesId).list().get(0);

        TaskFormData taskFormData = formService.getTaskFormData(task.getId());
        List<FormField> properties = taskFormData.getFormFields();

        return new FormFieldsDto(task.getId(), properties, procesId);

    }

    @PostMapping(path = "/postPregledPdfData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postPregledPdfData(@RequestBody List<FormSubmissionDto> pregledPdf, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postPregledPdfData");

        HashMap<String, Object> map = Utils.mapListToDto(pregledPdf);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "pregledPdf", pregledPdf);

        formService.submitTaskForm(taskId, map);

        System.out.println("izasao iz rad/postPregledPdfData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getKorigovanjePdfAutorTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getKorigovanjePdfAutorTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getKorigovanjePdfAutorTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Korigovanje PDF").singleResult();


        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema radova za korekciju");
            formFieldsDto.setFormFields(new ArrayList<>());
        }



        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postKorigovanjePdfData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postKorigovanjePdfData(@RequestBody List<FormSubmissionDto> korigovanjePdf, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postKorigovanjePdfData");

        HashMap<String, Object> map = Utils.mapListToDto(korigovanjePdf);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "korigovanjePdf", korigovanjePdf);

        formService.submitTaskForm(taskId, map);

        System.out.println("izasao iz rad/postKorigovanjePdfData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getVremeRecenziranjaTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getVremeRecenziranjaTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getVremeRecenziranjaTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Vreme recenziranja").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za postavljanje vremena recenziranja");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postVremeRecenziranjaData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postVremeRecenziranjaData(@RequestBody List<FormSubmissionDto> vremeRecenziranja, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postVremeRecenziranjaData");

        HashMap<String, Object> map = Utils.mapListToDto(vremeRecenziranja);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "vremeRecenziranja", vremeRecenziranja);

        formService.submitTaskForm(taskId, map);

        boolean ima_recenzenata = (boolean) runtimeService.getVariable(processInstanceId, "ima_recenzenata");
        System.out.println("ima_recenzenata: " + ima_recenzenata);

        System.out.println("izasao iz rad/postVremeRecenziranjaData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getIzborRecenzenataTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getIzborRecenzenataTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getIzborRecenzenataTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Izbor recenzenata").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<Korisnik> recenzenti = (List<Korisnik>) runtimeService.getVariable(task.getProcessInstanceId(), "recenzenti_lista");
            for(FormField field : properties){
                if(field.getId().equals("select_box1")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(Korisnik recenzent: recenzenti){
                        enumType.getValues().put(recenzent.getId().toString(), recenzent.getIme() + " " + recenzent.getPrezime());
                    }
                    break;
                }
            }

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za izbor recenzenata");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postIzborRecenenataData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postIzborRecenenataData(@RequestBody List<FormSubmissionDto> izborRecenzenata, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postIzborRecenenataData");

        HashMap<String, Object> map = Utils.mapListToDto(izborRecenzenata);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "izborRecenzenata", izborRecenzenata);

        formService.submitTaskForm(taskId, map);

        System.out.println("izasao iz rad/postIzborRecenenataData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getRecenziranjeTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getRecenziranjeTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getRecenziranjeTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username recenzent: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Recenziranje").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<Preporuka> svePreporuke = preporukaService.findAll();
            List<Preporuka> preporuke = new ArrayList<>();
            for(Preporuka prep: svePreporuke){
                if(!prep.getNaziv().equals("Neophodno jos ispravki")){
                    preporuke.add(prep);
                }
            }

            for(FormField field : properties){
                if(field.getId().equals("preporuka")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(Preporuka preporuka: preporuke){
                        enumType.getValues().put(preporuka.getNaziv(), preporuka.getNaziv());
                    }
                    break;
                }
            }

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za recenzenziranje");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postRecenziranjeData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postRecenziranjeData(@RequestBody List<FormSubmissionDto> recenziranjeData, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postRecenziranjeData");

        HashMap<String, Object> map = Utils.mapListToDto(recenziranjeData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "recenziranjeData", recenziranjeData);

//        formService.submitTaskForm(taskId, map);
        taskService.complete(taskId);

        System.out.println("izasao iz rad/postRecenziranjeData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getNoviRecenzentiTasks", produces = "application/json")
    public @ResponseBody ResponseEntity<List<FormFieldsDto>> getNoviRecenzentiTasks(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getNoviRecenzentiTasks");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).taskName("Novi recenzent").list();

        List<FormFieldsDto> formFieldsDtos = new ArrayList<>();
        for(Task task: tasks) {

            FormFieldsDto formFieldsDto = new FormFieldsDto();

            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<Korisnik> recenzenti = (List<Korisnik>) runtimeService.getVariable(task.getProcessInstanceId(), "recenzenti_lista");
            for (FormField field : properties) {
                if (field.getId().equals("novi_recenzent")) {
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for (Korisnik recenzent : recenzenti) {
                        enumType.getValues().put(recenzent.getUsername(), recenzent.getIme() + " " + recenzent.getPrezime());
                    }
                    break;
                }
            }

            formFieldsDto.setFormFields(properties);
            formFieldsDtos.add(formFieldsDto);

        }

        return new ResponseEntity(formFieldsDtos,  HttpStatus.OK);

    }

    @PostMapping(path = "/postNoviRecenentData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postNoviRecenentData(@RequestBody List<FormSubmissionDto> noviRecenzent, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postNoviRecenentData");

        HashMap<String, Object> map = Utils.mapListToDto(noviRecenzent);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        System.out.println("noviRecenzentUsername: " + noviRecenzent.get(0).getFieldValue());
        runtimeService.setVariable(processInstanceId, "noviRecenzentUsername", noviRecenzent.get(0).getFieldValue());

        formService.submitTaskForm(taskId, map);

        System.out.println("izasao iz rad/postNoviRecenentData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getAnalizaRecenzijaTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getAnalizaRecenzijaTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getAnalizaRecenzijaTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username urednik: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Analiziranje recenzija").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<Preporuka> svePreporuke = preporukaService.findAll();
            List<Preporuka> preporuke = new ArrayList<>();

            for(Preporuka prep: svePreporuke){
                if(!prep.getNaziv().equals("Neophodno jos ispravki")){
                    preporuke.add(prep);
                }
            }
            System.out.println("preporuke size: " + preporuke.size());

            List<String> komentarAutoru = (List<String>) runtimeService.getVariable(task.getProcessInstanceId(), "komentarAutoru");
            List<String> recenzentiPreporuke = (List<String>) runtimeService.getVariable(task.getProcessInstanceId(), "preporuke");
            List<String> komentarUredniku = (List<String>) runtimeService.getVariable(task.getProcessInstanceId(), "komentarUredniku");

            for(FormField field : properties){
                if(field.getId().equals("odluka")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(Preporuka preporuka: preporuke){
                        enumType.getValues().put(preporuka.getNaziv(), preporuka.getNaziv());
                    }
                }else if(field.getId().equals("komentar_autoru_analiza")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(String komentar: komentarAutoru){
                        enumType.getValues().put(komentar, komentar);
                    }
                }else if(field.getId().equals("komentar_uredniku_analiza")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(String komentar: komentarUredniku){
                        enumType.getValues().put(komentar, komentar);
                    }
                }else if(field.getId().equals("preporuka_analiza")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(String komentar: recenzentiPreporuke){
                        enumType.getValues().put(komentar, komentar);
                    }
                }
            }

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za ispravku");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postAnalizaRecenzijaData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postAnalizaRecenzijaData(@RequestBody List<FormSubmissionDto> analizaRecenzijaData, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postAnalizaRecenzijaData");

        HashMap<String, Object> map = Utils.mapListToDto(analizaRecenzijaData);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "odluka", analizaRecenzijaData.get(0).getFieldValue());
        System.out.println("odluka: " + analizaRecenzijaData.get(0).getFieldValue());

//        formService.submitTaskForm(taskId, map);
        taskService.complete(taskId);

        System.out.println("izasao iz rad/postAnalizaRecenzijaData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getIspravkaAutorTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getIspravkaAutorTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getIspravkaAutorTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username autor: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Ispravka").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<String> komentarAutoru = (List<String>) runtimeService.getVariable(task.getProcessInstanceId(), "komentarAutoru");

            for(FormField field : properties){
                if(field.getId().equals("komentar_prikaz")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(String komentar: komentarAutoru){
                        enumType.getValues().put(komentar, komentar);
                    }
                    break;
                }
            }

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za analizu recenzenzija");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postIspravkaAutorData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postIspravkaAutorData(@RequestBody List<FormSubmissionDto> ispravkaAutor, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postIspravkaAutorData");

        HashMap<String, Object> map = Utils.mapListToDto(ispravkaAutor);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "ispravkaAutor", ispravkaAutor);

//        formService.submitTaskForm(taskId, map);
        taskService.complete(taskId);

        System.out.println("izasao iz rad/postIspravkaAutorData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/getPregledIspravljenogRadaTask", produces = "application/json")
    public @ResponseBody ResponseEntity<FormFieldsDto> getPregledIspravljenogRadaTask(HttpServletRequest request) {

        System.out.println("usao u metodu casopis/getPregledIspravljenogRadaTask");

        String username = Utils.getUsernameFromRequest(request, tokenUtils);
        System.out.println("username urednik: " + username);

        Task task = taskService.createTaskQuery().taskAssignee(username).taskName("Pregled ispravljenog rada").singleResult();

        FormFieldsDto formFieldsDto = new FormFieldsDto();
        try {
            formFieldsDto.setTaskId(task.getId());
            formFieldsDto.setProcessInstanceId(task.getProcessInstanceId());

            TaskFormData taskFormData = formService.getTaskFormData(task.getId());
            List<FormField> properties = taskFormData.getFormFields();

            List<Preporuka> svePreporuke = preporukaService.findAll();
            List<Preporuka> preporuke = new ArrayList<>();
            for(Preporuka prep: svePreporuke){
                if(prep.getNaziv().equals("Prihvatiti") || prep.getNaziv().equals("Neophodno jos ispravki")){
                    preporuke.add(prep);
                }
            }
            System.out.println("preporuke size: " + preporuke.size());

            for(FormField field : properties){
                if(field.getId().equals("odluka2")){
                    EnumFormType enumType = (EnumFormType) field.getType();
                    enumType.getValues().clear();
                    for(Preporuka preporuka: preporuke){
                        enumType.getValues().put(preporuka.getNaziv(), preporuka.getNaziv());
                    }
                    break;
                }
            }

            formFieldsDto.setFormFields(properties);
        }catch(Exception e){
            System.out.println("nema taskova za analizu recenzenzija");
            formFieldsDto.setFormFields(new ArrayList<>());
        }

        return new ResponseEntity(formFieldsDto,  HttpStatus.OK);

    }

    @PostMapping(path = "/postPregledIspravljenogRadaData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity postPregledIspravljenogRadaData(@RequestBody List<FormSubmissionDto> pregledRada, @PathVariable String taskId) {
        System.out.println("usao u metodu rad/postPregledIspravljenogRadaData");

        HashMap<String, Object> map = Utils.mapListToDto(pregledRada);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "pregledRada", pregledRada);

        for(FormSubmissionDto data: pregledRada){
            if(data.getFieldId().equals("odluka2")){
                runtimeService.setVariable(task.getProcessInstanceId(), "odluka2", data.getFieldValue());
            }
        }

//        formService.submitTaskForm(taskId, map);
        taskService.complete(taskId);

        System.out.println("izasao iz rad/postPregledIspravljenogRadaData");

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadFile/{procesId}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file , @PathVariable("procesId") String procesId) {

        Integer radId = (Integer) runtimeService.getVariable(procesId, "radId");
        Rad rad = radService.findOneById(radId);
        try {
            rad.setPdf(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rad = radService.save(rad);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping(path = "/downloadFile/{procesId}")
    public @ResponseBody ResponseEntity downloadFile(@PathVariable String procesId) {

        Integer radId = (Integer) runtimeService.getVariable(procesId, "radId");
        Rad rad = radService.findOneById(radId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""  + "rad.pdf\"")
                .body(new ByteArrayResource(rad.getPdf()));
    }


    private List<CasopisDTO> getCasopisiDTO(){
        List<Casopis> casopisi = casopisService.findAll();
        List<CasopisDTO> casopisiDTO = new ArrayList<>();

        for(Casopis c : casopisi){
            if(c.getAktiviran() == true) {
                CasopisDTO dto = new CasopisDTO();
                dto.setId(c.getId());
                dto.setIssn(c.getIssn());
                dto.setName(c.getNaziv());
                dto.setNaplacujeClanarina(c.getNaplataClanarine());
                casopisiDTO.add(dto);
            }
        }

        return casopisiDTO;
    }


}

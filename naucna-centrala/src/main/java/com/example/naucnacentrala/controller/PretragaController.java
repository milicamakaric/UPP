package com.example.naucnacentrala.controller;

import com.example.naucnacentrala.dto.KombinovanaPretragaDTO;
import com.example.naucnacentrala.dto.RadESDTO;
import com.example.naucnacentrala.dto.RecenzentDTO;
import com.example.naucnacentrala.model.*;
import com.example.naucnacentrala.security.TokenUtils;
import com.example.naucnacentrala.service.*;
import com.example.naucnacentrala.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping(path = "pretraga")
public class PretragaController {

    @Autowired
    private KorisnikService korisnikService;

    @Autowired
    private RecenzentESService recenzentESService;

    @Autowired
    private NaucnaOblastService naucnaOblastService;

    @Autowired
    private RadService radService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RadESService radESService;

    @GetMapping(path = "/recenzentiES")
    public @ResponseBody ResponseEntity indexReviewers(){

        List<Korisnik> recenzenti = korisnikService.findAllRecenzenti();
        for(Korisnik recenzent : recenzenti){
            System.out.println("recenzent: " + recenzent.getUsername());

            RecenzentES recenzentES = new RecenzentES();
            recenzentES.setId(recenzent.getId());
            recenzentES.setRecenzentId(recenzent.getId());
            recenzentES.setIme(recenzent.getIme());
            recenzentES.setPrezime(recenzent.getPrezime());
            recenzentES.setLocation(new GeoPoint(recenzent.getLatitude(), recenzent.getLongitude()));
            recenzentESService.save(recenzentES);

        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/naucneOblasti", produces = "application/json")
    public @ResponseBody List<String> naucneOblasti(){

        List<NaucnaOblast> naucneOblasti = naucnaOblastService.findAll();
        List<String> ret = new ArrayList<>();

        for(NaucnaOblast oblast: naucneOblasti){
            ret.add(oblast.getNaziv());
        }

        return ret;
    }

    @GetMapping(path = "/customPretraga/{fraza}/{parametar}/{tip}", produces = "application/json")
    public @ResponseBody ResponseEntity customPretraga(@PathVariable Integer fraza, @PathVariable String parametar, @PathVariable String tip, HttpServletRequest httpRequest) throws Exception {

        System.out.println("fraza: " + fraza);
        System.out.println("parametar: " + parametar);
        System.out.println("tip: " + tip);

        String username = Utils.getUsernameFromRequest(httpRequest, tokenUtils);
        System.out.println("username: " + username);

        Korisnik korisnik = korisnikService.findOneByUsername(username);

        String query = "";
        if (fraza == 0) {

            query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match\" : {\n" +
                    "            \"" + tip + "\" : {\n" +
                    "                \"query\" : \"" + parametar + "\"\n" +
                    "            }\n" +
                    "           \n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"highlight\" : {\n" +
                    "        \"fields\" : {\n" +
                    "            \"" + tip + "\" : {\n" +
                    "            \t\"type\":\"plain\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

//            query = "{\n" +
//                    "    \"query\": {\n" +
//                    "        \"query_string\" : {\n" +
//                    "            \"default_field\" : \"" + tip + "\",\n" +
//                    "            \"query\" : \"*" + parametar + "*\"\n" +
//                    "        }\n" +
//                    "    },\n" +
//                    "    \"highlight\" : {\n" +
//                    "        \"fields\" : {\n" +
//                    "            \"" + tip + "\" : {\n" +
//                    "            \t\"type\":\"plain\"\n" +
//                    "            }\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}";

        } else if (fraza == 1) {

            query = "{\n" +
                    "    \"query\": {\n" +
                    "        \"match_phrase\" : {\n" +
                    "            \"" + tip + "\" : {\n" +
                    "                \"query\" : \"" + parametar + "\"\n" +
                    "         \n" +
                    "            }\n" +
                    "           \n" +
                    "        }\n" +
                    "    },\n" +
                    "    \"highlight\" : {\n" +
                    "        \"fields\" : {\n" +
                    "            \"" + tip + "\" : {\n" +
                    "            \t\"type\":\"plain\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
        }


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);

        String resourceUrl = "http://localhost:9200/casopis/rad/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<RadESDTO> retVal = getRadESDTO(locatedNode, tip, korisnik);


        return new ResponseEntity(retVal, HttpStatus.OK);
    }

    @PostMapping(path = "/advancedPretraga", consumes = "application/json", produces = "application/json")
    public @ResponseBody ResponseEntity advancedPretraga(@RequestBody KombinovanaPretragaDTO dto, HttpServletRequest httpRequest) throws Exception{

        String username = Utils.getUsernameFromRequest(httpRequest, tokenUtils);
        System.out.println("username: " + username);

        Korisnik korisnik = korisnikService.findOneByUsername(username);

        String must = "\"must\" : [\n" ;
        String should = "\"should\" : [\n";
        String fraza = "";

        boolean shouldBe = false;
        boolean mustBe = false;

        if(dto.isFrazaNazivCasopisa()){
            fraza = "match_phrase";
        }else{
            fraza = "match";
        }
        if(dto.isNazivCasopisaOznacen() && !dto.getNaslovCasopisa().equals("")){
            must += "{ \"" + fraza + "\" : { \"naslovCasopisa\" : \""+dto.getNaslovCasopisa()+"\" } },";
            mustBe = true;
        }else if(!dto.isNazivCasopisaOznacen() && !dto.getNaslovCasopisa().equals("")){
            should += "{ \"" + fraza + "\" : { \"naslovCasopisa\" : \""+dto.getNaslovCasopisa()+"\" } },";
            shouldBe = true;
        }

        if(dto.isFrazaNaslovRada()){
            fraza = "match_phrase";
        }else{
            fraza = "match";
        }
        if(dto.isNaslovRadaOznacen() && !dto.getNaslov().equals("")){
            must += "{ \"" + fraza + "\" : { \"naslov\" : \""+dto.getNaslov()+"\" } },";
            mustBe = true;

        }else if(!dto.isNaslovRadaOznacen() && !dto.getNaslov().equals("")){
            should += "{ \"" + fraza + "\" : { \"naslov\" : \""+dto.getNaslov()+"\" } },";
            shouldBe = true;
        }

        if(dto.isFrazaKljucniPojmovi()){
            fraza = "match_phrase";
        }else{
            fraza = "match";
        }
        if(dto.isKljucniPojmoviOznaceni() && !dto.getKljucniPojmovi().equals("")){
            must += "{ \"" + fraza + "\" : { \"kljucniPojmovi\" : \""+dto.getKljucniPojmovi()+"\" } },";
            mustBe = true;

        }else if(!dto.isKljucniPojmoviOznaceni() && !dto.getKljucniPojmovi().equals("")){
            should += "{ \"" + fraza + "\" : { \"kljucniPojmovi\" : \""+dto.getKljucniPojmovi()+"\" } },";
            shouldBe = true;
        }

        if(dto.isFrazaAutori()){
            fraza = "match_phrase";
        }else{
            fraza = "match";
        }
        if(dto.isAutoriOznaceni() && !dto.getAutori().equals("")){
            must += "{ \"" + fraza + "\" : { \"autori\" : \""+dto.getAutori()+"\" } },";
            mustBe = true;

        }else if(!dto.isAutoriOznaceni() && !dto.getAutori().equals("")){
            should += "{ \"" + fraza + "\" : { \"autori\" : \""+dto.getAutori()+"\" } },";
            shouldBe = true;
        }

        if(dto.isFrazaSadrzaj()){
            fraza = "match_phrase";
        }else{
            fraza = "match";
        }
        if(dto.isSadrzajOznacen() && !dto.getTekst().equals("")){
            must += "{ \"" + fraza + "\" : { \"tekst\" : \""+dto.getTekst()+"\" } },";
            mustBe = true;

        }else if(!dto.isSadrzajOznacen() && !dto.getTekst().equals("")){
            should += "{ \"" + fraza + "\" : { \"tekst\" : \""+dto.getTekst()+"\" } },";
            shouldBe = true;
        }
        must = must.substring(0,  must.length() - 1);
        should = should.substring(0,should.length()-1);

        must += "],";
        should += "],";

        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\" : {\n";
        if(mustBe){
            query += must;
        }

        if(shouldBe){
            query += should;
        }

        query += "         \"boost\" : 1.0\n" +
                        "    }\n" +
                        "  },\n" +
                        "\"highlight\" : {\n" +
                        "        \"fields\" : {\n" +
                        "            \"naslovCasopisa\" : {" +
                        "               \t\"type\":\"plain\"\n" +
                        "},\n" +
                        "        \"naslov\" : {" +
                        "           \t\"type\":\"plain\"\n" +
                        "},\n" +
                        "        \"kljucniPojmovi\" : {" +
                        "             \t\"type\":\"plain\"\n" +
                        "},\n" +
                        "       \"tekst\" : {" +
                        "               \t\"type\":\"plain\"\n" +
                        "},\n" +
                        "        \"autori\" : {" +
                        "               \t\"type\":\"plain\"\n" +
                        "}\n" +
                        "        }\n" +
                        "    }"+
                        "}";


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);

        String resourceUrl = "http://localhost:9200/casopis/rad/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<RadESDTO> retVal = getRadESDTOAdvanced(locatedNode, korisnik);

        return new ResponseEntity<>(retVal, HttpStatus.OK);

    }

    @GetMapping(path = "/naucneOblasti/{procesId}", produces = "application/json")
    public @ResponseBody ResponseEntity naucneOblasti(@PathVariable String procesId) {

        System.out.println("usao u metodu pretraga/naucneOblasti");

        Integer radId = (Integer) runtimeService.getVariable(procesId, "radId");
        System.out.println("radId: " + radId);

        Rad rad = radService.findOneById(radId);
        List<NaucnaOblast> oblasti = new ArrayList<>();
        oblasti.add(rad.getNaucnaOblast());
        List<Korisnik> recenzenti = korisnikService.findAllByNaucneOblasti(oblasti, "recenzenti");

        List<RecenzentDTO> retVal = new ArrayList<>();

        for(Korisnik recenzent: recenzenti){
            RecenzentDTO recenzentDTO = new RecenzentDTO();
            recenzentDTO.setId(recenzent.getId());
            recenzentDTO.setName(recenzent.getIme() + " " + recenzent.getPrezime());

            retVal.add(recenzentDTO);
        }

        return new ResponseEntity(retVal,  HttpStatus.OK);

    }

    @GetMapping(path = "/all/{procesId}", produces = "application/json")
    public @ResponseBody ResponseEntity all(@PathVariable String procesId) {

        System.out.println("usao u metodu pretraga/all");

        List<Korisnik> recenzenti = korisnikService.findAllRecenzenti();

        List<RecenzentDTO> retVal = new ArrayList<>();

        for(Korisnik recenzent: recenzenti){
            RecenzentDTO recenzentDTO = new RecenzentDTO();
            recenzentDTO.setId(recenzent.getId());
            recenzentDTO.setName(recenzent.getIme() + " " + recenzent.getPrezime());

            retVal.add(recenzentDTO);
        }

        return new ResponseEntity(retVal,  HttpStatus.OK);

    }

    @GetMapping(path = "/geoSpacing/{procesId}", produces = "application/json")
    public @ResponseBody ResponseEntity getReviewersByLocation(@PathVariable String procesId) throws Exception{

        Integer radId = (Integer) runtimeService.getVariable(procesId, "radId");
        System.out.println("radId: " + radId);

        Rad rad = radService.findOneById(radId);
        Korisnik autor = rad.getAutor();
        GeoPoint geoPoint = new GeoPoint(autor.getLatitude(), autor.getLongitude());

        List<Korisnik> koautori = rad.getKoautori();
        Double lat = autor.getLatitude();
        Double lon = autor.getLongitude();
        System.out.println("autor latitude: " + lat + "; longitude: " + lon);

        String query="{\n" +
                "    \"query\": {\n" +
                "        \"bool\" : {\n" +
                "            \"must\" : {\n" +
                "                \"match_all\" : {}\n" +
                "            },\n" +
                "            \"filter\" : {\n" +
                "            \"bool\" : {\n"+
                "               \"must_not\" : {\n"+
                "                \"geo_distance\" : {\n" +
                "                    \"distance\" : \"50km\",\n" +
                "                    \"location\" : {\n" +
                "                        \"lat\" :" + lat + ",\n" +
                "                        \"lon\" :" + lon + "\n" +
                "                    }\n" +
                "                }\n" +
                "               }\n"+
                "              }\n"+
                "            }\n" +
                "        }\n" +
                "    }\n"+
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);

        String resourceUrl = "http://localhost:9200/recenzenti/recenzent/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        List<RecenzentDTO> retVal = this.getRecenzentiFromResponse(rootNode);  //dobijem recenzente koji nisu u okolini autora

        System.out.println("recenzenti size: " + retVal.size());
        for(RecenzentDTO temp: retVal){
            System.out.println(temp.getName());
        }

        //potrebno je izbaciti recenzente koji su u okolini koautora
        for(Korisnik koautor: koautori){
            System.out.println("koautor: " + koautor.getId());
            Double latKoautor = koautor.getLatitude();
            Double lonKoautor = koautor.getLongitude();
            String query1="{\n" +
                    "    \"query\": {\n" +
                    "        \"bool\" : {\n" +
                    "            \"must\" : {\n" +
                    "                \"match_all\" : {}\n" +
                    "            },\n" +
                    "            \"filter\" : {\n" +
                    "                \"geo_distance\" : {\n" +
                    "                    \"distance\" : \"10km\",\n" +
                    "                    \"location\" : {\n" +
                    "                        \"lat\" : " + latKoautor + ",\n" +
                    "                        \"lon\" : " + lonKoautor + "\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";

            ObjectMapper objectMapper1 = new ObjectMapper();
            JsonNode jsonquery1 = objectMapper.readTree(query1);
            HttpEntity<JsonNode> request1 = new HttpEntity<>(jsonquery1);
            String resourceUrl1 = "http://localhost:9200/recenzenti/recenzent/_search?pretty";

            ResponseEntity<String> response1 = restTemplate.postForEntity(resourceUrl1, request1, String.class);
            JsonNode rootNode1 = objectMapper1.readTree(response1.getBody());
            List<RecenzentDTO> recenzentiFromResponse = this.getRecenzentiFromResponse(rootNode1);

            System.out.println("recenzenti from response size: " + recenzentiFromResponse.size());
            for(RecenzentDTO pom: recenzentiFromResponse){
                System.out.println(pom.getName());
            }

            Iterator<RecenzentDTO> iter = retVal.iterator();
            while(iter.hasNext()){
                RecenzentDTO rvRecDTO = iter.next();
                for(RecenzentDTO recDTO: recenzentiFromResponse){
                    if(recDTO.getId() == rvRecDTO.getId() || recDTO.getId().equals(rvRecDTO.getId())){
                        System.out.println("brisanje: " + rvRecDTO.getName());
                        iter.remove();
                    }
                }
            }
        }

        return new ResponseEntity(retVal, HttpStatus.OK);
    }

    @GetMapping(path="/moreLikeThis/{procesId}", produces = "application/json")
    public @ResponseBody ResponseEntity moreLikeThis(@PathVariable String procesId, HttpServletRequest httpRequest) throws  Exception{

        List<RecenzentDTO> retVal = new ArrayList<>();

        String username = Utils.getUsernameFromRequest(httpRequest, tokenUtils);
        System.out.println("username: " + username);
        Korisnik korisnik = korisnikService.findOneByUsername(username);

        Integer radId = (Integer) runtimeService.getVariable(procesId, "radId");
        System.out.println("radId: " + radId);

        Rad rad = radService.findOneById(radId);

        String tekst = radESService.parsePDF(rad);
        System.out.println("naslov rada: " + rad.getPdfName());
        tekst = StringEscapeUtils.escapeJson(tekst);

        String query = "{\n" +
                "    \"query\": {\n" +
                "        \"more_like_this\" : {\n" +
                "            \"fields\" : [\"tekst\"],\n" +
                "            \"like\" : \"" + tekst + "\",\n" +
                "            \"min_term_freq\" : 5,\n" +
                "            \"max_query_terms\" : 50,\n" +
                "            \"min_doc_freq\": 2\n" +
                "        }\n" +
                "    }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonquery = objectMapper.readTree(query);
        HttpEntity<JsonNode> request = new HttpEntity<>(jsonquery);

        String resourceUrl = "http://localhost:9200/casopis/rad/_search?pretty";
        ResponseEntity<String> response = restTemplate.postForEntity(resourceUrl, request, String.class);
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode locatedNode = rootNode.path("hits").path("hits");
        List<RadESDTO> radovi = getRadESDTO(locatedNode, "tekst", korisnik);

        for(RadESDTO dto: radovi){
            Rad tempRad = this.radService.findOneById(dto.getId());
            System.out.println("slican rad: " + tempRad.getId());

            for(Korisnik recenzent: tempRad.getRecenzenti()){

                System.out.println("recenzent: " + recenzent.getUsername());

                RecenzentDTO recenzentDTO = new RecenzentDTO();
                recenzentDTO.setId(recenzent.getId());
                recenzentDTO.setName(recenzent.getIme() + " " + recenzent.getPrezime());

                boolean postoji = false;
                for(RecenzentDTO recPostoji: retVal){
                    if(recPostoji.getName().equals(recenzentDTO.getName())){
                        postoji = true;
                        break;
                    }
                }

                if(!postoji) {
                    retVal.add(recenzentDTO);
                }
            }
        }

        return new ResponseEntity(retVal, HttpStatus.OK);
    }



    public List<RadESDTO> getRadESDTO(JsonNode node, String highlight, Korisnik korisnik){

        List<RadESDTO> retVal = new ArrayList<>();

        for(int i=0; i<node.size(); i++){

            RadESDTO radESDTO = new RadESDTO();

            Integer radId = Integer.parseInt(node.get(i).path("_source").path("radId").asText());
            Rad rad = radService.findOneById(radId);

            radESDTO.setId(radId);
            radESDTO.setNaslov(node.get(i).path("_source").path("naslov").asText());
            radESDTO.setNaslovCasopisa(node.get(i).path("_source").path("naslovCasopisa").asText());
            radESDTO.setApstrakt(node.get(i).path("_source").path("apstrakt").asText());
            radESDTO.setKljucniPojmovi(node.get(i).path("_source").path("kljucniPojmovi").asText());
            radESDTO.setNaucnaOblast(node.get(i).path("_source").path("naucnaOblast").asText());
            radESDTO.setAutori(node.get(i).path("_source").path("autori").asText());

            boolean kupljen = false;

            for(Korisnik k : rad.getKorisniciPlatili()){
                if(korisnik.getId().equals(k.getId())){
                    System.out.println("kupljen rad: " + rad.getId());
                    kupljen = true;
                    break;
                }
            }

            if(rad.getCasopis().getNaplataClanarine().equals(NaplacujeClanarina.NAPLATA_AUTORIMA)){
                System.out.println("za rad: " + rad.getId() + " casopis je open-access");
                radESDTO.setOpenAccess(true);
            }else if(kupljen){
                radESDTO.setOpenAccess(true);
            }else{
                radESDTO.setOpenAccess(false);
            }

            String highText = "";
            for(int j=0; j<node.get(i).path("highlight").path(highlight).size(); j++){
                highText += node.get(i).path("highlight").path(highlight).get(j).asText() + "...";
            }

            radESDTO.setHighlight(highText);
            retVal.add(radESDTO);
        }
        return retVal;

    }

    public List<RadESDTO> getRadESDTOAdvanced(JsonNode node, Korisnik korisnik){

        List<RadESDTO> retVal=new ArrayList<>();

        for(int i=0;i<node.size();i++){

            RadESDTO radESDTO = new RadESDTO();

            Integer radId = Integer.parseInt(node.get(i).path("_source").path("radId").asText());
            Rad rad = radService.findOneById(radId);

            radESDTO.setId(radId);
            radESDTO.setNaslov(node.get(i).path("_source").path("naslov").asText());
            radESDTO.setNaslovCasopisa(node.get(i).path("_source").path("naslovCasopisa").asText());
            radESDTO.setApstrakt(node.get(i).path("_source").path("apstrakt").asText());
            radESDTO.setKljucniPojmovi(node.get(i).path("_source").path("kljucniPojmovi").asText());
            radESDTO.setNaucnaOblast(node.get(i).path("_source").path("naucnaOblast").asText());
            radESDTO.setAutori(node.get(i).path("_source").path("autori").asText());

            boolean kupljen = false;

            for(Korisnik k : rad.getKorisniciPlatili()){
                if(korisnik.getId().equals(k.getId())){
                    System.out.println("kupljen rad: " + rad.getId());
                    kupljen = true;
                    break;
                }
            }

            if(rad.getCasopis().getNaplataClanarine().equals(NaplacujeClanarina.NAPLATA_AUTORIMA)){
                System.out.println("za rad: " + rad.getId() + " casopis je open-access");
                radESDTO.setOpenAccess(true);
            }else if(kupljen){
                radESDTO.setOpenAccess(true);
            }else{
                radESDTO.setOpenAccess(false);
            }


            String highText = node.get(i).path("highlight").toString();

            highText = highText.replace("\"","");
            highText = highText.replace("{","...");
            highText = highText.replace("}","...");
            highText = highText.replace("["," ");
            highText = highText.replace("]"," ");
            highText = highText.replace("\\r\\n","...");
            radESDTO.setHighlight(highText);

            retVal.add(radESDTO);
        }
        return retVal;

    }

    public List<RecenzentDTO> getRecenzentiFromResponse(JsonNode rootNode){

        List<RecenzentDTO> reviewers=new ArrayList<>();
        JsonNode locatedNode = rootNode.path("hits").path("hits");

        for(int i=0;i<locatedNode.size();i++){
            RecenzentDTO recenzentDTO = new RecenzentDTO();
            Integer recenzentId = Integer.parseInt(locatedNode.get(i).path("_source").path("recenzentId").asText());
            recenzentDTO.setId(recenzentId);
            recenzentDTO.setName(locatedNode.get(i).path("_source").path("ime").asText() + " " + locatedNode.get(i).path("_source").path("prezime").asText());

            reviewers.add(recenzentDTO);
        }

        return reviewers;
    }

}

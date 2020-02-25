package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.Rad;
import com.example.naucnacentrala.repository.RadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RadService {

    private final String path = "D:\\Milica\\fakultet\\master\\UPP\\naucna_centrala\\naucna-centrala\\src\\main\\resources\\files";
    private final Path storageLocation = Paths.get(this.path);

    @Autowired
    private RadRepository radRepository;

    public Rad findOneById(Integer id){ return radRepository.findOneById(id); }

    public List<Rad> findAll(){
        return radRepository.findAll();
    }

    public Rad save(Rad rad){
        return radRepository.save(rad);
    }

    public Rad savePdf(MultipartFile file, Rad rad){
        try {
            rad.setPdf(file.getBytes());
            rad.setPdfName(file.getOriginalFilename());
            // sacuvaj pdf u resources folderu
            Files.deleteIfExists(this.storageLocation.resolve(rad.getPdfName()));
            Files.copy(file.getInputStream(), this.storageLocation.resolve(rad.getPdfName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return radRepository.save(rad);
    }

    public String getPath(Integer id) throws UnsupportedEncodingException {
        Rad rad = radRepository.findOneById(id);
        String ret = this.path + "\\" + rad.getPdfName();
        System.out.println("path: " + ret);
        return ret;
    }
}

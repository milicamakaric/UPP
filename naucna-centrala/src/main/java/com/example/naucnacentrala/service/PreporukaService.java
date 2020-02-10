package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.Preporuka;
import com.example.naucnacentrala.repository.PreporukaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreporukaService {

    @Autowired
    private PreporukaRepository preporukaRepository;

    public Preporuka findOneById(Integer id){ return preporukaRepository.findOneById(id); }

    public List<Preporuka> findAll(){
        return preporukaRepository.findAll();
    }

    public Preporuka save(Preporuka preporuka){
        return preporukaRepository.save(preporuka);
    }
}

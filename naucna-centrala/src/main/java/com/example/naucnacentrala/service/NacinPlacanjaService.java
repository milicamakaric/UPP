package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.NacinPlacanja;
import com.example.naucnacentrala.repository.NacinPlacanjaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NacinPlacanjaService {

    @Autowired
    private NacinPlacanjaRepository nacinPlacanjaRepository;

    public List<NacinPlacanja> findAll(){
        return nacinPlacanjaRepository.findAll();
    }

}

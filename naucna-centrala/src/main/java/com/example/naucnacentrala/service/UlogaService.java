package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.Uloga;
import com.example.naucnacentrala.repository.UlogaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UlogaService {

    @Autowired
    private UlogaRepository ulogaRepository;

    public Uloga findOneById(Integer id){
        return ulogaRepository.findOneById(id);
    }

    public List<Uloga> findAll(){
        return ulogaRepository.findAll();
    }

    public Uloga save(Uloga uloga){
        return ulogaRepository.save(uloga);
    }

    public void remove(Integer id){
        ulogaRepository.deleteById(id);
    }

    public Uloga findOneByNaziv(String naziv){
        return ulogaRepository.findOneByNaziv(naziv);
    }
}

package com.example.naucnacentrala.service;

import com.example.naucnacentrala.model.RecenzentES;
import com.example.naucnacentrala.repository.RecenzentESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecenzentESService {

    @Autowired
    private RecenzentESRepository recenzentESRepository;

    public RecenzentES save(RecenzentES recenzentES){
        return recenzentESRepository.save(recenzentES);
    }

    public RecenzentES findOneById(Integer id){
        return recenzentESRepository.findOneById(id);
    }
}

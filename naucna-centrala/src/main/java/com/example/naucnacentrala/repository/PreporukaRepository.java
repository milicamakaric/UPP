package com.example.naucnacentrala.repository;

import com.example.naucnacentrala.model.Preporuka;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreporukaRepository extends JpaRepository<Preporuka, Integer> {

    Preporuka findOneById(Integer id);
}

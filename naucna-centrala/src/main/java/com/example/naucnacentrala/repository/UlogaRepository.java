package com.example.naucnacentrala.repository;

import com.example.naucnacentrala.model.Uloga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UlogaRepository extends JpaRepository<Uloga, Integer> {

    Uloga findOneById(Integer id);
    Uloga findOneByNaziv(String naziv);
}

package com.example.naucnacentrala.repository;

import com.example.naucnacentrala.model.RecenzentES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecenzentESRepository extends ElasticsearchRepository<RecenzentES, Integer> {

    RecenzentES findOneById(Integer id);
}

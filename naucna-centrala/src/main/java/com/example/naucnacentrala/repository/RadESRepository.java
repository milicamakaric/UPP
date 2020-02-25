package com.example.naucnacentrala.repository;

import com.example.naucnacentrala.model.RadES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RadESRepository extends ElasticsearchRepository<RadES, Integer> {

    RadES findOneById(Integer id);
}

package com.example.naucnacentrala.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "recenzenti", type = "recenzent")
public class RecenzentES {

    @Id
    private Integer id;

    @Field(type= FieldType.Integer)
    private Integer recenzentId;

    @Field(type = FieldType.Text)
    private String ime;

    @Field(type = FieldType.Text)
    private String prezime;

    @GeoPointField
    private GeoPoint location;

    public RecenzentES() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecenzentId() {
        return recenzentId;
    }

    public void setRecenzentId(Integer recenzentId) {
        this.recenzentId = recenzentId;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}

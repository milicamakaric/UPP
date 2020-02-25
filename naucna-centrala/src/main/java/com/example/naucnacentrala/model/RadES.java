package com.example.naucnacentrala.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "casopis", type = "rad")
public class RadES {

    @Id
    private Integer id;

    @Field(type= FieldType.Integer)
    private Integer radId;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String naslov;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String naslovCasopisa;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String apstrakt;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String kljucniPojmovi;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String naucnaOblast;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String pdfLokacija;

    @Field(type = FieldType.Text, searchAnalyzer = "serbian",analyzer = "serbian")
    private String tekst;

    @Field(type = FieldType.Text, store = true)
    private String autori;

    public RadES() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRadId() {
        return radId;
    }

    public void setRadId(Integer radId) {
        this.radId = radId;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getNaslovCasopisa() {
        return naslovCasopisa;
    }

    public void setNaslovCasopisa(String naslovCasopisa) {
        this.naslovCasopisa = naslovCasopisa;
    }

    public String getApstrakt() {
        return apstrakt;
    }

    public void setApstrakt(String apstrakt) {
        this.apstrakt = apstrakt;
    }

    public String getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi(String kljucniPojmovi) {
        this.kljucniPojmovi = kljucniPojmovi;
    }

    public String getNaucnaOblast() {
        return naucnaOblast;
    }

    public void setNaucnaOblast(String naucnaOblast) {
        this.naucnaOblast = naucnaOblast;
    }

    public String getPdfLokacija() {
        return pdfLokacija;
    }

    public void setPdfLokacija(String pdfLokacija) {
        this.pdfLokacija = pdfLokacija;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        if(tekst == null){
            this.tekst = "";
        }
        this.tekst = tekst;
    }

    public String getAutori() { return autori; }

    public void setAutori(String autori) { this.autori = autori; }
}

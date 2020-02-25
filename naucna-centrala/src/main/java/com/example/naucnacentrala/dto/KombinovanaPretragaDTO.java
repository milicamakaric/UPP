package com.example.naucnacentrala.dto;

public class KombinovanaPretragaDTO {

    private String naslovCasopisa;
    private String naslov;
    private String autori;
    private String kljucniPojmovi;
    private String tekst;
    private String naucnaOblast;
    private boolean nazivCasopisaOznacen;
    private boolean naslovRadaOznacen;
    private boolean autoriOznaceni;
    private boolean kljucniPojmoviOznaceni;
    private boolean sadrzajOznacen;

    public KombinovanaPretragaDTO() {
    }

    public String getNaslovCasopisa() {
        return naslovCasopisa;
    }

    public void setNaslovCasopisa(String naslovCasopisa) {
        this.naslovCasopisa = naslovCasopisa;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getAutori() {
        return autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public String getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi(String kljucniPojmovi) {
        this.kljucniPojmovi = kljucniPojmovi;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getNaucnaOblast() {
        return naucnaOblast;
    }

    public void setNaucnaOblast(String naucnaOblast) {
        this.naucnaOblast = naucnaOblast;
    }

    public boolean isNazivCasopisaOznacen() {
        return nazivCasopisaOznacen;
    }

    public void setNazivCasopisaOznacen(boolean nazivCasopisaOznacen) {
        this.nazivCasopisaOznacen = nazivCasopisaOznacen;
    }

    public boolean isNaslovRadaOznacen() {
        return naslovRadaOznacen;
    }

    public void setNaslovRadaOznacen(boolean naslovRadaOznacen) {
        this.naslovRadaOznacen = naslovRadaOznacen;
    }

    public boolean isAutoriOznaceni() {
        return autoriOznaceni;
    }

    public void setAutoriOznaceni(boolean autoriOznaceni) {
        this.autoriOznaceni = autoriOznaceni;
    }

    public boolean isKljucniPojmoviOznaceni() {
        return kljucniPojmoviOznaceni;
    }

    public void setKljucniPojmoviOznaceni(boolean kljucniPojmoviOznaceni) {
        this.kljucniPojmoviOznaceni = kljucniPojmoviOznaceni;
    }

    public boolean isSadrzajOznacen() {
        return sadrzajOznacen;
    }

    public void setSadrzajOznacen(boolean sadrzajOznacen) {
        this.sadrzajOznacen = sadrzajOznacen;
    }
}

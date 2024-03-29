package com.example.naucnacentrala.model;

import com.example.naucnacentrala.utils.StringListConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Rad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String naslov;

    @Column
    private String apstrakt;

    @Column
    private double cena;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NaucnaOblast naucnaOblast;

    @Column
    private String pdfLokacija;

    @Column
    private String pdfName;

    @Column
    private boolean prihvacen;

    @Column(name = "pdf")
    @Lob
    private byte[] pdf;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id", nullable = false)
    private Korisnik autor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Korisnik> koautori = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Korisnik> recenzenti = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> kljucniPojmovi;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Casopis casopis;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Korisnik> korisniciPlatili = new ArrayList<>();

    public Rad() {
    }

    public Rad(String naslov, String apstrakt, double cena, NaucnaOblast naucnaOblast, String pdfLokacija, boolean prihvacen) {
        this.naslov = naslov;
        this.apstrakt = apstrakt;
        this.cena = cena;
        this.naucnaOblast = naucnaOblast;
        this.pdfLokacija = pdfLokacija;
        this.prihvacen = prihvacen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getApstrakt() {
        return apstrakt;
    }

    public void setApstrakt(String apstrakt) {
        this.apstrakt = apstrakt;
    }

    public double getCena() { return cena; }

    public void setCena(double cena) { this.cena = cena; }

    public NaucnaOblast getNaucnaOblast() {
        return naucnaOblast;
    }

    public void setNaucnaOblast(NaucnaOblast naucnaOblast) {
        this.naucnaOblast = naucnaOblast;
    }

    public String getPdfLokacija() {
        return pdfLokacija;
    }

    public void setPdfLokacija(String pdfLokacija) {
        this.pdfLokacija = pdfLokacija;
    }

    public String getPdfName() { return pdfName; }

    public void setPdfName(String pdfName) { this.pdfName = pdfName; }

    public boolean isPrihvacen() {
        return prihvacen;
    }

    public void setPrihvacen(boolean prihvacen) {
        this.prihvacen = prihvacen;
    }

    public byte[] getPdf() { return pdf; }

    public void setPdf(byte[] pdf) { this.pdf = pdf; }

    public Korisnik getAutor() { return autor; }

    public void setAutor(Korisnik autor) { this.autor = autor; }

    public List<Korisnik> getKoautori() { return koautori; }

    public void setKoautori(List<Korisnik> koautori) {
        this.koautori = koautori;
    }

    public List<Korisnik> getRecenzenti() { return recenzenti; }

    public void setRecenzenti(List<Korisnik> recenzenti) { this.recenzenti = recenzenti; }

    public List<String> getKljucniPojmovi() {
        return kljucniPojmovi;
    }

    public void setKljucniPojmovi(List<String> kljucniPojmovi) {
        this.kljucniPojmovi = kljucniPojmovi;
    }

    public Casopis getCasopis() { return casopis; }

    public void setCasopis(Casopis casopis) { this.casopis = casopis; }

    public List<Korisnik> getKorisniciPlatili() { return korisniciPlatili; }

    public void setKorisniciPlatili(List<Korisnik> korisniciPlatili) { this.korisniciPlatili = korisniciPlatili; }
}

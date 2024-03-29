package com.example.naucnacentrala.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Casopis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String naziv;

    @Column
    private String issn;

    @Column
    private double cena;

    @Column
    @Enumerated(EnumType.STRING)
    private NaplacujeClanarina naplataClanarine;

    @Column
    private Boolean aktiviran;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<NaucnaOblast> naucneOblasti = new ArrayList<NaucnaOblast>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "glavni_urednik_id", nullable = false)
    private Korisnik glavniUrednik;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Korisnik> urednici = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private List<Korisnik> recenzenti = new ArrayList<>();

    @OneToMany(mappedBy = "casopis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rad> radovi = new ArrayList<>();

    @OneToMany(mappedBy = "casopis", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> pretplate = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Korisnik> korisniciPlatili = new ArrayList<>();

    public Casopis() {
    }

    public Casopis(String naziv, String issn, double cena, NaplacujeClanarina naplataClanarine, Boolean aktiviran) {
        this.naziv = naziv;
        this.issn = issn;
        this.cena = cena;
        this.naplataClanarine = naplataClanarine;
        this.aktiviran = aktiviran;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public double getCena() { return cena; }

    public void setCena(double cena) { this.cena = cena; }

    public NaplacujeClanarina getNaplataClanarine() {
        return naplataClanarine;
    }

    public void setNaplataClanarine(NaplacujeClanarina naplataClanarine) {
        this.naplataClanarine = naplataClanarine;
    }

    public Boolean getAktiviran() {
        return aktiviran;
    }

    public void setAktiviran(Boolean aktiviran) {
        this.aktiviran = aktiviran;
    }

    public List<NaucnaOblast> getNaucneOblasti() {
        return naucneOblasti;
    }

    public void setNaucneOblasti(List<NaucnaOblast> naucneOblasti) {
        this.naucneOblasti = naucneOblasti;
    }

    public Korisnik getGlavniUrednik() {
        return glavniUrednik;
    }

    public void setGlavniUrednik(Korisnik glavniUrednik) {
        this.glavniUrednik = glavniUrednik;
    }

    public List<Korisnik> getUrednici() {
        return urednici;
    }

    public void setUrednici(List<Korisnik> urednici) {
        this.urednici = urednici;
    }

    public List<Korisnik> getRecenzenti() {
        return recenzenti;
    }

    public void setRecenzenti(List<Korisnik> recenzenti) {
        this.recenzenti = recenzenti;
    }

    public List<Rad> getRadovi() {
        return radovi;
    }

    public void setRadovi(List<Rad> radovi) {
        this.radovi = radovi;
    }

    public List<Korisnik> getKorisniciPlatili() {
        return korisniciPlatili;
    }

    public void setKorisniciPlatili(List<Korisnik> korisniciPlatili) { this.korisniciPlatili = korisniciPlatili; }

    public List<Subscription> getPretplate() {
        return pretplate;
    }

    public void setPretplate(List<Subscription> pretplate) {
        this.pretplate = pretplate;
    }

}

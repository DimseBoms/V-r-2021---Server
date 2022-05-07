package com.example.eksamenchat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.net.InetAddress;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RomTilTab {

    private SimpleIntegerProperty id = new SimpleIntegerProperty(this, "id");
    //   private SimpleDateFormat tidspunkt;
    //   private SimpleStringProperty tidspunkt;
    private Date tidspunkt;
    private SimpleStringProperty brukernavn;
    private SimpleStringProperty klientIP;
    private SimpleStringProperty rom;
    private SimpleStringProperty melding;
    private ArrayList<Logg> nyesteLogg = new ArrayList<>(); // er det nødvendig med instansiering her?


    public RomTilTab(int nyId, Date tidspunkt, String bruker, String kliIP, String aktivtRom, String nyMelding) {
        this.id.set(nyId);
        //    long millis = System.currentTimeMillis();
        //   java.sql.Date testDato = new java.sql.Date(millis);
        this.tidspunkt = tidspunkt;
        this.brukernavn = new SimpleStringProperty(bruker);
        this.klientIP = new SimpleStringProperty(kliIP);
        this.rom = new SimpleStringProperty(aktivtRom);
        this.melding = new SimpleStringProperty(nyMelding);

        //   Adaptor.insertLogg(getTidspunkt(), getBrukernavn(), getKlientIP(), getRom(), getMelding());
    }

    public RomTilTab(int nyId, String bruker, String kliIP) {
        this.id.set(nyId);
        this.brukernavn = new SimpleStringProperty(bruker);
        this.klientIP = new SimpleStringProperty(kliIP);

    }


    public RomTilTab(int nyId, String bruker, String kliIP, String aktivtRom) {
        this.id.set(nyId);
        this.brukernavn = new SimpleStringProperty(bruker);
        this.klientIP = new SimpleStringProperty(kliIP);
        this.rom = new SimpleStringProperty(aktivtRom);

    }


    public final IntegerProperty idProperty() {return this.id;}

    public final int getId() {return this.idProperty().get();}

    public final void setId(final int id) {this.idProperty().set(id);}

    public Date getTidspunkt() {return tidspunkt;}

    public void setTidspunkt(Date tid) {this.tidspunkt = tid;}

    public String getBrukernavn() {
        return brukernavn.get();
    }

    public void setBrukernavn(String bruker) {
        brukernavn.set(bruker);
    }

    public String getKlientIP() {
        return klientIP.get();
    }

    public void setKlientIP(String ip) {
        klientIP.set(ip);
    }

    public String getRom() {
        return rom.get();
    }

    public void setRom(String romnavn) {
        rom.set(romnavn);
    }

    public String getMelding() {
        return melding.get();
    }

    public void setMelding(String nyMelding) {
        melding.set(nyMelding);
    }



    /*
    @Override
    public String toString() {
        return "Logg{" +
                "id=" + id +
                ", tidspunkt=" + tidspunkt +
                ", brukernavn=" + brukernavn +
                ", klientIP=" + klientIP +
                ", rom=" + rom +
                ", melding=" + melding +
                '}';
    }

     */
}
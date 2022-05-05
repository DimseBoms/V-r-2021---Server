package com.example.eksamenchat;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class Rom {
  //  protected int aktivMedlemmer;
 //   final int sisteMld = 5;
   // protected String[] meldinger;
  //  protected String[] ipAdresse;
    protected static ArrayList<Rom> aktiveRom = new ArrayList<>();
    protected ArrayList<String> meldinger = new ArrayList<>();
    protected ArrayList<String> ipAdresse = new ArrayList<>();
  //  protected InetAddress[] ipAdresse;

    public Rom(String romNavn, String ip) {
        this.romNavn = romNavn;
        this.ipAdresse.add(ip);
        this.aktiveRom.add(this);
    }

    public void setMelding(String melding) {
        this.meldinger.add(melding);
    }

    public ArrayList<String> getMeldinger() {return meldinger;}

    public ArrayList<Rom> getAktiveRom() {return aktiveRom;}

    public String getRomNavn() {
        return romNavn;
    }

    protected String romNavn;

    public Rom(String romNavn){}

    public void oppdaterMedlemmer(){}

    @Override
    public String toString() {
        return romNavn + " , " + ipAdresse + "\n";
    }
}

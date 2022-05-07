package com.example.eksamenchat;

import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    protected String brukerNavn;
    static Map<Object, Object> mapOverRom = new HashMap<>();

    public Rom(String romNavn, String ip, String brukerNavn) {
        this.romNavn = romNavn;
        this.ipAdresse.add(ip);
        this.brukerNavn = brukerNavn;
        this.aktiveRom.add(this);
   //     this.mapOverRom.put("rom", this);
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

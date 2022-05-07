package com.example.eksamenchat;

import java.util.ArrayList;

public class Bruker {
    protected static ArrayList<Bruker> brukerliste = new ArrayList<>();
    String navn;
    public Bruker(String navn) {
        this.navn = navn;
        brukerliste.add(this);
    }

    public static boolean sjekkBrukernavnTatt(String brukernavn) {
        for (Bruker b: brukerliste) {
            if (b.navn.equals(brukernavn)) return true;
        }
        return false;
    }
}

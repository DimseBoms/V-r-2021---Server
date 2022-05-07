package com.example.eksamenchat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;


public class RomNy {

    private SimpleIntegerProperty antBrukere = new SimpleIntegerProperty(this, "antBrukere");
    private SimpleStringProperty rom;
   // protected static ArrayList<RomNy> nyesteLogg = new ArrayList<>(); // er det nødvendig med instansiering her?
    protected final static ObservableList<RomNy> DATA_M = FXCollections.observableArrayList();
    private Bruker bruker;
    private int teller = 0;

    public RomNy(String rom, String brukernavn) {
        this.antBrukere.set(teller);
        this.bruker = new Bruker(brukernavn);
        this.rom = new SimpleStringProperty(rom);
        DATA_M.add(this);
    }


    public final IntegerProperty antBrukereProperty() {return this.antBrukere;}

    public final int getId() {return this.antBrukereProperty().get();}

    public final void setId(final int antBrukere) {this.antBrukereProperty().set(antBrukere);}

    public String getRom() {
        return rom.get();
    }

    public void setRom(String romnavn) {
        rom.set(romnavn);
    }

    public void setTeller() {
        this.teller++;
    }

}

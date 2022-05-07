package com.example.eksamenchat;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


public class RomVisning extends VBox {

    private final TableView<RomNy> TABELL_M = new TableView<>();
    private final TableColumn<RomNy, Integer> ANT_BRUKERE =  new TableColumn<>();
    private final TableColumn<RomNy, String> ROMNAVN =       new TableColumn<>();
    protected final static ObservableList<RomNy> DATA_M = RomNy.DATA_M;

    public RomVisning( ) {
        this.setSpacing(5);
        this.setPadding(new Insets(10));
        this.oppfriskTabellMedlem();
        this.getChildren().addAll(TABELL_M);
        this.setStyle("-fx-background-color: dimgray");
    }

    private void oppfriskTabellMedlem() {
        TABELL_M.getColumns().clear();
        TABELL_M.setEditable(true);

        // KOLONNE 1 - ANTALL BRUKERE I HVERT ROM //
        ANT_BRUKERE.setMinWidth(20);
        ANT_BRUKERE.setText("Antall Brukere");
        ANT_BRUKERE.setCellValueFactory(new PropertyValueFactory<RomNy, Integer>("antBrukere"));

        // KOLONNE 5 - ROMNAVN //
        ROMNAVN.setMinWidth(100);
        ROMNAVN.setText("Rom");
        ROMNAVN.setCellValueFactory(new PropertyValueFactory<RomNy, String>("rom"));

        TABELL_M.setItems(DATA_M);
        TABELL_M.getColumns().addAll(ANT_BRUKERE, ROMNAVN);
    } // slutt metode oppfriskTabell


}

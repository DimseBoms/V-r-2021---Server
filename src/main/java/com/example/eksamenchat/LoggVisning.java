package com.example.eksamenchat;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.Date;

public class LoggVisning extends VBox {

    private final TableView<Logg> TABELL_M = new TableView<>();
    private final TableColumn<Logg, Integer> ID =         new TableColumn<>();
    private final TableColumn<Logg, Date> TID     =             new TableColumn<>();
    private final TableColumn<Logg, String> BRUKER =              new TableColumn<>();
    private final TableColumn<Logg, String> KLIENT_IP =           new TableColumn<>();
    private final TableColumn<Logg, String> ROMNAVN =             new TableColumn<>();
    private final TableColumn<Logg, String> MELDING =             new TableColumn<>();
  //  protected final static ObservableList<Logg> DATA_M = FXCollections.observableArrayList();
    protected final static ObservableList<Logg> DATA_M = Adaptor.LOGG_RADER;


    public LoggVisning( ) {
        this.setSpacing(5);
        this.setPadding(new Insets(10));
        this.oppfriskTabellMedlem();
        this.getChildren().addAll(TABELL_M);
        this.setStyle("-fx-background-color: dimgray");
    }

    private void oppfriskTabellMedlem() {
        TABELL_M.getColumns().clear();
        TABELL_M.setEditable(true);

        // KOLONNE 1 - ID - AUTOINKREMENT FRA KLIENT - MÅ AKTIVERES FOR SLETTING//
        ID.setMinWidth(20);
        ID.setText("Id");
        ID.setCellValueFactory(new PropertyValueFactory<Logg, Integer>("id"));

        // KOLONNE 2 - TIDSPUNKT //
        TID.setMinWidth(200);
        TID.setText("Tidspunkt");
        TID.setCellValueFactory(new PropertyValueFactory<Logg, Date>("tidspunkt"));

        // KOLONNE 3 - BRUKERNAVN //
        BRUKER.setMinWidth(100);
        BRUKER.setText("Bruker");
        BRUKER.setCellValueFactory(new PropertyValueFactory<Logg, String>("brukernavn"));

        // KOLONNE 4 - IP ADRESSE //
        KLIENT_IP.setMinWidth(250);
        KLIENT_IP.setText("klientIP");
        KLIENT_IP.setCellValueFactory(new PropertyValueFactory<Logg, String>("klientIP"));

        // KOLONNE 5 - ROMNAVN //
        ROMNAVN.setMinWidth(100);
        ROMNAVN.setText("rom");
        ROMNAVN.setCellValueFactory(new PropertyValueFactory<Logg, String>("rom"));

        // KOLONNE 6 - MELDING //
        MELDING.setMinWidth(150);
        MELDING.setText("melding");
        MELDING.setCellValueFactory(new PropertyValueFactory<Logg, String>("melding"));

        TABELL_M.setItems(DATA_M);
        TABELL_M.getColumns().addAll(ID, TID, BRUKER, KLIENT_IP, ROMNAVN, MELDING);
    } // slutt metode oppfriskTabell


}

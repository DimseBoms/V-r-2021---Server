package com.example.eksamenchat;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;

public class EksamenChat extends Application {

    private static ArrayList<String> outputStreams = new ArrayList<String>();


    public static void main(String[] args) {
        // Lager initiell servertråd
        new Thread(() -> {
            try {
                ServerSocket server = new ServerSocket(8000);
                while (true) {
                    Socket socket = server.accept();
                    // Lager ny tråd for ny tilkobling
                    new Thread(new BehandleKlient(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO");
            }
        }).start();
        launch(args);
    }


    @Override
    public void start(Stage stage) {
    //    Adaptor.dropLogg();
    //    Adaptor.createLogg();
        long millis = System.currentTimeMillis();
        java.sql.Date testDato = new java.sql.Date(millis);

        //  Logg(String bruker, String kliIP, Rom aktivtRom, String nyMelding)
    //    Logg testLogg = new Logg(1, "Tore", "172.10.50.111", "NavnPåRom", "Dette er en lang melding");
  //      Adaptor.insertLogg(1, testDato, "Tore", "172.10.50.111", null, null);
 //       Adaptor.insertLogg(2, testDato, "Tore", "172.10.50.111", "NavnPåRom", null);


    //    Adaptor.selectLoggId(666);
   //     Adaptor.selectLoggId(2);
   //     Adaptor.selectLoggId(3);
        new Rom("testRom1", "ip her", "bruker1");
        new Rom("testRom2", "ip her", "bruker2");

        Adaptor.selectAll();


    //    System.out.println(Adaptor.loggInnføringer);
    //    Rom testrom = new Rom("TestRom", "172.10.50.123");
    //    Rom testrom2 = new Rom("TestRom2", "172.10.50.123");
    //    Rom testrom3 = new Rom("TestRom3", "172.10.50.123");
    //    System.out.println(testrom.getAktiveRom().size());
    //    System.out.println(Rom.aktiveRom);

        VBox root = new VBox();
        Button button = new Button("Manuell oppfriskning");
        root.getChildren().addAll(button, romVisning(), loggVisning());
        button.setOnAction(e -> {
            Adaptor.insertLogg(testDato,"Tore", "172.10.50.111", "NavnPåRom", "Dette er en lang melding");
            /*
            root.getChildren().clear();
            Adaptor.loggInnføringer.clear();
            Adaptor.selectAll();
            root.getChildren().addAll(button, romVisning(), loggVisning());

             */
        });

      //  root.getChildren().add(romVisning());
        Scene scene = new Scene(root, 900, 900);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    private LoggVisning loggVisning() {
        LoggVisning lv = new LoggVisning();
        return lv;
    }


    private HBox romVisning() {
        HBox liste = new HBox();
        ObservableList<String> adresser = FXCollections.observableArrayList(Adaptor.loggInnføringer.toString());
        ListView<String> listView = new ListView<String>(adresser);
        liste.getChildren().add(listView);
        return liste;
    }

    final static String[] ordtak = {
            "Smuler er også brød",
            "Bak skyene er himmelen alltid blå",
            "Nå går de gamle hjem",
            "Arbeidet adler mannen",
            "Man får kalde føtter av å gå på tynn is",
            "Harens veier er uransakelige"};



}


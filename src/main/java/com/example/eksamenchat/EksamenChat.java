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
    private Adaptor adaptor = new Adaptor();
    private static ServerSocket server;
    public static void main(String[] args) {
        // Lager initiell servertråd
        new Thread(() -> {
            try {
                server = new ServerSocket(8000);
                while (!server.isClosed()) {
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
  //     Adaptor.dropLogg();
  //     Adaptor.createLogg();

        new Rom("testRom1", "bruker1");
        new Rom("testRom2", "bruker2");

        new RomNy("testRom2NY1", "NyBruker1");
        new RomNy("testRomNY2", "NyBruker2");

        Adaptor.selectNyeste();

        VBox root = new VBox();
        Button nyLogg = new Button("Lag ny logg");
        Button nyBruker = new Button("Lag ny bruker");
        Button nyRom = new Button("Lag nytt rom");
        root.getChildren().addAll(nyLogg, nyBruker, nyRom, romVisning(), loggVisning());
        nyLogg.setOnAction(e -> {
                    long millis = System.currentTimeMillis();
                    java.sql.Date testDato = new java.sql.Date(millis);
                    adaptor.insertLogg(testDato,"Tore", "172.10.50.111",
                            "NavnPåRom", "Dette er en lang melding");
                });
  //      nyLogg.setOnAction(e -> new Logg (1, "NavnPåRom", "Dette er en lang melding"));
        nyBruker.setOnAction(e -> new Bruker("Dmitriy"));
        nyRom.setOnAction(e -> new RomNy("ManuellRom", "SimulertBruker"));

        Scene scene = new Scene(root, 900, 900);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    // O
    @Override
    public void stop(){
        System.out.println("Lukker intern server");
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Feilmelding ved lukking av server: " + e.getMessage());
        }
    }
    private LoggVisning loggVisning() {
        LoggVisning lv = new LoggVisning();
        return lv;
    }

    private RomVisning romVisning() {
        RomVisning rv = new RomVisning();
        return rv;
    }

}


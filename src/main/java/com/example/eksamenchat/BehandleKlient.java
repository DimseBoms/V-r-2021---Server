package com.example.eksamenchat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class BehandleKlient implements Runnable {
    Socket socket;
    Date tid;
    String navn;
    String ip;
    String rom;
    String melding;
    public BehandleKlient(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Etablerer datastrøm for lesing fra klient og skriving til klient
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            Map input = (Map) in.readObject();
            if (input != null) {
                this.tid = new Date(System.currentTimeMillis());
                this.navn = (String) input.get("brukerNavn");
                this.ip = socket.getInetAddress().getHostAddress();
                this.rom = (String) input.get("rom");
                this.melding = (String) input.get("melding");
                Adaptor.insertLogg(tid, navn, ip, rom, melding);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println();
        }
    }
}

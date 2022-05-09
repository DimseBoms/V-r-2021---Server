import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KlientBehandling implements Runnable {
    private Socket socket;
    private ObjectInputStream innStrøm;
    private ObjectOutputStream utStrøm;

    // Håndterer klient basert på Socket objektet som blir innsendt
    public KlientBehandling(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.innStrøm = new ObjectInputStream(socket.getInputStream());
            this.utStrøm = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            // Forsøker å lukke tilkobling
            socket.close();
            closeEverything(socket, innStrøm, utStrøm);
        }
    }

    // Metode som kjører klientbehandling i ny tråd
    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                // Leser hva slags type forespørsel og bestemmer hva slags svar som skal konstrueres
                // TODO: Fyll inn logikk her
                HashMap<Object, Object> forespørselMap = (HashMap<Object, Object>) innStrøm.readObject();
                if (forespørselMap.get("query").equals("loggInn")) {
                    System.out.println("Nytt innloggingsforsøk på " + forespørselMap);
                    if (sjekkBruker()){
                        loggInnSuksess();
                    } else loggInnFeil();
                }
            } catch (IOException e) {
                // Forsøker å lukke tilkobling
                closeEverything(socket, innStrøm, utStrøm);
                break;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loggInnFeil() throws IOException {
        HashMap<Object, Object> svar = new HashMap<>();
        svar.put("suksess", 0);
        svar.put("melding", "Feil ved autentisering. Brukernavn eller passord er feil");
        try {
            utStrøm.writeObject(svar);
        } catch (IOException e) {
            utStrøm.close();
        }
    }

    private void loggInnSuksess() throws IOException {
        HashMap<Object, Object> svar = new HashMap<>();
        svar.put("suksess", 1);
        svar.put("melding", "Bruker logget inn");
        try {
            utStrøm.writeObject(svar);
        } catch (IOException e) {
            utStrøm.close();
        }
    }

    private boolean sjekkBruker() {
        // TODO: Ta i bruk sjekk fra DBAdaptor
        return true;
    }

    // Hjelpemetode for å lukke alle strømmene
    public void closeEverything(Socket socket, ObjectInputStream innStrøm, ObjectOutputStream utStrøm) {
        try {
            if (utStrøm != null) {
                utStrøm.close();
            }
            if (innStrøm != null) {
                innStrøm.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class KlientBehandling implements Runnable {
    private Socket socket;
    private ObjectInputStream innStrøm;
    private ObjectOutputStream utStrøm;
    protected DBAdaptor dbAdaptor = new DBAdaptor();

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
                    sjekkBruker(forespørselMap);
                }

                if(forespørselMap.get("query").equals("sjekkRekke")){
                    System.out.println("sender rekke");
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

    private void loggInnFeil() {
        HashMap<Object, Object> svar = new HashMap<>();
        svar.put("feilkode", -1);
        svar.put("melding", "Feil ved autentisering. Telefonnummer eller epost er allerede i bruk");
        try {
            utStrøm.writeObject(svar);
        } catch (IOException e) {
            try {
                utStrøm.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void loggInnSuksess(String melding) {
        HashMap<Object, Object> svar = new HashMap<>();
        svar.put("feilkode", 0);
        svar.put("melding", melding);
        try {
            utStrøm.writeObject(svar);
        } catch (IOException e) {
            try {
                utStrøm.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void lagBruker(HashMap map) {
        // Lager ny brukerkonto og setter den inn i databasen.
        dbAdaptor.insertBruker((String) map.get("fornavn"), (String) map.get("etternavn"),
                (String) map.get("tlf"), (String) map.get("epost"));
        // Varsler bruker om at det har blitt laget en ny brukerkonto som følge av innlogging
        loggInnSuksess("Laget ny bruker. Velkommen " + map.get("fornavn") + "!");
    }

    // For å logge inn skal det være en fullstendig match mellom epost og tlf. Dersom enten epost eller tlf
    // er tatt i bruk fra før så må det være en match mellom disse for suksessfull innlogging.
    private void sjekkBruker(HashMap map) {
        // Leser inn verdier fra map
        String innEpost = (String) map.get("epost");
        String innTlf = (String) map.get("tlf");
        // Teller antall/sjekker om verdien allerede eksisterer i databasen
        dbAdaptor.selectEpost(map.get("epost").toString());
        dbAdaptor.selectTelefonnummer(map.get("tlf").toString());
        if (dbAdaptor.isEpostEksisterer() || dbAdaptor.isNrEksisterer()) {
            // Sjekker samsvar mellom tlf og epost via epost
            if (dbAdaptor.sjekkSamsvar(innEpost, innTlf)) {
                // Det er samsvar mellom epost og tlf
                loggInnSuksess("Bruker logget inn");
            } else {
                // Den ene eller andre eksisterer men det er ikke samsvar
                loggInnFeil();
            }
        } else {
            // Hverken epost eller tlf eksisterer
            lagBruker(map);
        }
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

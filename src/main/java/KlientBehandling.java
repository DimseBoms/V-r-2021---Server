import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KlientBehandling implements Runnable {
    private Socket socket;
    private ObjectInputStream innStrøm;
    private ObjectOutputStream utStrøm;
    //legg i konstruktør
    protected DBAdaptor dbAdaptor = new DBAdaptor();
    private Bruker bruker;
    private Rekke rekke;
    private GevinstBeregning gevinstBeregning;
    private ArrayList<Integer> vinnnerTab;

    /**
     * Håndterer klient basert på Socket objektet som blir innsendt
     * @param socket
     * @throws IOException
     */
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

    /**
     * Metode som kjører klientbehandling i ny tråd
     *
     */    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                // Leser hva slags type forespørsel og bestemmer hva slags svar som skal konstrueres
                HashMap<Object, Object> forespørselMap = (HashMap<Object, Object>) innStrøm.readObject();
                // Håndtering av innlogging
                if (forespørselMap.get("query").equals("loggInn")) {
                    System.out.println("Nytt innloggingsforsøk på " + forespørselMap);
                    sjekkBruker(forespørselMap);
                }
                // Håndtering av overføring av rekker
                if(forespørselMap.get("query").equals("sendRekke")) {
                    vinnnerTab = new ArrayList<>(lagVinnerTabell());
                    lagRekke(forespørselMap);
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
    /**
     * genererer en ny vinnerrekke for hver tråd som kobler seg til tjeneren
     */
    public ArrayList<Integer> lagVinnerTabell(){
        ArrayList<Integer>vinnerTabell = new ArrayList<>();
        int max = 34;
        int min = 1;
        int range = max - min + 1;


        for(int i=0; i<7; i++){
            int rand = (int)(Math.random() * range) + min;
            vinnerTabell.add(rand);
        }

        return vinnerTabell;
    }
    /**
     * konstruerer rekkeobjekter ut i fra hashmapen som blir sendt fra klient,
     * regner ut om noen av rekkene til bruker har vunnet, og hvor mye
     */
    public void lagRekke(HashMap map){
        ArrayList<ArrayList<Integer>> rekkeListe = (ArrayList<ArrayList<Integer>>)map.get("rekker");
        ArrayList<Integer> innsatsListe = (ArrayList<Integer>)map.get("innsats");

        for(int i =0; i<((ArrayList<?>) map.get("rekker")).size(); i++){
            System.out.println(new Rekke(rekkeListe.get(i),innsatsListe.get(i), bruker));

            System.out.println(rekkeListe.get(i));
            System.out.println(innsatsListe.get(i));
        }

        gevinstBeregning = new GevinstBeregning();
        gevinstBeregning.beregnGevinst(vinnnerTab, bruker);
        insertRekker(bruker);
        rekkeTilKlient();
    }
    /**
     * sender respons til klient
     */
    public void rekkeTilKlient()
    {
        System.out.println("sender svar til klient");
        HashMap<Object, Object> svar = new HashMap<>();
        svar.put("feilkode", 0);
        svar.put("vinnerRekke", vinnnerTab);
        svar.put("brukerRekker",bruker.hentVinnerRekker());
        svar.put("gevinst", bruker.hentGevinst());
        svar.put("innsats", bruker.hentVinnerInnsats());
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
    /**
     * legger til rekker i databasen
     */

    public void insertRekker(Bruker bruker){

        System.out.println("sender rekker til db");
        long millis = System.currentTimeMillis();
        Timestamp tid = new Timestamp(millis);
        dbAdaptor.createLottorekke();
        for(Rekke r: bruker.rekkeListe) {
                dbAdaptor.insertLottorekke(dbAdaptor.selectBrukerId(bruker.getEpost(), bruker.getTelefonnummer()), tid, r.getAntallRette() ,r.tallRekke.get(0),
                        r.tallRekke.get(1), r.tallRekke.get(2), r.tallRekke.get(3), r.tallRekke.get(4), r.tallRekke.get(5), r.tallRekke.get(6)
                );
        }
    }
    /**
     * metode for å sende logginn feil til klient
     */
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
    /**
     * metode for å sende logginn sukses til klient
     */

    private void loggInnSuksess(String melding, HashMap<Object, Object> map) {
        // Oppretter ny bruker og legger den inn som en instansvariabel i tilkobling
        this.bruker = new Bruker(
                (String) map.get("fornavn"),
                (String) map.get("etternavn"),
                (String) map.get("tlf"),
                (String) map.get("epost")
        );
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

    /**
     * kontrolerer tlf
     * @param tlf
     * @return
     */
    private static boolean kontrollerTlfNr(String tlf) {
        Pattern pattern = Pattern.compile("^\\d{8}$");
        Matcher matcher = pattern.matcher(tlf);
        return matcher.matches();
    }

    /**
     * kontrolerer epost
     * @param epost
     * @return
     */
    private static boolean kontrollerEpost(String epost) {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        Matcher matcher = pattern.matcher(epost);
        return matcher.matches();
    }

    /**
     * kontrollerer navn
     * @param navn
     * @return
     */
    private static boolean kontrollerNavn(String navn) {
        Pattern pattern = Pattern.compile("^[A-Za-z]\\w{1,60}$");
        Matcher matcher = pattern.matcher(navn);
        return matcher.matches();
    }
    /**
     * oppretter brukerobjekt gjennom hashmapen som blir sendt fra klient
     * legger til bruker i database
     */
    private void lagBruker(HashMap map) {
        bruker = new Bruker((String) map.get("fornavn"), (String)map.get("etternavn"), (String)map.get("tlf"), (String) map.get("epost"));
        // Lager ny brukerkonto og setter den inn i databasen.
        dbAdaptor.insertBruker((String) map.get("fornavn"), (String) map.get("etternavn"),
                (String) map.get("tlf"), (String) map.get("epost"));
        // Varsler bruker om at det har blitt laget en ny brukerkonto som følge av innlogging
        loggInnSuksess("Laget ny bruker. Velkommen " + map.get("fornavn") + "!", map);
    }

    /**
     *   For å logge inn skal det være en fullstendig match mellom epost og tlf. Dersom enten epost eller tlf
     *    er tatt i bruk fra før så må det være en match mellom disse for suksessfull innlogging.
     *    for hver tråd som kobler seg til tjeneren
     */

    private void sjekkBruker(HashMap map) {
        // Leser inn verdier fra map
        String fnavn = (String) map.get("fornavn");
        String eNavn = (String) map.get("etternavn");
        String tlf = (String) map.get("tlf");
        String ePost = (String) map.get("epost");
        HashMap<Object, Object> svar = new HashMap<>();

        if (kontrollerTlfNr(tlf) && kontrollerNavn(fnavn) && kontrollerNavn(eNavn) && kontrollerEpost(ePost)) {

            String innEpost = (String) map.get("epost");
            String innTlf = (String) map.get("tlf");
            // Teller antall og sjekker om verdien allerede eksisterer i databasen
            dbAdaptor.selectEpost(map.get("epost").toString());
            dbAdaptor.selectTelefonnummer(map.get("tlf").toString());
            if (dbAdaptor.isEpostEksisterer() || dbAdaptor.isNrEksisterer()) {
                // Sjekker samsvar mellom tlf og epost
                if (dbAdaptor.sjekkSamsvar(innEpost, innTlf)) {
                    // Det er samsvar mellom epost og tlf
                    loggInnSuksess("Bruker logget inn", map);
                } else {
                    // Den ene eller andre eksisterer men det er ikke samsvar
                    loggInnFeil();
                }
            } else {
                // Hverken epost eller tlf eksisterer
                lagBruker(map);
            }
        }
    }

    /**
     *  Hjelpemetode for å lukke alle strømmene
     */
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


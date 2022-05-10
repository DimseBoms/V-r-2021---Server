
import java.util.ArrayList;

public class Bruker {
    public static  ArrayList<Rekke> rekkeListe = new ArrayList<>();
    private String fornavn;
    private String etternavn;
    private String telefonnummer;
    private String epost;
    private static ArrayList<Bruker> brukere = new ArrayList<>();

    public Bruker(String fornavn, String etternavn, String telefonnummer, String epost) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telefonnummer = telefonnummer;
        this.epost = epost;
        brukere.add(this);
    }

    public static ArrayList<Bruker> getBrukere() {
        return brukere;
    }

    @Override
    public String toString() {
        return "Bruker{" +
                "fornavn='" + fornavn + '\'' +
                ", etternavn='" + etternavn + '\'' +
                ", telefonnummer='" + telefonnummer + '\'' +
                ", epost='" + epost + '\'' +
                '}';
    }

}
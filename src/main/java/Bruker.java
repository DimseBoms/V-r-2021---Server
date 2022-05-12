
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Bruker {

    protected  ArrayList<Rekke> rekkeListe;
    protected  ArrayList<Rekke> vinnerRekker;
    private String fornavn;
    private String etternavn;
    private String telefonnummer;
    private String epost;

    public String getFornavn() {
        return fornavn;
    }

    public String getEtternavn() {
        return etternavn;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public String getEpost() {
        return epost;
    }

    private static ArrayList<Bruker> brukere = new ArrayList<>();

    public Bruker(String fornavn, String etternavn, String telefonnummer, String epost) {
        this.fornavn = fornavn;
        this.etternavn = etternavn;
        this.telefonnummer = telefonnummer;
        this.epost = epost;
        rekkeListe= new ArrayList<>();
        brukere.add(this);
    }

    //henter rekker som har vunnet
    public ArrayList<?> hentVinnerRekker(){
        ArrayList<ArrayList<Integer>> vinnerRekker = new ArrayList<>();
        for(Rekke r: this.rekkeListe){
        if(r.getAntallRette() >= 5){
            vinnerRekker.add(r.tallRekke);
            }
        }
        return vinnerRekker;
    }
    public ArrayList<?> hentGevinst() {
        ArrayList<Integer> gevinstTab = new ArrayList<>();
        for (Rekke r : this.rekkeListe) {
            if (r.getAntallRette() >= 5) {
                gevinstTab.add((int) r.gevinst);
            }

        }
        return gevinstTab;
    }
    public ArrayList<?> hentVinnerInnsats(){
        ArrayList<Integer> innsatsListe = new ArrayList<>();
        for(Rekke r: this.rekkeListe){
            if(r.getAntallRette() >= 5){
                innsatsListe.add(r.rekkePris);
            }
        }
        return innsatsListe;
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
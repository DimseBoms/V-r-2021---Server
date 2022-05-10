import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Lottorekke {

    private int brukerId; // denne burde komme fra bruker? Bruker bruker?
    private Timestamp tidspunt; // Jeg er en util.Date
    private int antallRette;
    private int t1;
    private int t2;
    private int t3;
    private int t4;
    private int t5;
    private int t6;
    private int t7;
    private static ArrayList<Lottorekke> rekker = new ArrayList<>();

    public Lottorekke(int brukerId, Timestamp tidspunt, int antallRette, int t1, int t2, int t3, int t4, int t5, int t6, int t7) {
        this.brukerId = brukerId;
        this.tidspunt = tidspunt;
        this.antallRette = antallRette;
        this.t1 = t1;
        this.t2 = t2;
        this.t3 = t3;
        this.t4 = t4;
        this.t5 = t5;
        this.t6 = t6;
        this.t7 = t7;
        rekker.add(this);
    }

    public static ArrayList<Lottorekke> getLottorekker() {
        return rekker;
    }

    @Override
    public String toString() {
        return "Lottorekke{" +
                "brukerId=" + brukerId +
                ", tidspunt=" + tidspunt +
                ", antallRette=" + antallRette +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", t3=" + t3 +
                ", t4=" + t4 +
                ", t5=" + t5 +
                ", t6=" + t6 +
                ", t7=" + t7 +
                '}';
    }
}
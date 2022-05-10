import java.util.ArrayList;
import java.util.Collections;

public class Rekke   {
    public ArrayList<Integer> tallRekke;
    public int rekkePris;
    protected double gevinst;


    public Rekke(ArrayList<Integer>rekkeMedTall, int rekkePris, Bruker bruker) {

        this.tallRekke = new ArrayList<>(rekkeMedTall);
        Collections.sort(tallRekke);
        this.rekkePris= rekkePris;

        bruker.rekkeListe.add(this);
    }




    /**
     metode for Ã¥ sjekke om bruker har vunnet og
     beregne gevinst

     **/
    /*public double beregnGevinst (ArrayList<Integer>vinnerRekke, Bruker bruker){
        Collections.sort(vinnerRekke);
        int teller=0;


         for(Rekke r: bruker.rekkeListe) {
             for (Integer vinnerTall : vinnerRekke) {
                 for (Integer brukerTall : r.tallRekke) {
                     if (brukerTall.equals(vinnerTall)) {
                         teller++;
                     }
                     if (teller == 5) {
                         gevinst = r.rekkePris * 0.15 * 730;
                     }
                     if (teller == 6) {
                         gevinst = r.rekkePris * 0.15 * 30000;
                     }

                     if (this.tallRekke.equals(vinnerRekke)) {
                         gevinst = r.rekkePris * 0.2 * 5400000;
                     }
                 teller=0;}

             }

         }


        return gevinst;
    }
*/
    @Override
    public String toString() {
        return "Rekke{" +
                "tallRekke=" + tallRekke +
                ", rekkePris=" + rekkePris +
                ", gevinst=" + gevinst +
                '}';
    }
    public void setGevinst(double gevinst) {
        this.gevinst = gevinst;
    }
}
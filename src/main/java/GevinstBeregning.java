import java.util.ArrayList;
import java.util.Collections;

public class GevinstBeregning {
    protected int innsats;
    protected double gevinst;



    /**
     metode for å sjekke om bruker har vunnet og
     beregne gevinst
     **/

    public void beregnGevinst (ArrayList<Integer> vinnerRekke, Bruker bruker){
        Collections.sort(vinnerRekke);
        int teller=0;
        for(Rekke r: bruker.rekkeListe) {
            for (Integer vinnerTall : vinnerRekke) {
                for (Integer brukerTall : r.tallRekke) {
                    if (brukerTall.equals(vinnerTall)) {
                        teller++;
                    }
                }
            }
            if (teller == 5) {
                gevinst = r.rekkePris * 0.15 * 730;
            }
            else if  (teller == 6) {
                gevinst = r.rekkePris * 0.15 * 30000;
            }

            else if (r.tallRekke.equals(vinnerRekke)) {
                gevinst = r.rekkePris * 0.2 * 5400000;
            }
            teller=0;
            r.setGevinst(gevinst);

        }


    }

}
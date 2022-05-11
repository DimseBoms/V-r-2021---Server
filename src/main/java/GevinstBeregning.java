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
        System.out.println("bruker sin liste" + bruker.rekkeListe);
        Collections.sort(vinnerRekke);
        int teller = 0;
            for(Rekke r: bruker.rekkeListe) {
                Collections.sort(r.tallRekke);
                for(int i =0; i<vinnerRekke.size(); i++ ){
                        if (r.tallRekke.get(i).equals(vinnerRekke.get(i))) {
                            teller++;
                            System.out.println("match");
                        }
                    }


                r.setAntallRette(teller);
           // System.out.println(r.getAntallRette());

            teller=0;
            if (r.getAntallRette()==5) {
                gevinst += r.rekkePris * 0.15 * 730;
               r.setGevinst(r.rekkePris * 0.15 * 30000  );
            }

             if  (r.getAntallRette()==6) {
                gevinst += r.rekkePris * 0.15 * 30000;
                r.setGevinst(r.rekkePris* 0.15 * 30000);
            }

             if (r.tallRekke.equals(vinnerRekke)) {
                gevinst += r.rekkePris * 0.2 * 5400000;
                r.setGevinst(r.rekkePris * 0.2 * 5400000);
            }
            r.setGevinst(gevinst);
            System.out.println(r.gevinst);

        }

    }

}
package Mankala.Gracze;

import Mankala.Stan;

import java.util.Scanner;

public class Gracz implements Player {

    String nazwa;
    int heurystyka;

    public Gracz(String nazwa, int heurystyka){
        this.nazwa=nazwa;
        this.heurystyka=heurystyka;
    }

    @Override
    public int wykonajRuch(Stan stan, Player graczKtoryMaRuch, int numergracza) {

        Scanner scan = new Scanner(System.in);
        String ruch = scan.nextLine();

        boolean isNumeric = isNumeric(ruch);

        if (isNumeric) {
            int w =Integer.parseInt(ruch);
            if(w>0 && w< stan.kubly) {
                return w;
            }else
                System.out.println("zla liczba");
        }
        return -1;
    }

    public boolean isNumeric(String s){
        int intvalue;
        if((s ==null) || s.equals("")){
            return false;
        }
        try {
            intvalue = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("zla liczba");
        }
        return false;
    }

    public String toString(){
        return "Gracz: "+ nazwa;
    }

    public int getHeurystyka(){
        return heurystyka;
    }
}

package Mankala;

import Mankala.Algorytm.AlphaBeta;
import Mankala.Algorytm.Minmax;
import Mankala.Gracze.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        System.out.println("MANKALA");
        System.out.println("wybierz tryb: ");
        System.out.println("1: ai vs ai ");
        System.out.println("2: gracz vs ai");
        System.out.println("3: gracz vs gracz");
        System.out.println("4: ai vs ai, x razy");
        Scanner scan = new Scanner(System.in);
        String ruch = scan.nextLine();
        int r=Integer.parseInt(ruch);
        Gra g1= new Gra();

        Player p1;
        Player p2;
        switch(r){
            case 1:
                System.out.println("TRYB ai vs ai ");
                p1= new Komputer ("Komputer 1",new Minmax(2),1);
                p2= new Komputer ("Komputer 2",new AlphaBeta(4),1);
                g1.init(p1,p2);
                g1.start(1); //pierwszyruch: 0-nie losowy, 1-losowy
                break;

            case 2:
                System.out.println("TRYB gracz vs ai");
                p1= new Gracz ("Gracz",1);
                p2= new Komputer ("Komputer",new Minmax(4),1);
                g1.init(p1,p2);
                g1.start(1); //pierwszyruch: 0-nie losowy, 1-losowy
                break;
            case 3:
                System.out.println("TRYB gracz vs gracz");
                p1= new Gracz ("Gracz 1 ",1);
                p2= new Gracz ("Gracz 2",1);
                g1.init(p1,p2);
                g1.start(1); //pierwszyruch: 0-nie losowy, 1-losowy
                break;
            case 4:
                System.out.println("wprowadz liczbe gier");
                p1= new Komputer ("AlphaBeta1",new Minmax(3),3);
                p2= new Komputer ("AlphaBeta2",new AlphaBeta(6),3);
                String ruch2 = scan.nextLine();
                g1.init(p1,p2);
                g1.startOdpalX(1,Integer.parseInt(ruch2));
                break;
            default:
                System.out.println("wprowadzona fraza nie pasuje do żadnego trybu. zamykanie");
        }
    }

}

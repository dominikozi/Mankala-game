package Mankala;
import Mankala.Gracze.Player;

import java.util.Hashtable;

public class Stan {

    public int kubly;
    public int kamienie_poczatkowe;
    public int[] plansza;

    Hashtable<Player,Integer> punkty;
    Player p1;
    Player p2;

    Hashtable<Player,Integer> liczbaRuchow;
    Hashtable<Player,Integer> powtorzenia;

    public Stan copy(){
        Stan nowy = new Stan(this);
        return nowy;
    }

    public Stan (Stan other){
        this.kubly = other.kubly;
        this.kamienie_poczatkowe = other.kamienie_poczatkowe;
        this.p1= other.p1;
        this.p2= other.p2;
        punkty = new Hashtable<>();
        punkty.put(p1,other.punkty.get(other.p1));
        punkty.put(p2,other.punkty.get(other.p2));

        powtorzenia = new Hashtable<>();
        powtorzenia.put(p1,other.punkty.get(other.p1));
        powtorzenia.put(p2,other.punkty.get(other.p2));

        liczbaRuchow = new Hashtable<>();
        liczbaRuchow.put(p1,other.punkty.get(other.p1));
        liczbaRuchow.put(p2,other.punkty.get(other.p2));


        plansza = new int[2*kubly];
        for(int i=0;i<other.plansza.length;i++){
            this.plansza[i]=other.plansza[i];
        }
    }

    public Stan (int kubly, int kamienie_poczatkowe, Player p1, Player p2) {
        this.kubly=kubly;
        this.kamienie_poczatkowe = kamienie_poczatkowe;

        this.p1=p1;
        this.p2=p2;

        punkty = new Hashtable<>();
        punkty.put(p1,0);
        punkty.put(p2,0);

        powtorzenia = new Hashtable<>();
        powtorzenia.put(p1,0);
        powtorzenia.put(p2,0);

        liczbaRuchow = new Hashtable<>();
        liczbaRuchow.put(p1,0);
        liczbaRuchow.put(p2,0);

        plansza = new int[2*kubly];
        for (int i=0;i<plansza.length;i++){
            plansza[i]=kamienie_poczatkowe;
        }
    }

    public int mozliweRuchy(Player p, int numer){      //sprawdza liczbe mozliwych ruchow dla gracza
        int suma=0;
        for(int i=numer*kubly;i<(1+numer)*kubly;i++){
            if(plansza[i]>0){
                suma++;
            }
        }
        return suma;
    }

    public boolean czyRuchMozliwy(Player p, int numer, int ruch){
        if(plansza[ruch+(numer*kubly)]>0){
            return true;
        }
        return false;
    }

    public int getLiczbaRuchow(Player p){
        return liczbaRuchow.get(p);
    }

    public void dodajPunktyKoniec(Player p, int numer){ //dodaje punkty za wszystkie kamienie w kubelkach danego gracza.
                                                        // (wywolywane jesli dla gracza drugiego mozliweRuchy=0;
        int suma=0;
        for(int i=numer*kubly;i<(1+numer)*kubly;i++){
            suma=suma+plansza[i];
        }
        punkty.put(p,punkty.get(p)+suma);

    }

    public int ileKamieniWKuble(Player p, int numer, int nr_kubla){
        return plansza[((numer*kubly)+nr_kubla)];
    }


    public boolean nastepnyRuch(Player player, int number, int ruch){ //wykonuje ruch, zwraca true jesli po tym ruchu dalej jest ruch tego samego gracza.
        liczbaRuchow.put(player,liczbaRuchow.get(player)+1);
        int i = ruch+(number*kubly);
        int wartosc = plansza[i];
        plansza[i]=0;
            for(int w=wartosc;w>0;w--){
                i++;
                if((i==6 && number==0) || (i==12 && number==1)){
                    punkty.put(player,punkty.get(player)+1);
                    if (w==1){
                        return true;
                    }else if (i==12){
                        plansza[0]++;
                    }else{
                        plansza[i]++;
                    }
                    w--;
                    continue;
                }
                if(i>=12){
                    i=1;
                }
                if(w==1 && number==0 && i<6 && plansza[i]==0){
                    int przeciwglegla_pozycja=11-i;
                    int zbicie = plansza[przeciwglegla_pozycja];
                    plansza[przeciwglegla_pozycja]=0;
                    punkty.put(player,punkty.get(player)+1+zbicie);
                    plansza[i]--;
                }
                else if(w==1 && number==1 && i<12 && i>=6 && plansza[i]==0)
                {
                    int przeciwglegla_pozycja=11-i;
                    int zbicie = plansza[przeciwglegla_pozycja];
                    plansza[przeciwglegla_pozycja]=0;
                    punkty.put(player,punkty.get(player)+1+zbicie);
                    plansza[i]--;
                }
                plansza[i]++;
            }

        return false;
    }

    public void wyswietl(boolean zmiana) {
        System.out.println("");
        System.out.print(String.format("%19s", "")); System.out.print(String.format("%-4s", ""));
        for( int i=5;i>-1;i--){
            System.out.print(String.format("%4s",i));
        }
        if(zmiana){
            System.out.println(""); //plansza p1
            System.out.print(String.format("%20s",p2.toString())); System.out.print("{"+String.format("%2s",punkty.get(p2))+"}");
            for( int i=5;i>-1;i--){
                System.out.print("["+String.format("%2s",plansza[i])+"]");
            }
            System.out.println("");  //plansza p2
            System.out.print(String.format("%20s",p1.toString()));System.out.print(String.format("%4s",""));
            for( int i=6;i<12;i++){
                System.out.print("["+String.format("%2s",plansza[i])+"]");
            }
            System.out.print("{"+String.format("%2s",punkty.get(p1))+"}");
        }else {
            System.out.println(""); //plansza p1
            System.out.print(String.format("%20s", p1.toString()));
            System.out.print("{" + String.format("%2s", punkty.get(p1)) + "}");
            for (int i = 5; i > -1; i--) {
                System.out.print("[" + String.format("%2s", plansza[i]) + "]");
            }
            System.out.println("");  //plansza p2
            System.out.print(String.format("%20s", p2.toString()));System.out.print(String.format("%4s",""));
            for (int i = 6; i < 12; i++) {
                System.out.print("[" + String.format("%2s", plansza[i]) + "]");
            }
            System.out.print("{" + String.format("%2s", punkty.get(p2)) + "}");
        }
        System.out.println("");
        System.out.print(String.format("%19s", ""));System.out.print(String.format("%4s",""));
        for( int i=0;i<6;i++){
            System.out.print(String.format("%4s",i));
        }
        System.out.println("");

    }

    public int ocena(){
        return punkty.get(p1) - punkty.get(p2);
    }

    public int ocena(Player p1, Player p2){

        return punkty.get(p1) - punkty.get(p2);

    }

    public int ocena2(Player p1, Player p2){
        int p=0;
        if(punkty.get(p1)>=24){
            p=1000;
        }
        if(punkty.get(p2)>=24){
            p=-1000;
        }

        return punkty.get(p1) - punkty.get(p2) + p;
    }

    public int ocena3(Player p1, Player p2, int numergracza){
        int ja = liczonko(p1,p2,numergracza);
        int przeciwnik = liczonko(p2,p1,Math.abs(1-numergracza));

        return punkty.get(p1) - punkty.get(p2) + ja*5 - przeciwnik*5;
    }

    public int liczonko(Player p1, Player p2, int numergracza){
        int powtorki=0;
        for( int i = 0+numergracza*6;i<6+numergracza*6;i++){

            int d= 6+numergracza*6-i;
            int wartosc = plansza[i];
            wartosc=wartosc-d;
            while(wartosc>=0) {
                if (wartosc == 0) {
                    powtorki++;
                    break;

                }
                wartosc=wartosc-13;
            }
        }
        return powtorki;
    }


    public Player getOtherPlayer(Player player){
        if(player==p1){
            return p2;
        }
        return p1;
    }
}

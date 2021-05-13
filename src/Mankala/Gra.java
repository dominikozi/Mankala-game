package Mankala;

import Mankala.Gracze.Gracz;
import Mankala.Gracze.Komputer;
import Mankala.Gracze.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Gra {

    boolean czas_w_ms=false;

    Stan gra;
    Stan poczatkowa;
    Player p1 = new Gracz("init1",1);
    Player p2 = new Gracz("init2",1);
    ArrayList<Player> gracze = new ArrayList<>();
    ArrayList<Integer> numery_graczy = new ArrayList<>();
    ArrayList<Stan> stany = new ArrayList<>();

    Hashtable<Player,Long> czasy= new Hashtable<>();

    int liczba_ruchow = 0;

    boolean print=true;

    public Gra(){
    }

    public void nextPlayer(){
        Collections.reverse(gracze);
        Collections.reverse(numery_graczy);
    }
    boolean zmiana=false;
    public void zamienGraczy(){
        Collections.reverse(gracze);
        zmiana=true;
    }

    public Player getPlayer(){
        return gracze.get(0);
    }
    public int getNumer(){
        return numery_graczy.get(0);
    }

    public void init(Player p1, Player p2){

        if (p1 != null) {
            this.p1=p1;
        }
        if (p2 != null) {
            this.p2=p2;
        }

        gracze.add(this.p1);
        numery_graczy.add(0);
        gracze.add(this.p2);
        numery_graczy.add(1);

    }

    public Player getOtherPlayer(Player player){
        if(player==p1){
            return p2;
        }
        return p1;
    }

    public void start(int pierwszyruch){
        boolean end=false;
        Hashtable<Player,Long> czasy= new Hashtable<>();
        czasy.put(getPlayer(), 0L);
        czasy.put(getOtherPlayer(getPlayer()), 0L);
        if(Math.random()>0.5){
            zamienGraczy();
        }
        while(!end){

            long startTime = System.nanoTime();
            if (liczba_ruchow == 0) { //losowanie gracza zaczynającego
                gra = new Stan(6,4,p1,p2);
            }

            if(gra.mozliweRuchy(getPlayer(),getNumer())==0){
                nextPlayer();
                gra.dodajPunktyKoniec(getPlayer(),getNumer());
                int wynik = ocena(gra);
                System.out.println("--------- koniec gier ---------");
                System.out.println("Ocena wzgledna dla gracza 1: "+ wynik);
                System.out.println(p1.toString()+": "+gra.punkty.get(p1) ); System.out.print(p2.toString()+": "+gra.punkty.get(p2) );
                System.out.println();
                System.out.print("Wygrał "); if(wynik>0) System.out.print(p1.toString()); if(wynik <0) System.out.print(p2.toString());
                System.out.println();
                System.out.println("Liczba ruchów graczy: ");
                System.out.println("Gracz "+ getPlayer().toString() +"- " + gra.getLiczbaRuchow(getPlayer())+"ruchów");
                System.out.println("Gracz "+ gra.getOtherPlayer(getPlayer()).toString() +"- " + gra.getLiczbaRuchow(gra.getOtherPlayer(getPlayer()))+"ruchów");
                System.out.println("Sredni czas: ");
                System.out.println("Czas "+ getPlayer().toString() +"- " + czasy.get(getPlayer())/1000000+ " ms");
                System.out.println("Czas "+ gra.getOtherPlayer(getPlayer()).toString() +"- " + czasy.get(gra.getOtherPlayer(getPlayer()))/1000000+ " ms");


                end=true;continue;
            }

            if(print){
                gra.wyswietl(zmiana);
                System.out.println("");
                System.out.println("-------");
                System.out.println("Ruch playera: "+ getPlayer().toString());
            }

            stany.add(gra.copy());
            int ruch=0;
            if(liczba_ruchow==0 && pierwszyruch==1) {
                ruch = ThreadLocalRandom.current().nextInt(0, gra.kubly);
                System.out.println("Wykonano automatyczny losowy ruch dla pierwszego gracza: "+ ruch);
            }
            else{
                if(getPlayer().getClass().equals(Gracz.class)) ruch = sprRuch(gra);
                if(getPlayer().getClass().equals(Komputer.class)) {
                    ruch = znajdzRuch(gra);
                    System.out.println("Kubel wybrany przez komputer: "+ ruch);
                }
            }

            boolean repeat = gra.nastepnyRuch(getPlayer(),getNumer(),ruch);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            long timeWms = timeElapsed;
            czasy.put(getPlayer(),czasy.get(getPlayer())+timeWms);

            if(repeat){

            }else{
                nextPlayer();
            }

            liczba_ruchow++;

        }

    }

    public void startOdpalX(int pierwszyruch, int x){
        System.out.print("Odpalono gre AI vs AI "+x+" razy.");
        System.out.print("");
        System.out.println();

        int wynikp1=0;
        int wynikp2=0;
        int sredniaruchowp1=0;
        int sredniaruchowp2=0;
        long sredniczasp1=0L;
        long sredniczasp2=0L;
        int wynikwzglednysuma=0;
        int remisy =0;
        ArrayList<WynikGry> wyniki = new ArrayList<>();
        for(int i=0;i<x;i++){
            WynikGry temp = start2(pierwszyruch);
            wyniki.add(temp);
            System.out.println(temp.toString());
            if(temp.getZwyciezca()!=null) {
                if (temp.getZwyciezca().toString().equals(p1.toString())) {
                    wynikp1++;
                    sredniaruchowp1 = sredniaruchowp1 + temp.getLiczbaRuchów();
                    sredniczasp1 = sredniczasp1 + temp.getCzasZwyciezcy();
                    sredniczasp2 = sredniczasp2 + temp.getCzasOponenta();
                }
                if (temp.getZwyciezca().toString().equals(p2.toString())) {
                    wynikp2++;
                    sredniaruchowp2 = sredniaruchowp2 + temp.getLiczbaRuchów();
                    sredniczasp2 = sredniczasp2 + temp.getCzasZwyciezcy();
                    sredniczasp1 = sredniczasp1 + temp.getCzasOponenta();
                }
            }else{
                remisy++;
            }
            wynikwzglednysuma = wynikwzglednysuma + temp.getWynikWzgledny();
        }
        if(czas_w_ms) {
            sredniczasp1 = sredniczasp1/1000000;
            sredniczasp2 = sredniczasp2/1000000;
        }
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%22s %24s %24s","     " , p1.toString(), p2.toString());System.out.println("");
        System.out.println("---------------------------------------------------------------------------");
        System.out.printf("%22s %24s %24s","Wygrane" ,wynikp1, wynikp2);System.out.println("");
        System.out.printf("%22s %24s %24s","Srednia liczba ruchów" ,sredniaruchowp1/wynikp1, sredniaruchowp2/wynikp2);System.out.println("");
        System.out.printf("%22s %24s %24s","Sredni czas gry[nanos] " ,sredniczasp1/x, sredniczasp2/x);System.out.println("");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Liczba remisów: " + remisy );
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("Wynik wzgledny suma: "+wynikwzglednysuma);
        System.out.println("Wynik wzgledny średnia: "+(wynikwzglednysuma/x));

    }

    public WynikGry start2(int pierwszyruch){
        boolean end=false;
        czasy.put(getPlayer(), 0L);
        czasy.put(getOtherPlayer(getPlayer()), 0L);
        WynikGry wynikkk = new WynikGry();
        while(!end){
            if (liczba_ruchow == 0) { //losowanie gracza rozpoczynającego
                if(Math.random()>0.5){
                    zamienGraczy();//zamiana graczy z prawodopobieńswem 0.5
                }

                gra = new Stan(6,4,p1,p2);
                poczatkowa= new Stan(gra);
            }

            if(gra.mozliweRuchy(getPlayer(),getNumer())==0){
                nextPlayer();
                gra.dodajPunktyKoniec(getPlayer(),getNumer());
                int wynik = ocena(gra);
                if(wynik>0) {
                  System.out.println("wygral " + p1.toString());
                    wynikkk.setZwyciezca(p1);
                    wynikkk.setOponent(p2);
                    wynikkk.setLiczbaRuchów(gra.liczbaRuchow.get(p1));
                    wynikkk.setLiczbaRuchówOponenta(gra.liczbaRuchow.get(p2));
                    wynikkk.setCzasZwyciezcy(czasy.get(p1));
                    wynikkk.setCzasOponenta(czasy.get(p2));
                    wynikkk.setWynikZwyciezca(gra.punkty.get(p1));
                    wynikkk.setWynikOponent(gra.punkty.get(p2));
                }
                if(wynik<0){
                    System.out.println("wygral " + p2.toString());
                    wynikkk.setZwyciezca(p2);
                    wynikkk.setOponent(p1);
                    wynikkk.setLiczbaRuchów(gra.liczbaRuchow.get(p2));
                    wynikkk.setLiczbaRuchówOponenta(gra.liczbaRuchow.get(p1));
                    wynikkk.setCzasZwyciezcy(czasy.get(p2));
                    wynikkk.setCzasOponenta(czasy.get(p1));
                    wynikkk.setWynikZwyciezca(gra.punkty.get(p2));
                    wynikkk.setWynikOponent(gra.punkty.get(p1));
                }
                wynikkk.setWynikWzgledny(wynik);
                end=true;continue;
            }

            long startTime = System.nanoTime();
            stany.add(gra.copy());
            int ruch=0;
            if(liczba_ruchow==0 && pierwszyruch==1) {
                ruch = ThreadLocalRandom.current().nextInt(0, gra.kubly);
            }
            else{
                if(getPlayer().getClass().equals(Gracz.class)) ruch = sprRuch(gra);
                if(getPlayer().getClass().equals(Komputer.class)) {
                    ruch = znajdzRuch(gra);
                }
            }

            boolean repeat = gra.nastepnyRuch(getPlayer(),getNumer(),ruch);

            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            long timeWms = timeElapsed;
            czasy.put(getPlayer(),czasy.get(getPlayer())+timeWms);

            if(repeat){

            }else{
                nextPlayer();
            }

            liczba_ruchow++;

        }
        liczba_ruchow=0;
        return wynikkk;
    }


    public int znajdzRuch(Stan gra){
        return getPlayer().wykonajRuch(gra,getPlayer(),getNumer());
    }


    public int sprRuch(Stan gra){
        System.out.println("Wprowadź ruch (indeks kubełka): ");
        boolean stop=false;
        int wynik = -1;
        while(!stop){
            int wskaznikRuchu = getPlayer().wykonajRuch(gra,getPlayer(),getNumer());
            if(wskaznikRuchu==-1){
                System.out.println("Ruch niemozliwy.");
            }

            else if(gra.ileKamieniWKuble(getPlayer(),getNumer(),wskaznikRuchu)!=0){
                wynik=wskaznikRuchu;
                stop=true;
            }else{
                System.out.println("Ruch niemozliwy.");
            }
        }
        return wynik;
    }


    public int ocena(Stan gra){
        return gra.ocena();
    }


}

class WynikGry {
    public Player zwyciezca;
    public Player oponent;
    public int liczbaRuchów;
    public int liczbaRuchówOponenta;
    public long czasZwyciezcy;
    public long czasOponenta;
    public int wynikWzgledny;
    public int wynikZwyciezca;
    public int wynikOponent;



    public WynikGry(Player zwyciezca,Player oponent, int liczbaRuchów, int liczbaRuchówOponenta, long czasZwyciezcy, long czasOponenta, int wynikWzgledny, int wynikZwyciezca, int wynikOponent) {
        this.zwyciezca = zwyciezca;
        this.liczbaRuchów = liczbaRuchów;
        this.liczbaRuchówOponenta = liczbaRuchówOponenta;
        this.czasZwyciezcy = czasZwyciezcy;
        this.czasOponenta = czasOponenta;
        this.wynikWzgledny = wynikWzgledny;
        this.wynikZwyciezca = wynikZwyciezca;
        this.wynikOponent = wynikOponent;
    }

    public WynikGry() {
    }

    public Player getZwyciezca() {
        return zwyciezca;
    }

    public void setZwyciezca(Player zwyciezca) {
        this.zwyciezca = zwyciezca;
    }

    public int getLiczbaRuchów() {
        return liczbaRuchów;
    }

    public void setLiczbaRuchów(int liczbaRuchów) {
        this.liczbaRuchów = liczbaRuchów;
    }

    public long getCzasZwyciezcy() {
        return czasZwyciezcy;
    }

    public void setCzasZwyciezcy(long czasZwyciezcy) {
        this.czasZwyciezcy = czasZwyciezcy;
    }

    public long getCzasOponenta() {
        return czasOponenta;
    }

    public void setCzasOponenta(long czasOponenta) {
        this.czasOponenta = czasOponenta;
    }

    public int getWynikWzgledny() {
        return wynikWzgledny;
    }

    public int getLiczbaRuchówOponenta() {
        return liczbaRuchówOponenta;
    }

    public void setLiczbaRuchówOponenta(int liczbaRuchówOponenta) {
        this.liczbaRuchówOponenta = liczbaRuchówOponenta;
    }

    public void setWynikWzgledny(int wynikWzgledny) {
        this.wynikWzgledny = wynikWzgledny;
    }

    public Player getOponent() {
        return oponent;
    }

    public void setOponent(Player oponent) {
        this.oponent = oponent;
    }

    public int getWynikZwyciezca() {
        return wynikZwyciezca;
    }

    public void setWynikZwyciezca(int wynikZwyciezca) {
        this.wynikZwyciezca = wynikZwyciezca;
    }

    public int getWynikOponent() {
        return wynikOponent;
    }

    public void setWynikOponent(int wynikOponent) {
        this.wynikOponent = wynikOponent;
    }

    @Override
    public String toString() {
        return "WynikGry{" +
                "zwyciezca=" + zwyciezca +
                ", oponent=" + oponent +
                ", liczbaRuchów=" + liczbaRuchów +
                ", liczbaRuchówOponenta=" + liczbaRuchówOponenta +
                ", czasZwyciezcy=" + czasZwyciezcy +
                ", czasOponenta=" + czasOponenta +
                ", wynikWzgledny=" + wynikWzgledny +
                ", wynikZwyciezca=" + wynikZwyciezca +
                ", wynikOponent=" + wynikOponent +
                '}';
    }
}

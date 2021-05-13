package Mankala.Gracze;
import Mankala.Algorytm.Algorytm;
import Mankala.Algorytm.Minmax;
import Mankala.Stan;


public class Komputer implements Player {

    String nazwa;
    Algorytm algorytm;
    int heurystyka;

    public Komputer(String nazwa, Algorytm algorytm,  int heurystyka){
        this.nazwa  =   nazwa;
        this.algorytm=algorytm;
        this.heurystyka=heurystyka;
    }

    @Override
    public int wykonajRuch(Stan stan, Player graczKtoryMaRuch, int numergracza) {
        return algorytm.znajdzRuch(stan, graczKtoryMaRuch, numergracza );
    }

    public String toString(){
        return "Komputer: "+ nazwa;
    }

    public int getHeurystyka(){
        return heurystyka;
    }
}

package Mankala.Algorytm;

import Mankala.Stan;
import Mankala.Gracze.*;

public class Minmax implements Algorytm{

    int maxh;

    public Minmax(int maxh){
        this.maxh=maxh;
    }

    public Minmax(Minmax other){

        this.maxh=other.maxh;

    }

    @Override
    public int znajdzRuch(Stan stan, Player graczKtoryMaRuch, int numergracza) {

        Stan stann= stan.copy();

        return minmax4(stann,0,graczKtoryMaRuch,numergracza,true,graczKtoryMaRuch).getIndex();
    }
    public WynikMinMax minmax4(Stan stannn, int glebokosc, Player graczKtoryMaRuch, int numergracza, boolean isMaximizingPlayer, Player graczocenia){
        Stan stann = stannn.copy();
        if(glebokosc==maxh){
            return new WynikMinMax(stann.ocena(graczocenia,stann.getOtherPlayer(graczocenia)),0);
        }
        else if(stann.mozliweRuchy(graczKtoryMaRuch, numergracza)==0){ //koniec gry
            return new WynikMinMax(stann.ocena(graczocenia,stann.getOtherPlayer(graczocenia)),0);
        }
        if (isMaximizingPlayer){
            int bestval = Integer.MIN_VALUE;
            int bestmove=-1;
            WynikMinMax w1=null;
            for(int i=0;i<stann.kubly;i++) {
                Stan stan = stann.copy();
                if(stan.czyRuchMozliwy(graczKtoryMaRuch,numergracza,i)) {

                    int value;
                    if(stan.nastepnyRuch(graczKtoryMaRuch,numergracza,i)){
                        value = minmax4(stan, glebokosc+1, graczKtoryMaRuch, numergracza, true,graczocenia).getValue();
                    }else{
                        value = minmax4(stan, glebokosc+1, stan.getOtherPlayer(graczKtoryMaRuch),Math.abs(1-numergracza),false,graczocenia).getValue();
                    }
                    if(value > bestval){
                        bestval=value;
                        bestmove=i;
                        w1= new WynikMinMax(bestval, bestmove);
                    }

                }
            }
            return w1;
        }else{
            int bestval= Integer.MAX_VALUE;
            int bestmove=-1;
            WynikMinMax w1=null;
            for(int i=0;i<stann.kubly;i++) {
                Stan stan = stann.copy();
                if(stan.czyRuchMozliwy(graczKtoryMaRuch,numergracza,i)) {
                    int value;
                    if(stan.nastepnyRuch(graczKtoryMaRuch,numergracza,i)){
                        value = minmax4(stan, glebokosc+1, graczKtoryMaRuch, numergracza, false,graczocenia).getValue();
                    }else{
                        value = minmax4(stan, glebokosc+1, stan.getOtherPlayer(graczKtoryMaRuch),Math.abs(1-numergracza),true,graczocenia).getValue();
                    }
                    if(value < bestval){
                        bestval=value;
                        bestmove=i;
                        w1= new WynikMinMax(bestval, bestmove);
                    }
                }
            }
            return w1;
        }
    }
}


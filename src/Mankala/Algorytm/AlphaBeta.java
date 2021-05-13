package Mankala.Algorytm;

import Mankala.Gracze.Player;
import Mankala.Stan;

public class AlphaBeta implements Algorytm{

    int maxh;

    public AlphaBeta(int maxh) {
        this.maxh = maxh;
    }

    public AlphaBeta(AlphaBeta other){
        this.maxh=other.maxh;
    }

    @Override
    public int znajdzRuch(Stan stan, Player graczKtoryMaRuch, int numergracza) {
        Stan stann= stan.copy();
        return alfabeta(stann,0,graczKtoryMaRuch,numergracza,true,graczKtoryMaRuch, Integer.MIN_VALUE, Integer.MAX_VALUE).getIndex();
    }

    public WynikMinMax alfabeta(Stan stannn, int glebokosc, Player graczKtoryMaRuch, int numergracza, boolean isMaximizingPlayer, Player graczocenia, int alfa, int beta ){
        Stan stann = stannn.copy();
        if(glebokosc==maxh){
            if(graczocenia.getHeurystyka()==1) return new WynikMinMax(stann.ocena(graczocenia,stann.getOtherPlayer(graczocenia)),0);
            if (graczocenia.getHeurystyka()==2) return new WynikMinMax(stann.ocena2(graczocenia,stann.getOtherPlayer(graczocenia)),0);
            int token;
            if(graczKtoryMaRuch.equals(graczocenia)) token = numergracza;
            else token = Math.abs(1-numergracza);
            if (graczocenia.getHeurystyka()==3) return new WynikMinMax(stann.ocena3(graczocenia,stann.getOtherPlayer(graczocenia),token),0);
        }
        else if(stann.mozliweRuchy(graczKtoryMaRuch, numergracza)==0){ //koniec gry
            if(graczocenia.getHeurystyka()==1) return new WynikMinMax(stann.ocena(graczocenia,stann.getOtherPlayer(graczocenia)),0);
            if(graczocenia.getHeurystyka()==2) return new WynikMinMax(stann.ocena2(graczocenia,stann.getOtherPlayer(graczocenia)),0);
            int token;
            if(graczKtoryMaRuch.equals(graczocenia)) token = numergracza;
            else token = Math.abs(1-numergracza);
            if(graczocenia.getHeurystyka()==3) return new WynikMinMax(stann.ocena3(graczocenia,stann.getOtherPlayer(graczocenia),token),0);
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
                        value = alfabeta(stan, glebokosc+1, graczKtoryMaRuch, numergracza, true,graczocenia,alfa, beta).getValue();
                    }else{
                        value = alfabeta(stan, glebokosc+1, stan.getOtherPlayer(graczKtoryMaRuch),Math.abs(1-numergracza),false,graczocenia,alfa, beta).getValue();
                    }
                    if(value > bestval){
                        bestval=value;
                        bestmove=i;
                        w1= new WynikMinMax(bestval, bestmove);
                    }
                    if(alfa<bestval){
                        alfa=bestval;
                    }
                    if(alfa>=beta){
                        break;
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
                        value = alfabeta(stan, glebokosc+1, graczKtoryMaRuch, numergracza, false,graczocenia,alfa, beta).getValue();
                    }else{
                        value = alfabeta(stan, glebokosc+1, stan.getOtherPlayer(graczKtoryMaRuch),Math.abs(1-numergracza),true,graczocenia,alfa, beta).getValue();
                    }
                    if(value < bestval){
                        bestval=value;
                        bestmove=i;
                        w1= new WynikMinMax(bestval, bestmove);
                    }
                    if(bestval<beta){
                        beta=bestval;
                    }
                    if(beta<=alfa){
                        break;
                    }
                }
            }
            return w1;
        }
    }
}

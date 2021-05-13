package Mankala.Algorytm;
import Mankala.Gracze.Player;
import Mankala.Stan;

public interface Algorytm {

    public int znajdzRuch(Stan stan , Player graczKtoryMaRuch, int numergracza);

    public String toString();

}

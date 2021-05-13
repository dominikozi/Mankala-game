package Mankala.Gracze;

import Mankala.Stan;

public interface Player {


    public int wykonajRuch(Stan stan, Player graczKtoryMaRuch, int numergracza);

    public String toString();

    public int getHeurystyka();
}

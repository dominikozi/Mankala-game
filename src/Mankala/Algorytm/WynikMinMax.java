package Mankala.Algorytm;

public class WynikMinMax {

     public int value;
     public int index;

     public WynikMinMax(){

     }

     public WynikMinMax(int value, int index) {
         this.value = value;
         this.index = index;
     }


    public int getValue() {
        return value;
    }
    public int getIndex() {
        return index;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

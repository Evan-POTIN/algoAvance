package ExoVaries;

import java.util.Arrays;

public class ExoVaries {
    public static void main(String[] args) {

        int[] t = new int[]{0, 1, 1, 1, 0, 1, 0, 0, 0, 1};
        drapeauBicolorAux(t, 0);
    }

    public static int drapeauBicolorAux(int[] t, int i) {
        int a = t.length;

        if (i >= t.length - 1) {
            return t.length;
        } else {
            int premier = drapeauBicolorAux(t, i + 1);
            System.out.println(Arrays.toString(t));
            if (t[i] == 0){
                return premier;
            }else if (premier == i+1){

            }else{
                t[i] = 0;
                t[premier - 1] = 1;
                return premier - 1;
            }
            System.out.println(Arrays.toString(t));
            return premier;
        }
    }
}

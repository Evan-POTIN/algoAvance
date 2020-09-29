package TD_Jeux;

public class td_Jeux {
    public static void main(String[] var0) {
        resoudreAux(5,1,2);
    }

    public static void tourHanoi() {

    }

    public static void resoudreAux(int n, int i, int j) {
        int k = 6 - i - j;
        if (n == 1) {
            System.out.println(i+" -> "+j);
        }else{
            resoudreAux(n-1,i,k);
            System.out.println(i + " -> " + n);
            resoudreAux(n-1,k,j);
        }
    }
}

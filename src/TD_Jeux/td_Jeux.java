package TD_Jeux;

public class td_Jeux {
    public void main(String[] var0) {
        resoudre(5);
    }

    private static void resoudre(int n){
        resoudreAux(n,1,3);
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
    public int nbCoup(int n){
        if (n==1){
            return 1;
        }else if(n>= 2){
            return 2*nbCoup(n-1) + 1;
        }
    }
}

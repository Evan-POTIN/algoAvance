package TD_Jeux;

public class td_Jeux {
    public static void main(String[] var0) {
        remplir(3);
    }

    public static void remplir(int n){
        if(n==1){
            System.out.println(n);
        }else if (n==2){
            
        }else{
            System.out.println(n);
            remplir(n-1);
            vider(n-2);
            remplir(n-2);

        }
    }

    public static void vider(int n){
        if(n==1){

        }else if(n==2){

        }else {
            System.out.println(n);
            vider(n-1);
            remplir(n-2);
            vider(n-2);
        }
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
        }else{
            return 2*nbCoup(n-1) + 1;
        }
    }


}

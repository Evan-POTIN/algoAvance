package TD_Jeux;

import static TD_Jeux.td_Jeux.vider;

public class Baguenaudier {

    private int nbCase;
    private String[] baguenaudier;

    public Baguenaudier(int nbCase) {
        this.nbCase = nbCase;
        this.baguenaudier = new String[nbCase];
    }

    private void remplirAux(int n){
        if(n==0){
            this.baguenaudier[n] = "*";
            this.afficher();
        }else if (n==1){
            this.baguenaudier[n-1] = "*";
            this.afficher();
            this.baguenaudier[n] = "*";
            this.afficher();
        }else{
            remplirAux(n-1);
            viderAux(n-2);
            this.baguenaudier[n] = "*";
            this.afficher();
            remplirAux(n-2);

        }
    }

    private void viderAux(int n){
        if(n==0){
            this.baguenaudier[n] = "-";
            this.afficher();
        }else if(n==1){
            this.baguenaudier[n-1] = "-";
            this.afficher();
            this.baguenaudier[n] = "-";
            this.afficher();
        }else {
            viderAux(n-1);
            remplirAux(n-2);
            this.baguenaudier[n] = "-";
            this.afficher();
            viderAux(n-2);
        }
    }

    public void afficher(){
        for (int i = 0;i<nbCase;i++){
            System.out.print(this.baguenaudier[i] + " ");
        }
        System.out.print("\n");
    }

    public void remplir(){
        System.out.println("Remplir :");
        remplirAux(nbCase-1);
    }

    public void vider(){
        System.out.println("Vider :");
        viderAux(nbCase-1);
    }
}

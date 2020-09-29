package Turtle;

class DessinFractale{
    private final Turtle bob;

    private final static int LARGEUR = 800;
    private final static int HAUTEUR = 600;
    //taille de la fenetre graphique

    public DessinFractale(){
        bob  = new Turtle();
        Turtle.setCanvasSize(LARGEUR,HAUTEUR);//à appeler APRES création de la tortue
    }

    public DessinFractale(int v){
        //attention, plus v est grand, plus bob va lentement !
        this();
        bob.speed(v);
    }


    public void reset(){
        bob.clear();
        bob.up();
        bob.setPosition(0,0);
        bob.setDirection(0);
        bob.down();

    }

    public void close(){
        bob.exit();
    }


    public void carre(double l){
        Turtle bob = new Turtle();
        bob.forward(l);
        bob.left(90);
        bob.forward(l);
        bob.left(90);
        bob.forward(l);
        bob.left(90);
        bob.forward(l);
    }

    public void vonKoch(double l, int n) {
        //prérequis: n>=0
        //action: dessine vonKoch de  longueur l à l'ordre n
        if(n==0) {
            bob.forward(l);
        }
        else { //n-1>=0
            vonKoch(l/3, n-1);
            bob.left(60);
            vonKoch(l/3, n-1);
            bob.right(120);
            vonKoch(l/3, n-1);
            bob.left(60);
            vonKoch(l/3, n-1);
        }
    }

    public void arbre(int n,int alpha,double l){
        bob.forward(l);
        if(n>0){
            bob.left(alpha);
            arbre(n-1,alpha,l/2);
            bob.right(alpha);
            arbre(n-1,alpha,l/2);
            bob.right(alpha);
            arbre(n-1,alpha,l/2);
            bob.left(alpha);
        }//
        bob.backward(l); //ou bob.right(180);bob.forward(l);bob.
        //bob.right(180);
    }

    public void triangle(int n, int l) {
        if(n>=0) {
            bob.forward(l);
            bob.left(120);
            triangle(n-1,l/2);
            bob.forward(l);
            bob.left(120);
            triangle(n-1,l/2);
            bob.forward(l);
            bob.left(120);
            triangle(n-1,l/2);
        }
    }



    public void inverseDragon(double l , int n) {
        if (n > 0) {
            dragon(l, n - 1);
            bob.right(90);
            inverseDragon(l, n - 1);
        } else {
            bob.forward(l);
        }
    }


    public void dragon( double l , int n) {
        if(n>0){
            dragon(l,n-1);
            bob.left(90);
            inverseDragon(l,n-1);
        }
        else{
            bob.forward(l);
        }
    }


    public static void main(String[] args){
        DessinFractale d = new DessinFractale(500);
        /*d.carre(90);
        d.reset();
        d.carre(60);
        d.reset();

        d.vonKoch(90,2);
        d.reset();*/

        //d.arbre(5,70,500);
        //d.triangle(2,90);

        d.dragon(90,20);
        //d.close();
    }

}



package Magic.Cards;



public class BronzeSable extends Creature {
    public BronzeSable(){
        super(2,1);
    }

    /**
     * Execute the effect of the card
     */
    @Override
    public void effect(){
        System.out.println("\tScende in campo BronzeSable\n");
        //questo medoto non fa nulla, ma se dovessimo creare degli effetti di prova basta modificare qui
        //senza dover creare altre carte inutili
    }
}

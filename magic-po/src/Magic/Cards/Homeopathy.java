package Magic.Cards;


public class Homeopathy  extends Sorcery {


    public Homeopathy(){}

    /**
     * Execute the effect of the card
     */
    public void effect(){
        System.out.println("\tHomeopathy non fa nulla! (Si, proprio come splash)\n");
    }

    /**
     * End of effect:
     * Restore previous parameters of the target card
     */
    public void invoke(){
    	// Fine effetto della carta in EndPhase per esempio
    }
}

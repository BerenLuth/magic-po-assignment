package Magic.Cards;


public class NorwoodRanger extends Creature{
    public NorwoodRanger(){
        super(1,2);
    }

    /**
     * Execute the effect of the card
     */
    @Override
    public void effect(){
        System.out.println("\tScende in campo Norwood Ranger\n");
    }

}

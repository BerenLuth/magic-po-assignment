package Magic.Personal;

import Magic.Cards.Instant;
import Magic.Cards.Spell;

import java.util.ArrayList;


public class Hand {
    private int limit; //limit non verrà MAI modificato se non da effetti permanenti
    private ArrayList<Spell> carteMano;

    public Hand() {
        this.limit = 7;
        carteMano = new ArrayList<>();
    }

    /**
     * getter of limit
     * @return limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * setter of limit
     * @param limit amount to be setted as the new limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * getter of array list representing the hand
     * @return arraylist Hand
     */
    public ArrayList<Spell> getCarteMano() {
        return carteMano;
    }

    /**
     * adds a card to the hand
     * @param card card to be added
     */
    public void addCarta(Spell card) {
        carteMano.add(card);
    }

    /**
     * removes a card from the hand
     * @param card card to be removed
     */
    public void removeCarta(Spell card) {
        carteMano.remove(card);
    }

    /**
     * removes a card from the hand
     * @param i index of the card to be removed
     */
    public void removeCarta(int i) {
        carteMano.remove(i);
    }

    /**
     * size of the hand (number of cards)
     * @return size of hand
     */
    public int sizeMano() {
        return carteMano.size();
    }

    /**
     * checks if there are cards to be discarded
     * @return number of cards to be discarded
     */
    public int toDiscard(){
        if(this.getLimit() > this.sizeMano())
            return 0;
        else
            return this.sizeMano() - this.getLimit();
    }

    /**
     * get a card
     * @param i index of the card
     * @return card
     */
    public Spell getCarta(int i){
        return carteMano.get(i);
    }

    /**
     * prints cards in hand
     */
    public void printHand(){
        for(int i = 0; i<sizeMano(); i++){
            System.out.println(i + " "+ carteMano.get(i).getClass().getSimpleName());
        }
    }

    /**
     * checks if there are Instant cards in the hand
     * @return true->there is at least 1 Instant; false->no Instants
     */
    public boolean hasInstant(){
        for(Spell s : carteMano){
            if(s instanceof Instant)
                return true;
        }
        return false;
    }

    /**
     * prints all Instant type cards
     */
    public void printInstantInHand(){
        for(int i = 0; i<sizeMano(); i++){
            if(carteMano.get(i) instanceof Instant)
                System.out.println(i + " "+ carteMano.get(i).getClass().getSimpleName());
        }
    }
}

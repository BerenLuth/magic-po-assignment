package Magic.Personal;

import Magic.Cards.Permanent;
import Magic.Cards.Spell;

import java.util.ArrayList;


public class Field {
    ArrayList<Permanent> field;

    // Inizializzazione a vuoto del field
    public Field(){
    	this.field = new ArrayList<>();
    }

    /**
     * adds a card to the field
     * @param card the card to be added
     */
    public void addCard(Permanent card){
    	field.add(card);
    }

    /**
     * removes all the cards from the field
     */
    public void removeAll(){
    	field.clear();
    }

    /**
     * getter of the field
     * @return the field
     */
    public ArrayList<Permanent> getField(){
        return field;
    }

    /**
     * select a target on the field
     * @param n index of the target
     * @return target
     */
    public Permanent selectTarget(int n){return field.get(n);}

    /**
     * select a target on the field
     * @param card card to be selected
     * @return target
     */
    public Permanent selectTarget(Spell card){return field.get(field.indexOf(card));}

    /**
     * removes the selected card from field
     * @param card target to be removed
     */
    public void remove(Permanent card){
    	field.remove(card);
    }

    /**
     * adds a card to the field
     * @param creature card to be added
     */
    public void add(Permanent creature){
        field.add(creature);
    }
}

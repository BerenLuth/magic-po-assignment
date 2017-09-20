package Magic.Personal;

import Magic.Cards.Spell;
import Magic.GameException.DrawException;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {

    private String name;
    private ArrayList<Spell> deck;

    public Deck(){
        this.name = "Base";
        this.deck = new ArrayList<>();
    }

    public Deck(String name, ArrayList<Spell> deck){
        this.name = name;
        this.deck = deck;
    }

    /**
     * getter of name
     * @return name of the deck
     */
    public String getName() {
        return this.name;
    }

    /**
     * setter of the name of deck
     * @param name name of the deck to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter of the arraylist Deck
     * @return deck
     */
    public ArrayList<Spell> getDeck() {
        return this.deck;
    }

    /**
     *
     * @return the size of deck
     */
    public int sizeDeck(){
        return this.deck.size();
    }

    /**
     * shuffles the deck.
     */
    public void shuffle(){
        Collections.shuffle(deck);
    }

    /**
     * returns the first card of the deck
     * @return first card of deck
     * @throws DrawException no cards on the deck
     */
    public Spell top() throws DrawException {
        if(deck.isEmpty())
            throw new DrawException();
        else
            return deck.remove(0);
        //return deck.isEmpty()?null:deck.remove(0);
    }

    /**
     * extracts the desired card from the deck
     * @param m the card to be extracted
     * @return the card extracted
     */
    public Spell extractSpell(Spell m){
        if(deck.remove(m))     //se la carta è presente la rimuovo
            return m;           //e ritorno un'istanza della stessa
        else
            return null;        //altrimenti ritorno null
    }

    /**
     * adds the card to the deck
     * @param m the card to be added
     */
    public void addSpell(Spell m){
        deck.add(m);
    }

    /**
     * sets the owner of every card in the deck
     * @param owner the player to be set as owner
     */
    public void addCardsOwner(Player owner){
        for(Spell s: deck){
            s.setOwner(owner);
        }
    }
}

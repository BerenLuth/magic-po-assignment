package Cardgame.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class Library {
    private ArrayList<Card> cards = new ArrayList<Card>();
    final private Player owner;

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Library(Player p) {
        owner = p;
    }

    public void add(Card c) {
        cards.add(c);
    }

    public void add(Iterator<Card> deck) {
        while (deck.hasNext())
            cards.add(deck.next());
    }

    public synchronized int deckSize(){
        return cards.size();
    }

    Card draw() {
        if (cards.isEmpty())
            owner.lose("out of Cards");

        return cards.remove(cards.size() - 1);
    }

}

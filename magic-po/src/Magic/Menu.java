package Magic;


import Magic.Cards.*;
import Magic.Cards.Afflict;
import Magic.Game.Game;
import Magic.Personal.*;

public class Menu {

    public static void main(String args[]) {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();

        deck1.addSpell(new Homeopathy());
        deck1.addSpell(new Afflict());
        deck1.addSpell(new BenevolentAncestor());
        deck1.addSpell(new BronzeSable());
        deck1.addSpell(new Homeopathy());
        deck1.addSpell(new NorwoodRanger());
        deck1.addSpell(new Homeopathy());
        deck1.addSpell(new Fatigue());
        deck1.addSpell(new BenevolentAncestor());
        deck1.addSpell(new Fatigue());
        deck1.addSpell(new Homeopathy());
        deck1.addSpell(new Afflict());

        deck2.addSpell(new Fatigue());
        deck2.addSpell(new BenevolentAncestor());
        deck2.addSpell(new BronzeSable());
        deck2.addSpell(new Homeopathy());
        deck2.addSpell(new Afflict());
        deck2.addSpell(new NorwoodRanger());
        deck2.addSpell(new Fatigue());
        deck2.addSpell(new NorwoodRanger());
        deck2.addSpell(new BronzeSable());
        deck2.addSpell(new Homeopathy());
        deck2.addSpell(new Fatigue());
        deck2.addSpell(new Homeopathy());

        Player g1 = new Player("Paperino", deck1);
        Player g2 = new Player("Topolino", deck2);

        Game.getInstance().setGame(g1, g2);       
    }

}

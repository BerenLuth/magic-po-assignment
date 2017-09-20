package cardgame.Cards.ImplementedCards;

import cardgame.Cards.*;
import cardgame.Game.CardGame;
import cardgame.Game.Player;

import java.util.ArrayList;
import java.util.List;

//DONE
public class BronzeSable implements Card {

    private class BronzeSableEffect extends AbstractCreatureCardEffect {
        public BronzeSableEffect(Player p, Card c) { super(p,c); }

        protected Creature create_creature() {
            return new BronzeSableCreature(owner);
        }
    }
    public Effect get_effect(Player p) { return new BronzeSableEffect(p, this); }


    private class BronzeSableCreature extends AbstractCreature {
        ArrayList<Effect> allEffects = new ArrayList<>();
        ArrayList<Effect> tapEffects = new ArrayList<>();

        BronzeSableCreature(Player owner) {
            super(owner);

        }

        @Override
        public String name() {
            return "Bronze Sable";
        }

        @Override
        public int get_power() { return 2; }

        @Override
        public int get_toughness() {
            return 1;
        }

        @Override
        public List<Effect> effects() {
            return allEffects;
        }

        @Override
        public List<Effect> avaliable_effects() {
            return (is_tapped)?tapEffects:allEffects;
        }
    }


    public String name() { return "Bronze Sable"; }
    public String type() { return "Artifact Creature - Sable"; }
    public String rule_text() { return name() + " does nothing"; }
    public String toString() { return name() + "[" + rule_text() +"]";}
    public boolean isInstant() { return false; }

}

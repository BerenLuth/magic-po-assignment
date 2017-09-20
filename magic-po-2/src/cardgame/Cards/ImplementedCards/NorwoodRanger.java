package cardgame.Cards.ImplementedCards;

import cardgame.Cards.*;
import cardgame.Game.CardGame;
import cardgame.Game.Player;

import java.util.ArrayList;
import java.util.List;

//DONE
public class NorwoodRanger implements Card {

    private class NorwoodRangerEffect extends AbstractCreatureCardEffect {
        public NorwoodRangerEffect(Player p, Card c) { super(p,c); }

        protected Creature create_creature() { return new NorwoodRangerCreature(owner); }
    }
    public Effect get_effect(Player p) { return new NorwoodRangerEffect(p, this); }


    private class NorwoodRangerCreature extends AbstractCreature {
        ArrayList<Effect> allEffects = new ArrayList<>();
        ArrayList<Effect> tapEffects = new ArrayList<>();

        NorwoodRangerCreature(Player owner) {
            super(owner);

        }

        @Override
        public String name() { return "Norwood Ranger"; }

        @Override
        public int get_power() {
            return 1;
        }

        @Override
        public int get_toughness() {
            return 2;
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


    public String name() { return "Norwood Ranger"; }
    public String type() { return "Creature - Elf Scout"; }
    public String rule_text() { return name() + " does nothing"; }
    public String toString() { return name() + "[" + rule_text() +"]";}
    public boolean isInstant() { return false; }

}

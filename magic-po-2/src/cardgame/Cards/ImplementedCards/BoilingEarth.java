package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Creature;
import cardgame.Cards.Effect;
import cardgame.Game.Player;

import java.util.ArrayList;

/**
 * Created by Fabio on 22/03/2016.
 */
public class BoilingEarth implements Card{

    private class BoilingEarthEffect extends AbstractCardEffect{
        public BoilingEarthEffect(Player owner, Card c) {
            super(owner,c);
        }
        public void resolve() {
            ArrayList<Creature> creatures = new ArrayList<>();
            creatures.addAll(owner.get_creatures());
            creatures.addAll(opponent.get_creatures());

            for(Creature c : creatures){
                c.inflict_damage(1);
            }
        }
    }

    public Effect get_effect(Player owner) {
        return new BoilingEarthEffect(owner, this);
    }

    @Override
    public String name() {
        return "Boiling Earth";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "Boiling Earth deals 1 damage to each creature";
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public String toString(){
        return name() + " [" + rule_text() + "]";
    }
}

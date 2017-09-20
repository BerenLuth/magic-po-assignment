package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Game.*;
import cardgame.Utils;

/**
 * Created by Daniele on 01/04/2016.
 */

public class Darkness implements Card {


    private class DarknessEffect extends AbstractCardEffect implements TriggerAction {

        protected DarknessEffect(Player p, Card c) {
            super(p, c);
        }

        @Override
        public void resolve() {
            owner.setCombatPrevent(true);
            opponent.setCombatPrevent(true);
            CardGame.instance.get_triggers().register(Phases.END_FILTER, this);
        }

        @Override
        public void execute() {
            owner.setCombatPrevent(false);
            opponent.setCombatPrevent(false);
        }

    }
    @Override
    public Effect get_effect(Player owner) {
        return new DarknessEffect(owner, this);
    }

    @Override
    public String name() {
        return "Darkness";
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String rule_text() {
        return "Prevent all combat damage that would be dealt this turn";
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    public String toString() { return name() + " [" + rule_text() +"]";}
}

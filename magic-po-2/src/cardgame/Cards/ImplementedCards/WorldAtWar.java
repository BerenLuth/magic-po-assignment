package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Creature;
import cardgame.Cards.Effect;
import cardgame.Game.*;
/**
 * Created by valdemar on 04/04/2016.
 */
//DONE
public class WorldAtWar implements Card{

    private class WorldAtWarEffect extends AbstractCardEffect implements TriggerAction{

        protected WorldAtWarEffect(Player p, Card c) {
            super(p, c);
        }
        //  extra turn but skip untap
        @Override
        public void resolve() {
            DefaultCombatPhase.getAttaccanti().forEach(Creature::untap);
            CardGame.instance.get_triggers().register(Phases.MAIN_FILTER, this);
        }

        @Override
        public void execute() {
            (new DefaultCombatPhase()).execute();
            (new DefaultMainPhase()).execute();

        }


    }

    @Override
    public Effect get_effect(Player owner) {
        return new WorldAtWarEffect(owner, this);
    }

    @Override
    public String name() {
        return "World at War";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "After the first postcombat main phase this turn, there's an additional combat phase followed by an additional main phase. At the beginning of that combat, untap all creatures that attacked this turn.";
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public String toString(){
        return name() + "[" + rule_text() +"]";
    }
}

package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Game.*;

/**
 * Created by Sebahhh on 04/04/2016.
 */
//DONE
public class SavorTheMoment implements Card {

    private class SavorTheMomentEffect extends AbstractCardEffect implements TriggerAction{

        protected SavorTheMomentEffect(Player p, Card c) {
            super(p, c);
        }
        //  extra turn but skip untap


        @Override
        public void resolve() {
            //  skip aggiuntivo
            owner.set_phase(Phases.UNTAP, new SkipPhase(Phases.UNTAP));
            CardGame.instance.get_triggers().register(Phases.END_FILTER, this);
        }

        @Override
        public void execute() {
                CardGame.instance.next_player();
        }


    }

    @Override
    public Effect get_effect(Player owner) {
        return new SavorTheMomentEffect(owner, this);
    }

    @Override
    public String name() {
        return "Savor The Moment";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "Take an extra turn after this one; skip the untap phase of that turn.";
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

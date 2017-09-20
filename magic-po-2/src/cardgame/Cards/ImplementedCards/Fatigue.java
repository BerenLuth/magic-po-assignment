package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Cards.Targettable;
import cardgame.Game.Phases;
import cardgame.Game.Player;
import cardgame.Game.SkipPhase;
import cardgame.Utils;

/**
 * Created by Fabio on 29/03/2016.
 */

//DONE
public class Fatigue implements Card {

    private class FatigueEffect extends AbstractCardEffect implements Targettable{
        protected FatigueEffect(Player p, Card c) {
            super(p, c);
        }

        //Salva il giocatore che subirà l'effetto
        Player target;

        @Override
        public boolean play(){
            setTarget();
            return super.play();
        }

        @Override
        public void resolve() {
            SkipPhase skipPhase = new SkipPhase(Phases.DRAW, 1);
            target.set_phase(Phases.DRAW, skipPhase);
        }

        @Override
        public boolean hasTarget(){
            return true;
        }

        @Override
        public boolean setTarget(){
            System.out.println(owner.get_name() + ": Vuoi colpire l'avversario o te stesso? \n1.avversario\n2.te stesso");
            int result = Utils.readIntRange(1,2);
            if(result == 0)
                target = opponent;
            else
                target = owner;
            return true;
        }
    }

    @Override
    public Effect get_effect(Player owner) {
        return new FatigueEffect(owner, this);
    }

    @Override
    public String name() {
        return "Fatigue";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "Target player skip her or his next Draw step";
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public String toString(){
        return name() + "[" + rule_text() +"]";
    }
}

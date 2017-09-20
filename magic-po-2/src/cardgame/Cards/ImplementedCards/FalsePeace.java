package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Cards.Targettable;
import cardgame.Game.Phases;
import cardgame.Game.Player;
import cardgame.Game.SkipPhase;
import cardgame.Game.TriggerAction;
import cardgame.Utils;

/**
 * Created by Fabio on 30/03/2016.
 */
//DONE
public class FalsePeace implements Card{


    private class FalsePeaceEffect extends AbstractCardEffect implements Targettable{
        protected FalsePeaceEffect(Player p, Card c) {
            super(p, c);
        }

        /**
         * target salva la scelta del target
         * 0 per opponent
         * 1 per owner
         */
        Player target;

        @Override
        public boolean play(){
            setTarget();
            return super.play();
        }


        @Override
        public void resolve() {
            SkipPhase skipPhase = new SkipPhase(Phases.COMBAT, 1);
            target.set_phase(Phases.COMBAT, skipPhase);
        }

        @Override
        public boolean hasTarget(){
            return true;
        }


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
        return new FalsePeaceEffect(owner, this);
    }

    @Override
    public String name() {
        return "False Peace";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "Target player skips his next Combat phase";
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public String toString(){
        return name() + "[" + rule_text() +"]";
    }
}

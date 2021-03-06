package cardgame.Cards.ImplementedCards;

import cardgame.Cards.*;
import cardgame.Game.CardGame;
import cardgame.Game.Phases;
import cardgame.Game.Player;
import cardgame.Game.TriggerAction;
import cardgame.Utils;

import java.util.ArrayList;

/**
 * Created by Fabio on 24/03/2016.
 */
public class Afflict implements Card {

    private class AfflictEffect extends AbstractCardEffect implements Targettable, TriggerAction{
        public AfflictEffect(Player p, Card c){
            super(p,c);
        }

        Creature target;

        @Override
        public boolean play(){
            if(this.setTarget())
                return super.play();
            return false;
        }

        @Override
        public void resolve() {
            //tolgo 1 attacco e 1 difesa fino alla fine del turno
            if(target != null) {
                target.setAttackTrigger(-1);
                target.setDamageLeftTrigger(-1);
                CardGame.instance.get_triggers().register(Phases.END_FILTER, this);
                System.out.println("+1/+1 a " + target.name());
            }
            else
                System.out.println("Afflict non ha nessun obbiettivo");
        }


        @Override
        public boolean hasTarget(){
            return true;
        }

        @Override
        public boolean setTarget(){
            //Creo un'array list con tutte le creature in campo
            ArrayList<Creature> creatures = new ArrayList<>();
            creatures.addAll(owner.get_creatures());
            creatures.addAll(opponent.get_creatures());

            //Controllo che ci siano carte in campo
            if(creatures.size()>0){
                System.out.println(owner.get_name() + ": SCegliere la creatura target: ");
                target = Utils.creatureTargetChoice(creatures);   //Scelgo quale sarà la carta target
                return true;
            }
            else{
                System.out.println("Non ci sono carte in campo, afflict non avrà nessun effetto");
                target = null;
                return false;
            }

        }

        @Override
        public void execute() {
            //Se il la carta ha finito il suo effetto lo rimuovo, altrimenti continua
            target.setAttackTrigger(1);
        }
    }


    @Override
    public Effect get_effect(Player owner) {
        return new AfflictEffect(owner, this);
    }

    @Override
    public String name() {
        return "Afflict";
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String rule_text() {
        return " Target creature gets -1/-1 until end of turn";
    }

    @Override
    public boolean isInstant() {
        return true;
    }


    public String toString() { return name() + "[" + rule_text() +"]";}
}

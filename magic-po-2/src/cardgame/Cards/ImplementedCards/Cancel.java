package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Cards.Targettable;
import cardgame.Game.CardGame;
import cardgame.Game.Player;
import cardgame.Utils;

/**
 * Created by valdemar on 24/03/2016.
 */
//DONE
public class Cancel implements Card{

    private class CancelEffect extends AbstractCardEffect implements Targettable{
        protected CancelEffect(Player p, Card c) {
            super(p, c);
        }

        AbstractCardEffect target = null;

        @Override
        public boolean play(){
            if(setTarget())
                return super.play();
            return false;
        }


        @Override
        public void resolve() {
            /**
             * Counterspells the spell that has just been played by the opponent
             */
            if(CardGame.instance.get_stack().contains(target)) {
                if(target != null)
                    for (Effect e : CardGame.instance.get_stack()) {
                        if (target == e) {
                            CardGame.instance.get_stack().remove(e);
                            System.out.println("\tRemoved " + target.getCard().name() + " from stack");
                        }
                    }
                else
                    System.out.println("\tCancel non ha nessun target!");
            } else
                System.out.println("\tLa carta target di Cancel è già stata rimossa!");
        }

        @Override
        public boolean hasTarget(){
            return true;
        }

        @Override
        public boolean setTarget(){
            int i = 1;
            if(CardGame.instance.get_stack().getSize() == 0){
                System.out.println("Nessun target selezionabile");
                return false;
            }
            System.out.println("Queste sono le carte nello stack, scegline una:");
            for(Effect e: CardGame.instance.get_stack()){
                if(e instanceof AbstractCardEffect) {
                    AbstractCardEffect effect = (AbstractCardEffect) e;
                    System.out.println(i + ". " + effect.getCard().name());
                    i++;
                }
            }
            i = Utils.readIntRange(1,CardGame.instance.get_stack().getSize());
            i--;    //l'indice parte da 1 e non da 0 quindi abbasso il valore di i
            for(Effect e: CardGame.instance.get_stack()){
                if(e instanceof AbstractCardEffect) {
                    if(i == 0) {
                        target = (AbstractCardEffect) e;
                        return true;
                    }
                    else
                        i--;
                }
            }
            return false;
        }
    }

    @Override
    public Effect get_effect(Player owner) {
        return new CancelEffect(owner, this);
    }

    @Override
    public String name() {
        return "Cancel";
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String rule_text() {
        return "Counter target spell";
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public String toString(){
        return name() + "[" + rule_text() +"]";
    }
}

package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Cards.Targettable;
import cardgame.Game.CardGame;
import cardgame.Game.Player;
import cardgame.Utils;


/**
 * Created by Fonto on 30/03/16.
 */

public class Deflection implements Card {
    private class DeflectionEffect extends AbstractCardEffect implements Targettable{

        private AbstractCardEffect target = null;
        DeflectionEffect(Player owner,Card c){
            super(owner,c);
        }


        @Override
        public boolean play(){
            setTarget();
            return super.play();
        }

        @Override
        public void resolve() {
            if (target != null){
                if(CardGame.instance.get_stack().contains(target)) {
                    System.out.println("La magia " + target.getCard().name() + " subisce Deflection!");
                    ((Targettable) target).setTarget();
                } else
                    System.out.println("\tLa carta target di Deflection è stata rimossa dallo stack!");
            } else
                System.out.println("\tDeflection non ha nessun target!");
        }

        @Override
        public boolean hasTarget(){
            return true;
        }

        /*@Override
        public boolean setTarget(){
            //ho messo il controllo per vedere se è magia e se è targettabile,
            int limit = 0;
            int risposta;
            int risposta2;
            boolean uCanT = true;

            if(CardGame.instance.get_stack().getSize() == 0){
                System.out.println("Nessun target selezionabile");
                return false;
            }

            System.out.println("Nella pila ci sono le seguenti magie:");
            //Metendo lo stesso controllo su entrambi i cicli, mi assicuro che l'indice coincida
            for (Effect e: CardGame.instance.get_stack() ){
                if( (e instanceof Targettable) && (e instanceof AbstractCardEffect)){   //Stampo solo se è targettabile & magia
                    System.out.println(limit + " " + e.toString());
                    limit++;
                }
            }

            if(CardGame.instance.get_stack().getSize()==0){
                uCanT=false;
                doNothing=true;
                System.out.println("Non ci sono carte nella pila: non fa nulla!");
            }

            while (uCanT){
                System.out.println("Seleziona l'indice della carta da deviare:");
                risposta = Utils.readInt();
                if (risposta>=0 && risposta<limit){
                    //Metendo lo stesso controllo su entrambi i cicli, mi assicuro che l'indice coincida
                    for(Effect e: CardGame.instance.get_stack()) {
                        if( (e instanceof Targettable) && (e instanceof AbstractCardEffect) ){   //Stampo solo se è targettabile & magia
                            if (risposta != 0)
                                risposta--;
                            else {
                                target = (AbstractCardEffect) e;
                                risposta--;
                            }
                        }
                    }
                    if ( target.hasTarget())
                        uCanT = false;
                    else{
                        System.out.println("La carta non ha un bersaglio! Se non la cambi non avrà effetto! Vuoi cambiarla? (1 per si/2 per no)");
                        risposta2 = Utils.readIntRange(1,2);
                        if (risposta2 == 2) {
                            uCanT = false;
                            doNothing = true;
                        }
                        else
                            setTarget();
                    }
                }
            }
            return true;
        }*/

        @Override
        public boolean setTarget(){
            int i = 1;
            if(CardGame.instance.get_stack().getSize() == 0){
                System.out.println("Nessun target selezionabile");
                return false;
            }
            System.out.println("Queste sono le carte nello stack, scegline una:");
            for(Effect e: CardGame.instance.get_stack()){
                if(e instanceof AbstractCardEffect && e instanceof Targettable) {
                    AbstractCardEffect effect = (AbstractCardEffect) e;
                    System.out.println(i + ". " + effect.getCard().name());
                    i++;
                }
            }
            i = Utils.readIntRange(1,CardGame.instance.get_stack().getSize());
            for(Effect e: CardGame.instance.get_stack()){
                if(e instanceof AbstractCardEffect && e instanceof Targettable) {
                    if(i == 1) {
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
        return new DeflectionEffect(owner, this);
    }

    @Override
    public String name() {
        return "Deflection";
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String rule_text() {
        return "Change the target of target spell with a single target";
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

package cardgame.Cards.ImplementedCards;

import cardgame.Cards.*;
import cardgame.Game.Player;
import cardgame.Utils;

/**
 * Created by valdemar on 24/03/2016.
 */

public class VolcanicHammer implements Card{

    private class VolcanicHammerEffect extends AbstractCardEffect implements Targettable{
        public VolcanicHammerEffect(Player p, Card c) {
            super(p, c);
        }

        private Creature targetCreature = null;
        private Player targetPlayer = null;

        @Override
        public boolean play() {
            setTarget();
            return super.play();
        }

        @Override
        public void resolve(){
            if (targetPlayer != null){
                System.out.println("\tInfligge 3 danni a: " + targetPlayer.get_name());
                targetPlayer.inflict_damage(3);
            }
            else{
                System.out.println("\tInfligge 3 danni a: " + targetCreature.name());
                targetCreature.inflict_damage(3);
            }
        }

        @Override
        public boolean hasTarget(){
            return true;
        }

        /*@Override
        public boolean setTarget(){
            System.out.println("Vuoi colpire un giocatore?");
            String risposta = Utils.readString();

            if (risposta.equals("si") ){
                isAPlayer = true;
            }
            else isAPlayer = false;

            if (isAPlayer){
                System.out.println("Vuoi colpire l'altro giocatore?");
                risposta = Utils.readString();

                if (risposta.equals("si") ){
                    targetPlayer = opponent;
                }
                else targetPlayer = owner;

            }
            else{
                int i = 0;
                System.out.println(owner.get_name());
                for(Creature c : owner.get_creatures()){
                    System.out.println(i + " " + c.name());
                    i++;
                }
                System.out.println(opponent.get_name());
                for(Creature c : opponent.get_creatures()){
                    System.out.println(i + " " + c.name());
                    i++;
                }
                System.out.println("Seleziona l'indice della creatura da colpire");
                int index = Utils.readIntRange(i);

                if (index< owner.get_creatures().size()){
                    targetCreature = owner.get_creatures().get(index);
                }
                else{
                    targetCreature = opponent.get_creatures().get(index-owner.get_creatures().size());
                }

            }
            return true;
        }*/

        @Override
        public boolean setTarget(){
            System.out.println("Vuoi colpire un giocatore? (1 si/2 no)");
            if(Utils.readIntRange(1,2) == 1)
                return choosePlayer();
            else
                return chooseCreature();
        }

        public boolean choosePlayer(){
            System.out.println("Quale giocatore vuoi colpire?");
            System.out.println("1. " + owner.get_name());
            System.out.println("2. " + opponent.get_name());
            if(Utils.readIntRange(1,2) == 1)
                targetPlayer = owner;
            else
                targetPlayer = opponent;
            return true;
        }

        public boolean chooseCreature(){
            int i = 0;
            System.out.println(owner.get_name());
            for(Creature c : owner.get_creatures()){
                System.out.println(i + " " + c.name());
                i++;
            }
            System.out.println(opponent.get_name());
            for(Creature c : opponent.get_creatures()){
                System.out.println(i + " " + c.name());
                i++;
            }
            System.out.println("Seleziona l'indice della creatura da colpire");
            int index = Utils.readIntRange(0,i);

            if (index < owner.get_creatures().size()){
                targetCreature = owner.get_creatures().get(index);
            }
            else{
                targetCreature = opponent.get_creatures().get(index-owner.get_creatures().size());
            }
            return true;
        }

    }


    @Override
    public Effect get_effect(Player owner) {
        return new VolcanicHammerEffect(owner,this);
    }

    @Override
    public String name() {
        return "Volcanic Hammer";
    }

    @Override
    public String type() {
        return "Sorcery";
    }

    @Override
    public String rule_text() {
        return "Volcanic Hammer deals 3 damage to any one creature or player";
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

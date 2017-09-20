package cardgame.Cards.ImplementedCards;

/**
 * Created by valdemar on 31/03/2016.
 */

import cardgame.Cards.*;
import cardgame.Game.CardGame;
import cardgame.Game.Player;
import cardgame.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BenevolentAncestor implements Card {

    private class BenevolentAncestorCreature extends AbstractCreature{
        ArrayList<Effect> allEffects = new ArrayList<>();
        ArrayList<Effect> tapEffects = new ArrayList<>();

        protected BenevolentAncestorCreature(Player owner) {
            super(owner);
            tapEffects.add( new Targettable() {
                                Player p = null;
                                Creature c = null;
                                public boolean play() {
                                    setTarget();
                                    CardGame.instance.get_stack().add(this);
                                    return tap();
                                }
                                @Override
                                public boolean setTarget() {
                                    int target;
                                    System.out.println("Vuoi colpir un giocatore?");
                                    String risposta = Utils.readString();
                                    if(risposta.equals("si")){
                                        System.out.println("0 per l'avversario, 1 per colpire te stesso");
                                        target = Utils.readIntRange(1);
                                        if(target==0)
                                            p=owner.getOpponent();
                                        if(target==1)
                                            p=owner;
                                    }
                                    else{
                                        System.out.println("Scegli la creatura da colpire");
                                        int i = 0;
                                        System.out.println(owner.get_name());
                                        for(Creature c : owner.get_creatures()){
                                            System.out.println(i + " " + c.name());
                                            i++;
                                        }
                                        System.out.println(owner.getOpponent().get_name());
                                        for(Creature c : owner.getOpponent().get_creatures()){
                                            System.out.println(i + " " + c.name());
                                            i++;
                                        }
                                        System.out.println("Seleziona l'indice della creatura da colpire");
                                        int index = Utils.readIntRange(i);

                                        if (index< owner.get_creatures().size()){
                                            c = owner.get_creatures().get(index);
                                        }
                                        else{
                                            c = owner.getOpponent().get_creatures().get(index-owner.get_creatures().size());
                                        }
                                    }
                                    return true;
                                }
                                public void resolve() {
                                    if(p!=null) {
                                        p.setDamagePrevent(1);
                                        p = null;
                                    }
                                    else {
                                        c.setDamageLeftTrigger(1);
                                        c=null;
                                    }
                                }

                                public String toString()
                                { return "tap: BenevolentAncestor, prevent the first damage that would be dealt to target creature or player"; }
                            }
            );
        }

        @Override
        public boolean attack(){
            System.out.println("Questa carta è un Defender, non può attaccare.");
            return false;
        }


        @Override
        public String name() { return "Benevolent Ancestor"; }

        @Override
        public int get_power() {
            return 0;
        }

        @Override
        public int get_toughness() {
            return 4;
        }

        @Override
        public List<Effect> effects() {
            return allEffects;
        }

        @Override
        public List<Effect> avaliable_effects() {
            return (!is_tapped)?tapEffects:allEffects;
        }
    }



    //ToDo: implementare l'effetto della carta (prende il primo danno del turno)
    //  guardare Darkness che ha un effetto simile
    //  si ma darkness non l'abbiamo fatta, maremma maiala
    private class BenevolentAncestorCreatureEffect extends AbstractCreatureCardEffect{

        public BenevolentAncestorCreatureEffect(Player owner, Card c) { super(owner,c); }

        @Override
        protected Creature create_creature() {
            return new BenevolentAncestorCreature(owner);
        }


    }

    public Effect get_effect(Player owner) { return new BenevolentAncestorCreatureEffect(owner, this); }
    public String name() { return "Benevolent Ancestor"; }
    public String type() { return "Creature - Spirit"; }
    public String rule_text() { return name() + " Prevent the next 1 damage that would be dealt to target creature or player this turn. "; }
    public String toString() { return name() + "[" + rule_text() +"]";}
    public boolean isInstant() { return false; }}

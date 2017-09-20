package cardgame.Game;

import cardgame.Cards.Card;
import cardgame.Cards.Creature;
import cardgame.Cards.Effect;
import cardgame.Utils;

import java.util.ArrayList;
import java.util.Scanner;


public class DefaultCombatPhase implements Phase {

    private final static ArrayList<Creature> attaccanti = new ArrayList<>();
    private final static ArrayList<Creature> bloccanti = new ArrayList<>();

    @Override
    public void execute() {
        Player current_player = CardGame.instance.get_current_player();
        Player opponent = CardGame.instance.get_current_adversary();

        System.out.println(current_player.get_name() + ": combat phase");
        if (current_player.get_creatures().size() > 0) {
            sceltaAttaccanti(current_player);
            sceltaBloccanti(opponent);
            assegnazioneDanni(opponent, current_player);
        }
        CardGame.instance.get_triggers().trigger(Phases.COMBAT_FILTER);
        // ToDo: sistemare la combat e i vari messaggi
    }

    private void sceltaAttaccanti(Player p) {
        attaccanti.clear();
        System.out.println("dichiara le creature attaccanti");
        // print possibili attaccanti
        for (Creature c : p.get_creatures()) {
            if (c.attack())
                attaccanti.add(c);
        }
        System.out.println("Istantanei dopo assegnazione attaccanti");
        fillStack(p.getOpponent());
    }

    private void sceltaBloccanti(Player p) {
        bloccanti.clear();
        if(!p.get_creatures().isEmpty()) {            
            if (!attaccanti.isEmpty()) {
                System.out.println("Scegli le creature bloccanti");
                int i=0;
                for(Creature c : p.get_creatures()){
                    System.out.println(i+". "+c.name()+c.getAttack()+c.get_toughness());
                    i++;
                }
                for (Creature c : p.get_creatures()) {
                    if (c.defend())
                        bloccanti.add(c);
                }
            }
        }
    }


    private void assegnazioneDanni(Player p, Player b) {
        int scelta;
        ArrayList<Fight> scontri = new ArrayList<>();
        String risposta;
        Fight scontro;
        Creature creature;
        for (Creature c : attaccanti) {
            scontro = new Fight(c, p, b);
            scontri.add(scontro);
            System.out.println("Vuoi bloccare "+c.name()+c.getAttack()+c.get_toughness());
            if (!bloccanti.isEmpty()) {
                risposta = Utils.readString();
                if (risposta.equals("si")) {
                    System.out.println("Con chi vuoi bloccare " + c.name());
                    int i;
                    while (risposta.equals("si") && !bloccanti.isEmpty()) {
                        System.out.println("scegli tra le seguenti creature");
                        i = 0;
                        for (Creature b1 : bloccanti) {
                            System.out.println(i + " " + b1.name());
                            i++;
                        }
                        scelta = Utils.readIntRange(bloccanti.size() - 1);
                        creature = bloccanti.get(scelta);
                        scontro.addDifensore(creature);
                        bloccanti.remove(scelta);
                        System.out.println("Vuoi scegliere altre creature?");
                        risposta = Utils.readString();
                    }
                }
                scontro.choosePriority();
            }
        }
        System.out.println("Istantanei dopo assegnazione bloccanti");
        fillStack(p);
        for (Fight s : scontri) {
            s.execute();
        }
        scontri.clear();
    }

    private class Fight {
        private ArrayList<Creature> difensori;
        private Creature attaccante;
        private Player difensore;
        private Player attacco;

        public Fight(Creature attaccante, Player difensore, Player attacco) {
            this.difensore = difensore;
            this.attacco = attacco;
            this.attaccante = attaccante;
            this.difensori = new ArrayList<>();
        }

        private void addDifensore(Creature c) {
            difensori.add(c);
        }

        private void execute() {
            if (difensori.isEmpty()) {
                System.out.println(attaccante.name()+" attacca diretto");
                if(!difensore.isCombatPrevent())
                    difensore.inflict_damage(attaccante.getAttack());
                else
                    System.out.println(difensore.get_name()+" previene il danno ricevuto dalle creature");
            } else {
                int dannoAttaccante = attaccante.getAttack();
                int sup = 0;
                for (Creature c : difensori) {
                    if (dannoAttaccante > 0 && !difensore.isCombatPrevent()) {
                        sup = c.getDamageLeft();
                        if (dannoAttaccante > (sup))
                            c.inflict_damage(dannoAttaccante - (dannoAttaccante - sup));
                        else
                            c.inflict_damage(dannoAttaccante);
                    }
                    else
                        System.out.println("il danno da combattimento viene prevenuto");
                    if (!attacco.isCombatPrevent())
                        attaccante.inflict_damage(c.getAttack());
                    dannoAttaccante -= sup;
                }

            }
        }
        private void choosePriority() {
            if (difensori.size() > 1) {
                ArrayList<Creature> sup = new ArrayList<>();
                int i;
                int target;
                System.out.println("Scegli con che priorità si svolgerà lo scontro tra "+attaccante.name()+"e bloccanti");
                while (!difensori.isEmpty()) {
                    i=0;
                    for (Creature c : difensori) {
                        System.out.println(i + ". " + c.name());
                        i++;
                    }
                    sup.add(difensori.get(target = Utils.readIntRange(difensori.size()-1)));
                    difensori.remove(target);
                }
                difensori = sup;
            }
        }
    }
    private void fillStack(Player p){
        int numpass=0;
        Player sup = p;
        while(numpass<2){
            if(play_available_effect(sup))
                numpass=0;
            else numpass++;
            sup=p.getOpponent();
        }
        CardGame.instance.get_stack().resolve();
    }
    private boolean play_available_effect(Player active_player) {
        //collect and display available effects...
        ArrayList<Effect> available_effects = new ArrayList<>();
        Scanner reader = CardGame.instance.get_scanner();

        //...cards first
        System.out.println(active_player.get_name() + " select card/effect to play, 0 to pass");
        int i=0;
        for( Card c:active_player.get_hand() ) {
            if (c.isInstant() ) {
                available_effects.add( c.get_effect(active_player) );
                System.out.println(Integer.toString(i+1)+") " + c );
                ++i;
            }
        }

        //...creature effects last
        for ( Creature c:active_player.get_creatures()) {
            for (Effect e:c.avaliable_effects()) {
                available_effects.add(e);
                System.out.println(Integer.toString(i+1)+") " + c.name() + "["+ e + "]" );
                ++i;
            }
        }

        //get user choice and play it
        int idx= reader.nextInt()-1;
        if (idx<0 || idx>=available_effects.size()) return false;

        available_effects.get(idx).play();
        return true;
    }

    public static ArrayList<Creature> getAttaccanti() {
        return attaccanti;
    }

    public static ArrayList<Creature> getBloccanti() {
        return bloccanti;
    }
}

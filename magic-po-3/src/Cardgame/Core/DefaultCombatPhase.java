package Cardgame.Core;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Controller.Observers.MsgCenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class DefaultCombatPhase extends AbstractPhase {

    private class CombatEntry {
        public DecoratedCreature attacker;
        public ArrayList<DecoratedCreature> defenders=new ArrayList<>();
    }
    
    public void execute() {
        final Player current_player = CardGame.instance.getCurrentPlayer();
        final Player adversary = CardGame.instance.getCurrentAdversary();
        final int current_player_idx = (CardGame.instance.getPlayer(0) == current_player)?0:1;
        ArrayList<CombatEntry> combat = new ArrayList<>();
        int active_player;
        int number_passes;

        notifyObservers(current_player.name() + ": combat phase");
        
        CardGame.instance.getTriggers().trigger(Triggers.COMBAT_FILTER);

        // declare attackers
        if (!current_player.getCreatures().isEmpty()) {
            System.out.println(current_player.name() + " choose attackers");
            ArrayList<DecoratedCreature> potential_attackers = new ArrayList<>();
            int i = 1;
            for (DecoratedCreature c : current_player.getCreatures()) {
                if (c.canAttack()) {
                    System.out.println(i + ") " + c);
                    potential_attackers.add(c);
                    ++i;
                }
            }
            ArrayList<Integer> creatureList = null;
            try {
                creatureList = new ArrayList<>();
                int idx;
                do {
                    TargetRequest req = new TargetRequest(potential_attackers, 1);
                    Requests.instance.addRequest(req);
                    idx = Requests.instance.getResult().get(0);
                    creatureList.add(idx);
                    if (idx > -1)
                        potential_attackers.remove(idx);
                } while (idx != -1);
            } catch (InterruptedException e) {
            }

            for (Integer idx : creatureList) {
                if (idx >= 0 && idx < potential_attackers.size()) {
                    CombatEntry entry = new CombatEntry();
                    entry.attacker = potential_attackers.get(idx);
                    entry.attacker.attack();
                    combat.add(entry);
                }
            }
        }


            
        notifyObservers("Attacker declaration response subphase");
        //allow other player to respond first
        active_player = (current_player_idx+1)%2;
        number_passes=0;
        while (number_passes<2) {
            notifyObservers(CardGame.instance.getPlayer(active_player).name() + ": instants subphase");
            System.out.println("prima di available request");
            if (Utilities.play_available_effect(CardGame.instance.getPlayer(active_player),false))
                number_passes=0;
            else ++number_passes;
            
            active_player = (active_player+1)%2;
        }

        CardGame.instance.getStack().resolve();
        
        HashSet<DecoratedCreature> defenders = new HashSet<>();
        
        // declare defenders
        if (!adversary.getCreatures().isEmpty()) {
            for (CombatEntry e: combat) {
                if (e.attacker.isRemoved()) continue;
                System.out.println(adversary.name() + " choose creatures defending from " + e.attacker );
                ArrayList<DecoratedCreature> potential_defenders=new ArrayList<>();
                int i=1;
                for (DecoratedCreature c:adversary.getCreatures()) {
                    if (c.canDefend() && !defenders.contains(c)) {
                        System.out.println(i + ") " + c);
                        potential_defenders.add(c);
                        ++i;
                    }
                }
                ArrayList<Integer> creatureList = null;
                try {
                    creatureList = new ArrayList<>();
                    int idx;
                    do {
                        TargetRequest req = new TargetRequest(potential_defenders, 1);
                        Requests.instance.addRequest(req);
                        idx = Requests.instance.getResult().get(0);
                        creatureList.add(idx);
                        if (idx > -1)
                            potential_defenders.remove(idx);
                    } while (idx != -1);
                } catch (InterruptedException err) {}

                for (Integer idx : creatureList) {
                    if (idx>=0 && idx<potential_defenders.size()) {
                        DecoratedCreature defender=potential_defenders.get(idx);
                        if (!defenders.contains(defender)) {
                            defender.defend();
                            e.defenders.add(defender);
                            defenders.add(defender);
                        }
                    }
                }
            }
        }
        
        System.out.println("Defender declaration response subphase");
        //allow priority player to respond first
        active_player = current_player_idx;
        number_passes=0;
        while (number_passes<2) {
            notifyObservers(CardGame.instance.getPlayer(active_player).name() + ": instants subphase");
            if (Utilities.play_available_effect(CardGame.instance.getPlayer(active_player),false))
                number_passes = 0;
            else ++number_passes;

            active_player = (active_player+1)%2;
        }
        CardGame.instance.getStack().resolve();
        
        
        //distribute damage
        notifyObservers("Damage distribution subphase");
        for (CombatEntry e:combat) {
            if (e.attacker.isRemoved()) continue;
            
            boolean is_blocked=false;
            int defender_damage=0;
            int attacker_residual_power=e.attacker.power();
            
            for (DecoratedCreature defender:e.defenders) {
                if (defender.isRemoved()) continue;
                is_blocked=true;
                
                //compute damage attacker inflict to this defender
                int attacker_damage = Math.min(attacker_residual_power, 
                        defender.toughness()-defender.getDamage() );
                //accumulate defender damage
                defender_damage += Math.max(0,defender.power());
                
                notifyObservers(e.attacker + " deals " + attacker_damage + " damage to " + defender);
                notifyObservers(defender + " deals " + attacker_damage + " damage to " + e.attacker);
                //inflict damage to defender
                if (attacker_damage>0) {
                    defender.inflictDamage(attacker_damage);
                    attacker_residual_power-=attacker_damage;
                }
            }
            
            
            if (!is_blocked) {
                //inflict damage to adversary player
                notifyObservers(e.attacker + " deals " + e.attacker.power() + " damage to " + adversary.name());
                adversary.inflictDamage(Math.max(0,e.attacker.power()));
            } else {
                //inflict cumulative damage to attacker
                defender_damage = Math.min(defender_damage, 
                        e.attacker.toughness()-e.attacker.getDamage() );
                e.attacker.inflictDamage(defender_damage);
            }
            
        }
        
    }
    
}

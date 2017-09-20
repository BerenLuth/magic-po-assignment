package Cardgame.Core;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;

import java.util.ArrayList;
import java.util.List;


// utility static methods for common behavior
public abstract class Utilities extends Observable{
     
    // looks for all playable effects from Cards in hand and creatures in play
    // and asks player for which one to play
    // includes creatures and sorceries only if is_main is true
    public static boolean play_available_effect(Player active_player, boolean is_main) {
        //collect and display available effects...
        ArrayList<Effect> available_effects = new ArrayList<>();
        ArrayList<Card> closeList = new ArrayList<>();

        //...Cards first
        int i=0;
        for( Card c:active_player.getHand() ) {
            if ( is_main || c.isInstant() ) {
                available_effects.add( c.getEffect(active_player) );
                c.setPossibleTarget();
                c.setTargetIndex(i++);
                closeList.add(c);
            }
        }

        //...creature effects lasts
        for ( Creature c:active_player.getCreatures()) {
            for (Effect e:c.avaliableEffects()) {
                available_effects.add(e);

            }
        }

        TargetRequest request = new TargetRequest(available_effects,1);
        Requests.instance.addRequest(request,active_player);
        //get user choice and play it
        try {
            List<Integer> answer = Requests.instance.getResult();
            if (answer != null) {
                int idx = answer.get(0);
                System.out.println("utilities idx: " + idx);
                for (Card c : closeList){
                    c.endTargeting();
                }
                if (idx < 0 || idx >= available_effects.size()) return false;

                // Gioco la carta o l'effetto
                available_effects.get(idx).play();
            }
            else {
                return false;
            }
        } catch (InterruptedException azz){
            return false;
        }
        return true;
    }
    
    
    
}

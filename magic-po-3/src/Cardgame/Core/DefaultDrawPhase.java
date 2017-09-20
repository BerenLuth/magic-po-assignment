package Cardgame.Core;


import Cardgame.Controller.Observers.PhaseObserver;

public class DefaultDrawPhase extends AbstractPhase {


    public void execute() {
        Player current_player = CardGame.instance.getCurrentPlayer();
        
        notifyObservers(current_player.name() + ": draw phase");
        PhaseObserver.phaseObserver.update();
        // Notifica che è cominciata la DrawPhase

        CardGame.instance.getTriggers().trigger(Triggers.DRAW_FILTER);
        //addOption richiamata dentro a Player.drawCardInHand()
        current_player.draw();

        while(current_player.getHand().size() > current_player.getMaxHandSize()) {
            // Aspetta che la GUI abbia ritirato le carte pescate.
            // Nella discard viene riempita la options per le carte che dovranno essere scartate.
            int i = current_player.getHand().size() - current_player.getMaxHandSize();
            String s = (i > 1) ? "s" : "";
            notifyObservers(current_player.name() + ": must discard " + i + " card" + s);
            current_player.selectDiscard();
        }

    }
}

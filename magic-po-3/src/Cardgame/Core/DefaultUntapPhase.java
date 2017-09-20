package Cardgame.Core;

public class DefaultUntapPhase extends AbstractPhase {
    
    public void execute(){
        Player current_player = CardGame.instance.getCurrentPlayer();
        
        notifyObservers(current_player.name() + ": untap phase");

        CardGame.instance.getTriggers().trigger(Triggers.UNTAP_FILTER);
        
        if (current_player.getCreatures().isEmpty())
            notifyObservers("...no creatures to untap");
        
        for(Creature c:current_player.getCreatures()) {
            notifyObservers("...untap " + c.name());
            c.untap();
        }
    }
}
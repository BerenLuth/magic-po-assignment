package Cardgame.Core;



public class DefaultEndPhase extends AbstractPhase {
    
    public void execute() {
        Player current_player = CardGame.instance.getCurrentPlayer();

        notifyObservers(current_player.name() + ": end phase");

        for(Creature c:current_player.getCreatures()) {
            notifyObservers("...reset damage to " + c.name());
            c.resetDamage();
        }
        
        for(Creature c:CardGame.instance.getCurrentAdversary().getCreatures()) {
            notifyObservers("...reset damage to adversary creature " + c.name());
            c.resetDamage();
        }
        
        CardGame.instance.getTriggers().trigger(Triggers.END_FILTER);
        notifyObservers("~~-----o-------o-----~~");
    }
    
}

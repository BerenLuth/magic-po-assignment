package cardgame;


public class DefaultEndPhase implements Phase {
    
    public void execute() {
        Player current_player = CardGame.instance.getCurrentPlayer();
        
        System.out.println(current_player.name() + ": end phase");
        
        for(Creature c:current_player.getCreatures()) {
            System.out.println("...reset damage to " + c.name());
            c.resetDamage();
        }
        
        for(Creature c:CardGame.instance.getCurrentAdversary().getCreatures()) {
            System.out.println("...reset damage to adversary creature " + c.name());
            c.resetDamage();
        }
        
        CardGame.instance.getTriggers().trigger(Triggers.END_FILTER);
    }
    
}

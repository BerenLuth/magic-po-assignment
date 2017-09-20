package cardgame.Game;


import cardgame.Cards.Creature;

public class DefaultEndPhase implements Phase {
    
    public void execute() {
        Player current_player = CardGame.instance.get_current_player();
        
        System.out.println(current_player.get_name() + ": end phase");
        
        CardGame.instance.get_triggers().trigger(Phases.END_FILTER);
        current_player.resetDamagePrevent();
        current_player.getOpponent().resetDamagePrevent();
        for(Creature c:current_player.get_creatures()) {
            System.out.println("...reset damage to " + c.name());
            c.reset_damage();

            // resetTrigger ora utilizza l'interfaccia TriggerAction, se non funziona, togliere i commenti qui
            //c.resetTrigger();   //controlla quanti turni rimangono al trigger e in caso lo resetta
        }
        
        for(Creature c:CardGame.instance.get_current_adversary().get_creatures()) {
            System.out.println("...reset damage to adversary creature " + c.name());
            c.reset_damage();

            // resetTrigger ora utilizza l'interfaccia TriggerAction, se non funziona, togliere i commenti qui
            //c.resetTrigger();   //controlla quanti turni rimangono al trigger e in caso lo resetta
        }
    }
    
}

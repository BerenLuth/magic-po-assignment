package Cardgame.Core;


public class DefaultTurnManager extends AbstractTurnManager {

    private final Player[] Players;
    int current_player_idx=1;
    
    public DefaultTurnManager(Player[] p) {
        super();
        Players=p;
    }
    
    public Player getCurrentPlayer() { return Players[current_player_idx]; }
    
    public Player getCurrentAdversary() { return Players[(current_player_idx+1)%2]; }
    
    public Player nextPlayer() { 
        current_player_idx = (current_player_idx+1)%2;
        notifyObservers(getCurrentPlayer());
        return getCurrentPlayer();
    }
}

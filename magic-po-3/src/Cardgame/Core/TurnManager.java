package Cardgame.Core;


public interface TurnManager {
    Player getCurrentPlayer();
    
    Player getCurrentAdversary();
    
    Player nextPlayer();
}
package Cardgame.Core;

import Cardgame.Controller.Observers.PhaseObserver;


public abstract class AbstractTurnManager extends Observable implements TurnManager {

    public AbstractTurnManager(){
        this.addObserver(PhaseObserver.phaseObserver);
    }

}

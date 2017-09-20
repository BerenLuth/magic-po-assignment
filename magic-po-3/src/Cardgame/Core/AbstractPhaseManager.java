package Cardgame.Core;

import Cardgame.Controller.Observers.PhaseObserver;

public abstract class AbstractPhaseManager extends Observable implements PhaseManager {

    public AbstractPhaseManager(){
        this.addObserver(PhaseObserver.phaseObserver);
    }

}

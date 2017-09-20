package Cardgame.Core;

import Cardgame.Controller.Observers.MsgCenter;


public abstract class AbstractPhase extends Observable implements Phase{

    public AbstractPhase(){
        this.addObserver(MsgCenter.msgCenter);
    }

}

package Cardgame.Core;

import Cardgame.Controller.Observer;


public interface ObservableInterface {

    void addObserver(Observer obs);

    void addMsgObserver(Observer obs);

    void removeObserver(Observer obs);

    void notifyObservers();

    void notifyMsg();

    void notifyMsg(Object arg) throws InterruptedException;

    void notifyObservers(Object arg) throws InterruptedException;

    void setChanged();

    boolean getChanged();
}

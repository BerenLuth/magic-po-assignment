package Cardgame.Core;

import Cardgame.Controller.Observer;
import java.util.ArrayList;

public abstract class Observable implements ObservableInterface{

    protected ArrayList<Observer> observers = new ArrayList<Observer>();
    protected ArrayList<Observer> msgObservers = new ArrayList<Observer>();

    boolean changed = false;
    String message = null;



    public void addObserver(Observer obs){
        this.observers.add(obs);
    }

    public void addMsgObserver(Observer obs) {
        this.msgObservers.add(obs);
    }

    public void removeObserver(Observer obs){
        this.observers.remove(obs);
    }

    public void notifyObservers(){
        for(Observer obs: observers)
            obs.update();
    }

    public void notifyMsg(){
        for(Observer obs: msgObservers)
            obs.update();
    }

    public void notifyMsg(Object arg) {
        for(Observer obs: msgObservers)
            try {
                obs.update(arg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void notifyObservers(Object arg) {
        for(Observer obs: observers)
            try {
                obs.update(arg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    public void setChanged(){
        changed = true;
    }

    public boolean getChanged(){
        return changed;
    }

}

package Cardgame.Controller.Observers;

import Cardgame.Controller.Observer;


public abstract class AbstractObserver<T> implements Observer<T>{

    @Override
    public synchronized void update() {
        notifyAll();
    }

    @Override
    public synchronized void update(T arg) {

    }
}

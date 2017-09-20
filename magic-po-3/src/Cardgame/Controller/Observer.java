package Cardgame.Controller;

public interface Observer<T> {

    void update();
    void update(T arg) throws InterruptedException;
}

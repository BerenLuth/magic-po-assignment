package Cardgame.Controller.Observers;

import Cardgame.Controller.Observer;

public class MsgCenter implements Observer, Runnable{
    private String message;

    private boolean changed;


    public static MsgCenter msgCenter = new MsgCenter();

    public static MsgCenter getMsgCenter(){
        return msgCenter;
    }

    private MsgCenter(){
        this.message = null;
        changed = false;
    }



    public synchronized void run() {}

    public synchronized void setState(String str){
        this.message = str;
        changed = true;
        notifyAll();

    }

    public synchronized String getState(){
        while(changed == false)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        changed = false;
        notifyAll();
        return this.message;
    }


    public void update() {

    }

    public void update(Object arg) {
        setState((String) arg);
        changed = true;
    }
}

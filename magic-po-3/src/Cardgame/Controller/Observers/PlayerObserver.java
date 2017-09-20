package Cardgame.Controller.Observers;

import Cardgame.Controller.Observer;
import Cardgame.Core.Player;
import Cardgame.GUI.GameGUI;

public class PlayerObserver implements Observer<Player>, Runnable {
    private static Player player1;
    private static int life1;
    private static int size1;
    private static boolean newLife1;
    private static boolean newDeck1;
    private static Player player2;
    private static int life2;
    private static int size2;
    private static boolean newLife2;
    private static boolean newDeck2;
    private static boolean canStop;


    public static PlayerObserver playerObserver = null;

    public static PlayerObserver getPlayerObserver(Player pl){
        if(playerObserver == null)
            playerObserver = new PlayerObserver(pl);
        else
            addSecondPlayer(pl);
        return playerObserver;
    }

    private static void addSecondPlayer(Player pl2){
        pl2.addObserver(playerObserver);
        player2 = pl2;
        life2 = player2.getLife();
        size2 = player2.getDeck().deckSize();
        newLife2 = true;
        newDeck2 = true;
        GameGUI.instance.startPlayer2ObserverThread();
    }

    private PlayerObserver(Player pl1){
        pl1.addObserver(this);
        player1 = pl1;
        life1 = player1.getLife();
        size1 = player1.getDeck().deckSize();
        newLife1 = true;
        newDeck1 = true;
        canStop = false;
        GameGUI.instance.startPlayer1ObserverThread();
    }

    public static boolean isReady(){
        if(player1 != null && player2 != null)
            return true;
        return false;
    }

    public synchronized String getLife1(){
        while(!newLife1)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        newLife1 = false;
        notifyAll();
        return "" + life1;
    }

    public synchronized String getDeckSize1(){
        while(!newDeck1)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        newDeck1 = false;
        notifyAll();
        return "" + size1;
    }

    public synchronized String getDeckSize2(){
        while(!newDeck2)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        newDeck2 = false;
        notifyAll();
        return "" + size2;
    }

    public synchronized String getLife2(){
        while(!newLife2)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        newLife2 = false;
        notifyAll();
        return "" + life2;
    }

    public void stopThread(){
        canStop = true;
    }


    @Override
    public void update() {

    }

    @Override
    public synchronized void update(Player arg) throws InterruptedException {
        if(arg == player1){
            life1 = player1.getLife();
            newLife1 = true;
            newDeck1 = true;
            size1 = player1.getDeck().deckSize();
            notifyAll();
        }
        else{
            life2 = player2.getLife();
            size2 = player2.getDeck().deckSize();
            newLife2 = true;
            newDeck2 = true;
            notifyAll();
        }

    }

    @Override
    public void run() {}
}

package Cardgame.Controller.GUIRequests;

import Cardgame.Core.Observable;
import Cardgame.Core.Player;
import Cardgame.GUI.GameGUI;

import java.util.List;

/**
 * classe che raccoglie le richieste effettuate da effetti/fasi
 * deve avere funzione di monitor
 */
public class Requests extends Observable implements Runnable{
    /*in genere dovrebbe esserci un solo elemento qui dentro, ma con la deflect potrei averne due*/
    public TargetRequest actualRequest;
    private TargetRequest newRequest;
    private Player askingPlayer;

    Thread t = new Thread(this);

    public List<Integer> result;

    public static Requests instance = new Requests();


    //aggiunge una nuova richiesta
    public void addRequest(TargetRequest request) throws NotDefinedPlayerException{
        newRequest = request;
        if (askingPlayer == null){
            throw new NotDefinedPlayerException();
        }
        System.out.println("Parte thread della request");
        t = new Thread(this);
        t.start();
    }

    public void addRequest(TargetRequest request, Player askingPlayer) {
        System.out.println("Invio request da " + askingPlayer.name());
        this.askingPlayer = askingPlayer;
        try {
            addRequest(request);
        } catch (NotDefinedPlayerException e) {
            System.out.println("NON STA GIOCANDO NESSUN PLAYER!");
        }
    }

    public List<Integer> getResult() throws InterruptedException {
        System.out.println("aspetto risultato request");
        t.join();
        System.out.println("Ricevuto risultato request");
        return result;
    }

    public Player getAskingPlayer(){
        return askingPlayer;
    }

    public void setResult(List<Integer> lst){
        this.result = lst;
    }

    @Override
    public void run() {
        System.out.println("Inizio esecuzione thread request");
        Requests.instance.actualRequest = newRequest;
        Requests.instance.actualRequest.initTargets();

        GameGUI.instance.sendRequest(newRequest);
        System.out.println("Chiamo metodo della GUI");
        //metodo della gui per pigliare il request e rispondere

        System.out.println("aspetto qui");
        actualRequest.getIndexes();
        System.out.println("fine attesa");
    }
}

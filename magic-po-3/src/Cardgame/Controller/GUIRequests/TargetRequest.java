package Cardgame.Controller.GUIRequests;


import Cardgame.Controller.Observers.MsgCenter;
import Cardgame.Core.Observable;
import Cardgame.Core.Targetable;

import java.util.ArrayList;
import java.util.List;

/**
 * il pickTarget crea un'istanza di TatgetRequest che viene mandata alla view per poter leggere i target possibili e permettere di
 * sceglierli
 */
public class  TargetRequest extends Observable{
    private List<Targetable> targets = new ArrayList<>(); /*si, così perchè può avere creature, player, magie nello stack e altre cose che possono essere implementate più avanti*/
    private List<Targetable> possibleTargets; /*vale quel che sta scritto sopra*/
    private int targetsNumber = -1; /*numero di target da scegliere, se <0 posso ritornare quanti target voglio*/
    private List<Integer> indexes; /*indici dei target da scegliere (anche se non so se è il metodo più comodo) */
    private boolean answered = false;
    private boolean pickedAnswer = false;

    /*
     * NOTA BENE: se non si risponde alla TargetRequest IL GIOCO NON DEVE POTER PROSEGUIRE
     */

    /**
     * crea una nuova richiesta e setta i parametri targettabili
     * @param possibleTargets lista di elementi sceglibili
     * @param maxTargets numero di target richiesti
     */
    public TargetRequest(List possibleTargets, int maxTargets){
        this.possibleTargets = possibleTargets;
        this.targetsNumber = maxTargets;
        System.out.println("new request2" + maxTargets);
        this.addObserver(MsgCenter.msgCenter);
        System.out.println("new request2");
    }

    /**
     * crea una nuova richiesta senza specificare il numero di target
     * @param possibleTargets
     */
    public TargetRequest(List possibleTargets){
        this.possibleTargets = possibleTargets;
        this.targetsNumber = -1;
        System.out.println("new request1");
        this.addObserver(MsgCenter.msgCenter);
        System.out.println("new request1");

    }

    /**
     * crea una nuova richiesta senza specificare nulla
     * non dovrebbe fare nulla
     */
    public TargetRequest(){
        System.out.println("new request0");
    }

    public List getTargets() throws InterruptedException{
        //aspetta che venga impostato un target
        return targets;
    }

    public synchronized List<Integer> getIndexes(){
        //aspetta che venga impostato un target
        System.out.println("viene chiamata getIndexes e aspetta risposta");
        while(!answered)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println("getIndexes finisce di aspettare e ritorna gli indici");
        notifyAll();
        setPickedAnswer();
        return indexes;
    }

    public synchronized void answerRequest(List<Integer> indexes) {
        System.out.println("rispondo");
        System.out.println("Il request risponde (anserRequest)");
        Requests.instance.setResult(indexes);
        //Requests.instance.result = new ArrayList<>(indexes);
        answered = true;

        close();
        notifyAll();
        System.out.println("vengono notificati della risposta");
    }


    //metodi che usa la GUI per leggere
    public List getPossibleTargets(){
        return possibleTargets;
    }

    public int getTargetsNumber(){
        return targetsNumber;
    }


    //segna i target come selezionabili (serve alla GUI)
    public synchronized void initTargets(){
        System.out.println("inizializzo target dalla request");
        int i = 0;
        for (Targetable t : possibleTargets){
            System.out.println(i + ": "+ t.toString() );
            t.setPossibleTarget();
            t.setTargetIndex(i);
            i++;
        }
    }

    //resetta i target
    public void close(){
        for(Targetable t: possibleTargets){
            t.endTargeting();
        }
    }

    public void setPickedAnswer(){
        pickedAnswer = true;
    }

}


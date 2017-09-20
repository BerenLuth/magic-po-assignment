package Cardgame.Controller.Observers;

import Cardgame.Core.*;
import Cardgame.GUI.GameGUI;

public class PhaseObserver extends AbstractObserver<Player> {
    private Player currentPlayer;
    private PhaseManager currentPhaseManager;
    private Phases currentPhase;
    private static boolean changes = true;

    public static PhaseObserver phaseObserver = new PhaseObserver();


    public void update() {
        currentPlayer = CardGame.instance.getCurrentPlayer();
        currentPhaseManager = currentPlayer.getPhaseManager();
        currentPhase = currentPhaseManager.currentPhase();
        GameGUI.instance.nextPhase();
    }

    public void update(Player arg){
        currentPlayer = arg;
        currentPhaseManager = currentPlayer.getPhaseManager();
        currentPhase = currentPhaseManager.currentPhase();
        GameGUI.instance.nextTurn();

    }

    public Phases getCurrentPhase(){
        return currentPhaseManager.currentPhase();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

}

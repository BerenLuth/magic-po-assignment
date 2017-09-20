package Cardgame.Controller.Observers;

import Cardgame.Core.CardGame;
import Cardgame.Core.Creature;
import Cardgame.Core.DecoratedCreature;
import Cardgame.GUI.GameGUI;

import java.util.List;

/**
 * Classe che si occupa di osservare i cambiamenti tra le creature
 * esempi: -creatura evocata/distrutta
 *         -creatura attaccata
 *         -creatura effettata
 *         -creatura tappata?
 */
public class CreatureObserver extends AbstractObserver<Creature> {
    private List<DecoratedCreature> p1creatures = CardGame.instance.getPlayer(0).getCreatures();
    private List<DecoratedCreature> p2creatures = CardGame.instance.getPlayer(1).getCreatures();

    public static CreatureObserver observer = new CreatureObserver();

    @Override
    public synchronized void update(){
        //qui la gui dovrebbe rileggere le mani del giocatore e aggiornarsi
        System.out.println("CreatureObserver.update");  //Messaggio di log per vedere se qua arriva

        GameGUI.instance.setField1(p1creatures);
        GameGUI.instance.setField2(p2creatures);

        GameGUI.instance.drawCardInField();


        //---------------------------------------------------
        super.update();
    }
}

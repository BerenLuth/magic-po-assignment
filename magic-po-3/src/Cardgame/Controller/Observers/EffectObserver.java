package Cardgame.Controller.Observers;

import Cardgame.Core.CardGame;
import Cardgame.Core.Effect;
import Cardgame.GUI.GameGUI;

import java.util.ArrayList;

/**
 * Classe che dovrebbe occuparsi di osservare lo stack e i suoi effetti
 * esempi: -effetto creato
 *         -effetto risolto/cancellato
 *         -eventuali cambi di target
 */
public class EffectObserver extends AbstractObserver<Effect>{
    public static EffectObserver observer = new EffectObserver();

    ArrayList<Effect> stack = CardGame.instance.getStack().getStack();

    @Override
    public synchronized void update() {
        System.out.println("EffectObserver.update");  //Messaggio di log per vedere se qua arriva

        GameGUI.instance.setStack(stack);
        GameGUI.instance.drawStack();

        super.update();
    }

}
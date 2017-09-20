package Cardgame.Controller.Observers;

import Cardgame.Core.Card;
import Cardgame.Core.CardGame;
import Cardgame.GUI.GameGUI;

import java.util.List;

/**
 * Osserva cambiamenti tra le carte e li notifica alla gui
 * esempi: -carta giocata/pescata/eliminata
 *         -attivazione e disattivazione di un effetto (decorazione)
 */
public class CardObserver extends AbstractObserver<Card> {
    public static CardObserver observer = new CardObserver();

    private List<Card> p1Cards = CardGame.instance.getPlayer(0).getHand();
    private List<Card> p2Cards = CardGame.instance.getPlayer(1).getHand();

    @Override
    public synchronized void update(){
        //qui la gui dovrebbe rileggere le mani del giocatore e aggiornarsi
        System.out.println("CardObserver.update");  //Messaggio di log per vedere se qua arriva
        GameGUI.instance.setHand1(p1Cards);
        GameGUI.instance.setHand2(p2Cards);
        GameGUI.instance.drawCardInHand( GameGUI.P2);
        GameGUI.instance.drawCardInHand( GameGUI.P1);

        //---------------------------------------------------
        super.update();
    }
}

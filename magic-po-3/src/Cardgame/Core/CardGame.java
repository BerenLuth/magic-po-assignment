package Cardgame.Core;

import Cardgame.Controller.Observers.MsgCenter;
import Cardgame.GUI.MainGUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;


/*
Signleton class maintaining global game properties.
Handles:
 - Players
 - Turns
 - Stack
 - Triggers
*/
public class CardGame extends Observable implements Runnable{

    private final Player[] Players = new Player[2];
    private final Deque<TurnManager> turn_manager_stack = new ArrayDeque<TurnManager>();


    public static void main(String[] args) {

        try {
            Thread msgCenter = new Thread(MsgCenter.getMsgCenter());
            msgCenter.setName("msgCenter");
            msgCenter.start();

            MsgCenter.msgCenter.setState("Known Cards: " + CardFactory.size());
            for (String s : CardFactory.known_cards()) {
                //notifyObservers(s);
            }

            MainGUI mainGUI = new MainGUI();
            Thread t = new Thread(mainGUI);
            t.setName("MainGUI");
            t.start();

        } catch (RuntimeException e) {
            MsgCenter.msgCenter.setState(e.getMessage());
        }
    }

    //Singleton and instance access
    public static final CardGame instance = new CardGame();

    /* DROPPED
    public void setup()
    */

    //setup modificata in modo che l'interfaccia grafica possa passare i valori
    public void setup(String nameOne, String deckOne, String nameTwo, String deckTwo) {
        Players[0].setName(nameOne);
        Players[1].setName(nameTwo);

        ArrayList<Card> p1deck = new ArrayList<>();
        try {
            Scanner p1deckFile = new Scanner(new File(deckOne));
            while (p1deckFile.hasNextLine()) {
                p1deck.add(CardFactory.construct(p1deckFile.nextLine()));
            }
        } catch (IOException ex) {
            throw new RuntimeException("cannot read player 1's deck file ");
        }
        Players[0].setDeck(p1deck.iterator());

        ArrayList<Card> p2deck = new ArrayList<>();
        try {
            Scanner p1deckFile = new Scanner(new File(deckTwo));
            while (p1deckFile.hasNextLine()) {
                p2deck.add(CardFactory.construct(p1deckFile.nextLine()));
            }
        } catch (IOException ex) {
            throw new RuntimeException("cannot read player 1's deck file");
        }
        Players[1].setDeck(p2deck.iterator());

    }


    //game setup
    private CardGame() {

        this.addObserver(MsgCenter.msgCenter);
        turn_manager_stack.push(new DefaultTurnManager(Players));

        Players[0] = new Player();
        Players[0].setName("Player 1");


        Players[1] = new Player();
        Players[1].setName("Player 2");
    }

    //execute game
    public void run() {
        Players[0].getDeck().shuffle();
        Players[1].getDeck().shuffle();

        for (int i = 0; i != 5; ++i) Players[0].draw();
        for (int i = 0; i != 5; ++i) Players[1].draw();

        Players[0].setPhase(Phases.DRAW, new SkipPhase(Phases.DRAW));

        try {
            while (true) {
                instance.nextPlayer().executeTurn();
            }
        } catch (EndOfGame e) {
            notifyObservers(e.getMessage());
        }
    }


    // Player and turn management

    public void setTurnManager(TurnManager m) {
        turn_manager_stack.push(m);
    }

    public void removeTurnManager(TurnManager m) {
        turn_manager_stack.remove(m);
    }

    public Player getPlayer(int i) {
        return Players[i];
    }

    public Player getCurrentPlayer() {
        return turn_manager_stack.peek().getCurrentPlayer();
    }

    public Player getCurrentAdversary() {
        return turn_manager_stack.peek().getCurrentAdversary();
    }

    public Player nextPlayer() {
        return turn_manager_stack.peek().nextPlayer();
    }


    // Stack access
    private CardStack stack = new CardStack();

    public CardStack getStack() {
        return stack;
    }


    //Trigger access
    private Triggers triggers = new Triggers();

    public Triggers getTriggers() {
        return triggers;
    }

}

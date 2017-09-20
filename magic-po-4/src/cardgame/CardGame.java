package cardgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
Signleton class maintaining global game properties.
Handles: 
 - Players
 - Turns
 - Stack
 - Triggers
*/
public class CardGame {

  
    public static void main(String[] args) {
    
        try {
            System.out.println("Known cards: " + CardFactory.size());
            for (String s:CardFactory.known_cards()) {
                System.out.println(s);
            }
            

            
            /*
            //create decks
            ArrayList<Card> deck = new ArrayList<Card>();
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Deflection"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Afflict"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Aggressive Urge"));
            for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Benevolent Ancestor"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Day of Judgment"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Cancel"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Boiling Earth"));
            for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Darkness"));
            //for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("World at War"));
            for (int i=0; i!=5; ++i) deck.add(CardFactory.construct("Volcanic Hammer"));

            instance.getPlayer(0).setDeck(deck.iterator());
            instance.getPlayer(1).setDeck(deck.iterator());
            */
            
            instance.setup();

            instance.run();
        } catch(RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    
    //Signleton and instance access
    public static final CardGame instance = new CardGame();
    
    
    public void setup() {
        System.out.println("Please write player 1's name");
        Players[0].setName(reader.nextLine());
        
        ArrayList<Card> p1deck = new ArrayList<Card>();
        System.out.println( Players[0].name() + " give the filename of your deck");
        try {
            Scanner p1deckFile = new Scanner(new File(reader.nextLine()));
            while(p1deckFile.hasNextLine()) {
                p1deck.add(CardFactory.construct(p1deckFile.nextLine()));
            }
        } catch (IOException ex) { throw new RuntimeException("cannot read player 1's deck file" ); }
        Players[0].setDeck(p1deck.iterator());



        System.out.println("Please write player 2's name");
        Players[1].setName(reader.nextLine());
        
        ArrayList<Card> p2deck = new ArrayList<Card>();
        System.out.println( Players[1].name() + " give the filename of your deck");
        try {
            Scanner p2deckFile = new Scanner(new File(reader.nextLine()));
            while(p2deckFile.hasNextLine()) {
                p2deck.add(CardFactory.construct(p2deckFile.nextLine()));
            }
        } catch (IOException ex) { throw new RuntimeException("cannot read player 1's deck file" ); }
        Players[1].setDeck(p2deck.iterator());    
    }
    
    
    //game setup 
    private CardGame() { 
        turn_manager_stack.push( new DefaultTurnManager(Players) );
        
        Players[0]=new Player();
        Players[0].setName("Player 1");
        
        
        Players[1]=new Player();
        Players[1].setName("Player 2");
    }
    
    //execute game
    public void run() {
        Players[0].getDeck().shuffle();
        Players[1].getDeck().shuffle();
                
        for (int i=0; i!=5; ++i) Players[0].draw();
        for (int i=0; i!=5; ++i) Players[1].draw();

        Players[0].setPhase(Phases.DRAW,new SkipPhase(Phases.DRAW));
        
        try {
            while (true) { instance.nextPlayer().executeTurn(); }
        } catch(EndOfGame e) {
            System.out.println(e.getMessage());
        } 
    }
    
    
    // Player and turn management
    private final Player[] Players = new Player[2];
    private final Deque<TurnManager>  turn_manager_stack = new ArrayDeque<TurnManager>();
    public void setTurnManager(TurnManager m) { turn_manager_stack.push(m); }
    public void removeTurnManager(TurnManager m) { turn_manager_stack.remove(m); }
    
    public Player getPlayer(int i) { return Players[i]; }    
    public Player getCurrentPlayer() { return turn_manager_stack.peek().getCurrentPlayer(); }
    public Player getCurrentAdversary() { return turn_manager_stack.peek().getCurrentAdversary(); }
    public Player nextPlayer() { return turn_manager_stack.peek().nextPlayer(); }
    
    
    // Stack access
    private CardStack stack = new CardStack();
    public CardStack getStack() { return stack; }
    
    
    //Trigger access
    private Triggers triggers=new Triggers();
    public Triggers getTriggers() { return triggers; }
    
    
    //IO resources  to be dropped in final version
    private Scanner reader = new Scanner(System.in);
    public Scanner getScanner() { return reader; }
}

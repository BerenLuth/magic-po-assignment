package Magic.Personal;

import Magic.Cards.Command;
import Magic.Cards.Permanent;
import Magic.Cards.Spell;
import Magic.Game.PhaseManager;
import Magic.Game.GameStack;
import Magic.Game.*;
import Magic.GameException.DrawException;
import Magic.GameException.EndException;
import Magic.GameException.LifeException;

import java.util.ArrayList;

public class Player {
    private Player opponent;
    private String name;
    private int life;
    private Deck deck;
    private Hand hand;
    private Field field;
    private PhaseManager gf;

    public Player(String name, Deck deck){
        this.name = name;
        this.deck = deck;
        this.field = new Field();
        this.hand = new Hand();
        this.gf = new PhaseManager(this);
        this.life = 20;
    }

    /**
     * getter of Hand
     * @return hand
     */
    public Hand getHand() {return this.hand;}

    /**
     * getter of the opponent
     * @return the opponent
     */
    public Player getOpponent(){return this.opponent;}

    /**
     * setter of the opponent
     * @param opponent player to be set a sopponent
     */
    public void setOpponent(Player opponent) {this.opponent = opponent;}

    /**
     * getter of the deck
     * @return deck of the player
     */
    public Deck getDeck() {return this.deck;}

    /**
     * getter of PhaseManager
     * @return PhaseManager of the player
     */
    public PhaseManager getGF(){return this.gf;}

    /**
     * getter of Field
     * @return field of the player
     */
    public Field getField() {
        // Questo cambiamento dipende dagli utilizzi che ne faremmo di Campo...
        // Se ci servirà avere Campo come oggetto bisogna lasciare com'era... al momento va bene cosi
        return this.field;
    }

    /**
     * sets the starting deck of the player
     */
    public void startingDeck(){
        this.deck.shuffle(); // Inoltra responsabilità di shuffle al Deck
        this.deck.addCardsOwner(this);  // imposta il proprietario di ogni carta
    }

    /**
     * starts the turn of the player
     * @throws EndException end of game
     */
    public void startPhases() throws EndException {
        gf.start();
    }

    /**
     * received damage
     * @param damage amount of damage to be received
     * @throws LifeException
     */
    public void receiveDamage(int damage) throws LifeException {
        this.life -= damage;
        if(this.life <= 0){
            throw new LifeException(this);
        }
    }

    /**
     * add card to stack
     * @param m card to be added
     */
    public void playSpell(Spell m){
        GameStack.getInstance().addMagia(m);
    }

    /**
     * add card to stack
     * @param i index of the card to be added
     */
    public void playSpell(int i){
        Spell card = getHand().getCarta(i);
        GameStack.getInstance().addMagia(card);
    }

    /**
     * draws a card
     * @throws EndException end of game - no more cards to draw
     */
    public void draw() throws EndException{
        Spell temp;

        //Prova a pescare, se sono finite le carte lancia la EndException
        try{
            temp = deck.top();
        }catch (DrawException de){
            throw new EndException(this);
        }

        if(temp!=null){            
            System.out.println(name + " pesca: " + temp.getClass().getSimpleName());
            hand.addCarta(temp);
        }
        else{
            Game.getInstance().EndGame(this);
        }
        
    }

    /**
     * initial draw
     * @throws EndException end of game - no more cards to draw
     */
    public void initDraw() throws EndException{
        int i = 5;
        while (i>0){
            this.draw();
            i--;
        }
    }

    /**
     * check if any card needs to be discarded
     * @return number of cards
     */
    public int toDiscard(){
        return getHand().toDiscard(); }

    /**
     * removes a card
     * @param i index of the card to be removed
     */
    public void removeCarta(int i){ // Inoltro messaggio per conto di DrawPhase
        getHand().removeCarta(i);
    }

    /**
     * add a command at the beginning of the turn
     * @param c command to be added
     * @param targetPhase the phase on which the command will be added
     * @return list of commands
     */
    public ArrayList<Command> addBeginCommand(Command c, Class targetPhase) {
        return gf.addBeginCommand(c, targetPhase);
    }

    /**
     * add a command at the end of the turn
     * @param c command to be added
     * @param targetPhase the phase on which the command will be added
     * @return list of commands
     */
    public ArrayList<Command> addEndCommand(Command c, Class targetPhase){
        return gf.addEndCommand(c, targetPhase);
    }

    /**
     * solves the damage
     */
    public void solveDamage(){
        for(Permanent p: field.getField())
            p.solveDamage();
    }

    /**
     * getter of Name
     * @return name of the player
     */
    public String getName() {
        return name;
    }
}

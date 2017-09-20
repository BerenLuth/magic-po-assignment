package Magic.Game;// Il compito di Magic.Game.Turn è alternare la partenza del turno del G1 e del G2

import Magic.GameException.EndException;
import Magic.Personal.Player;

import java.util.ArrayList;

public class Turn {
    private ArrayList<Player> giocatori;
    int turno = 0;
    private PhaseManager gf;


    public Turn(Player one, Player two) {
        this.giocatori = new ArrayList<>();
        this.giocatori.add(one);
        this.giocatori.add(two);
    }

    /**
     * starts the execution of turns. Ex: starts player 1 turn, when it ends starts player 2 turn and so on
     */
    public void start(){
        while (!Game.getInstance().isEnd()) {
            System.out.println("\n*** Comincia il turno di " + giocatori.get(turno % 2).getName() + " ***");
            try{
                giocatori.get(turno % 2).startPhases();
                nextTurn();
            }catch (EndException e){
                Game.getInstance().setEnd();
            }
        }
        System.out.println("Fine del gioco\n");
    }

    /**
     * repeats the turn of the player
     */
    public void repeateTurn() {
        nextTurn();
    }

    /**
     * next turn
     */
    private void nextTurn() {
        this.turno++;
    }
}

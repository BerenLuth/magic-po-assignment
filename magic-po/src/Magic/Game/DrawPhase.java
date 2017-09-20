package Magic.Game;

import Magic.Cards.Command;
import Magic.GameException.EndException;
import Magic.Personal.Player;
import Magic.Utils.Reader;

import java.util.ArrayList;

public class DrawPhase extends Phase {


    /**
     * starts this phase execution
     * @param g the player who is running the phase
     * @throws EndException end of game
     */
    public void start(Player g) throws EndException {
        System.out.println("\tInizia la DrawPhase");
        execute(getBegin());
        g.draw();
        discard(g);
        GameStack.getInstance().resolve();
        execute(getEnd());
    }

    /**
     * checks the card limit and deals with discarding outnumbering cards
     * @param g the target player
     */
    private void discard(Player g){
        int toDiscard = g.toDiscard(); // DrawPhase --> Player --> Hand.toDiscard()

        if(toDiscard > 0)
            System.out.println("Hai troppe carte in mano!! Scegli"+toDiscard+"da scartare");

        while(toDiscard > 0){
            int n = Reader.readIntRange(g.getHand().sizeMano());
            g.removeCarta(n);       // DrawPhase --> Player --> Hand.removeCarta(i)
            toDiscard--;
        }
    }
}
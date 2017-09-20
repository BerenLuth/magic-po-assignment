package Magic.Game;

import Magic.Cards.Command;
import Magic.Personal.Player;

import java.util.ArrayList;


public class EndPhase extends Phase {


    /**
     * starts this phase execution
     * @param g the player who is running the phase
     */
    public void start(Player g){
        System.out.println("\tInizia la EndPhase");
        execute(getBegin());
        GameStack.getInstance().resolve();
        g.solveDamage();
        g.getOpponent().solveDamage(); // EndPhase -> Player --(Campo)>> Permanent.solveDamage
        execute(getEnd());
    }
}
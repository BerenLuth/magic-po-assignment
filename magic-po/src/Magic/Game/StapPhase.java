package Magic.Game;
import Magic.Cards.*;
import Magic.Personal.Field;
import Magic.Personal.Player;

import java.util.ArrayList;

public class StapPhase extends Phase {

    /**
     * starts this phase execution
     * @param g the player who is running the phase
     */
    public void start(Player g){
        System.out.println("\tInizia la StapPhase");
        execute(getBegin());
        int i;
        Field field = g.getField();
        ArrayList<Permanent> campo = field.getField();
        int dim = campo.size();
        for(Permanent p: campo)
            p.untap();
        GameStack.getInstance().resolve();
        execute(getEnd());
    }
 
}
package Magic.Game;

import Magic.Cards.Command;
import Magic.GameException.EndException;
import Magic.Personal.Player;
import Magic.Utils.Reader;
import Magic.Utils.Time;

import java.util.ArrayList;

public class PhaseManager {
    private ArrayList<PhaseInterface> phases;
    private Player player;

    public PhaseManager(Player g){
        this.player = g;
        this.phases = new ArrayList<>();
        this.phases.add(new DrawPhase());
        this.phases.add(new StapPhase());
        this.phases.add(new CombatPhase());
        this.phases.add(new MainPhase());
        this.phases.add(new EndPhase());
    }
    
    /**
     * the next phase
     *@param actual actual phase
     *@return next phase
     */
    public PhaseInterface nextPhase(PhaseInterface actual){        
        return phases.get(phases.indexOf(actual)+1);
    }

    public void removePhase(PhaseInterface type){
       // phases.remove(getPhase(type));
    }

    /**
     * adds the command to be invoked at the beginning of the phase
     * @param c command to be added
     * @param targetPhase the target phase of the command
     */
    public ArrayList<Command> addBeginCommand(Command c, Class targetPhase) {
        return getPhase(targetPhase).addBeginCommand(c);
    }

    /**
     * adds the command to be invoked at the end of the phase
     * @param c command to be added
     * @param targetPhase the target phase of the command
     */
    public ArrayList<Command> addEndCommand(Command c, Class targetPhase){
        return getPhase(targetPhase).addEndCommand(c);
    }
    
    public PhaseInterface getPhase(Class c){
        for(PhaseInterface ph: phases)
            if(c.isInstance(ph)) {
                 return ph;
            }
        return null;
    }

    /**
     * replaces the faces given as input
     * @param old the phase to be replaced
     * @param nevv the new phase
     */
    public void replacePhase(PhaseInterface old, PhaseInterface nevv){

        phases.set( phases.indexOf(old), nevv );

    }

    /**
     * starts the execution of the list of phases.
     * @throws EndException end of game
     */
    public void start() throws EndException {
        for(PhaseInterface p : phases){
            p.start(player);
            Time.sleep();
        }
    }


}

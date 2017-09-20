package Magic.Cards;


import Magic.Personal.Player;

import java.util.ArrayList;


public abstract class Sorcery implements Spell {
    private Player owner;
    private ArrayList<Command> save;
    public Sorcery(){
    }

    /**
     * Effect of the card (NULL)
     */
    @Override
    public void effect(){}

    /**
     * Set owner of the card
     * @param owner owner of the card
     */
    public void setOwner(Player owner){
        this.owner = owner;
    }

    /**
     * Getter of the owner.
     * @return owner
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * add command (end of the effect) to the beginning of the target phase
     * @param c command to be added
     * @param targetPhase target phase
     * @param p target player
     */

    /**
     * Getter of the save.
     * @return save
     */
    public ArrayList<Command> getSave() {
        return save;
    }

    public void setSave(ArrayList<Command> save) {
        this.save = save;
    }

    public static ArrayList<Command> addBeginCommand(Command c, Class targetPhase, Player p) {
        return p.addBeginCommand(c, targetPhase);
    }


    /**
     * add command (end of the effect) to the ending of the target phase
     * @param c command to be added
     * @param targetPhase target phase
     * @param p target player
     */
    public static ArrayList<Command> addEndCommand(Command c, Class targetPhase, Player p){
    	return p.addEndCommand(c, targetPhase);
    }
}

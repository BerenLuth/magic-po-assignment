package Magic.Cards;


import Magic.Personal.Player;

import java.util.ArrayList;


public abstract class Instant implements Spell {

    private Player owner;
    private ArrayList<Command> save;
    public Instant(){
    }

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
     * Getter of the save.
     * @return save
     */
    public ArrayList<Command> getSave() {
        return save;
    }

    /**
     * Set save of the card
     * @param save owner of the card
     */
    public void setSave(ArrayList<Command> save) {
        this.save = save;
    }

    /**
     * Effect of the card (NULL)
     */
    @Override
    public void  effect(){}

    public static ArrayList<Command> addBeginCommand(Command c, Class targetPhase, Player p) {
        return p.addBeginCommand(c, targetPhase);
    }

    public static ArrayList<Command> addEndCommand(Command c, Class targetPhase, Player p){
        return p.addEndCommand(c, targetPhase);
    }
}

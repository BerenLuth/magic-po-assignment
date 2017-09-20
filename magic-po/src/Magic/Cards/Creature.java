package Magic.Cards;


import Magic.GameException.EndException;
import Magic.Personal.Player;


public abstract class Creature implements Spell, Permanent{
    private int attacco;
    private int costituzione;
    private int damage;
    private boolean tapped;
    private Player owner;

    public Creature(int attacco, int costituzione){
        this.attacco=attacco;
        this.costituzione=costituzione;
        this.damage=0;
    }

    /**
     * Set owner of the card
     * @param owner owner of the card
     */
    public void setOwner(Player owner) {this.owner = owner;}

    /**
     * Getter of the owner.
     * @return owner
     */
    public Player getPlayer() {return owner;}

    /**
     * setter of costituzione
     * @param costituzione parameter to be set as costituzione
     */
    public void setCostituzione(int costituzione) {
        this.costituzione = costituzione;
        if(costituzione<=0)
            die();
    }

    /**
     * getter of costituzione
     * @return costituzione
     */
    public int getCostituzione() {return costituzione;}

    /**
     * setter of attack
     * @param attacco parameter to be set as attacco
     */
    public void setAttacco(int attacco) {this.attacco = attacco;}

    /**
     * getter parameter attack
     * @return attacco
     */
    public int getAttacco() {return attacco;}

    /**
     * setter damage of the card
     * @param damage parameter to be set as damage
     */
    public void setDamage(int damage) {this.damage = damage;}

    /**
     * getter parameter damage
     * @return damage of the card
     */
    public int getDamage() {return this.damage;}

    /**
     * getter parameter tapped
     * @return parameter tapped
     */
    public Boolean isTapped(){return this.tapped;}

    /**
     * Set tapped to true (Tap the card)
     */
    public void tap(){tapped=true;}

    /**
     * Set tapped to false (Untap the card
     */
    public void untap(){tapped = false;}

    /**
     * Effect of the card (NULL)
     */
    @Override
    public void effect(){}

    /**
     * deals damage to creature
     * @param c the target creature
     */
    public void dealDamage(Creature c){
        c.receiveDamage(this.attacco);
    }

    /**
     * deals damage to player
     * @param g the target player
     * @throws EndException end of game
     */
    public void dealDamage(Player g) throws EndException{
        g.receiveDamage(this.attacco);
    }

    /**
     * add creature to field
     */
    public void joinBattlefield(){
        owner.getField().add(this);
    }

    /**
     * receive damage
     * @param damage damage to be received
     */
    public void receiveDamage(int damage){
        this.damage += damage;
        if(this.damage>=this.costituzione)
            die();
    }

    /**
     * calculate total damage to be received
     */
    public void solveDamage(){
        this.costituzione -= this.damage;
        this.damage = 0;
    }

    /**
     * destroy creature, remove from game
     */
    public void die(){
        System.out.println(this.getClass()+"+ morto");
        owner.getField().remove(this);
    }


}

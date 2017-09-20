package cardgame.Cards;

import cardgame.Game.Player;
import cardgame.Utils;

// utility clas implementing common defaul behavior and fields for creatures
// creatures with differenf behavior from the default nee not extend it
public abstract class AbstractCreature implements Creature {
    protected Player owner;
    protected boolean is_tapped=false;
    protected int damageLeft = get_toughness();
    protected int attack = get_power();
        
        protected AbstractCreature(Player owner) { this.owner=owner; }
        
        public boolean tap() { 
            if (is_tapped) {
                System.out.println("creature " + name() + " already tapped");
                return false;
            }
            
            System.out.println("tapping creature " + name());
            is_tapped=true; 
            return true; 
        }
    public int getDamageLeft(){return damageLeft;}
    public Player getOwner() {
        return owner;
    }

    public int getAttack() {
        return attack;
    }

    public boolean untap() {
            if (!is_tapped) {
                System.out.println("creature " + name() + " not tapped");
                return false;
            }
            
            System.out.println("untapping creature " + name());
            is_tapped=false; 
            return true; 
        }
        
        public boolean isTapped() { return is_tapped; }
        public boolean attack() {
            if(!isTapped()){
                String risposta;
                System.out.println("Vuoi attaccare con"+name()+"? (Rispondere si o no)");                
                risposta = Utils.readString();
                if (risposta.equals("si"))
                    return true;
                else 
                    return false;
            }
            return false;            
        } // to do in assignment 2
        public boolean defend() {
            if (!isTapped()) {
                String scelta;
                System.out.println("Vuoi bloccare con" + name() + "? (Rispondere si o no)");
                scelta = Utils.readString();
                if (scelta.equals("si"))
                    return true;
                else
                    return false;
            }
            return false;
        }    // to do in assignment 2
        public void inflict_damage(int dmg) { 
            damageLeft -= dmg;
            if (damageLeft <=0)
                owner.destroy(this);        
        }
        
        public void reset_damage() { damageLeft = get_toughness(); }


    public void setAttackTrigger(int i){
        this.attack = get_power() + i;
        if(attack<0)
            attack=0;
    }
    public void setDamageLeftTrigger(int i){
        this.damageLeft += i;
        if (damageLeft <=0)
            owner.destroy(this);
    }


}

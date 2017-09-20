package Cardgame.Core;

// utility class implementing common default behavior and fields for creatures

import Cardgame.Controller.Observers.CreatureObserver;

import java.util.List;
import java.util.ArrayList;

// creatures with differenf behavior from the default nee not extend it
public abstract class AbstractCreature extends Observable implements Creature {
    protected Player owner;
    protected boolean isTapped=false;
    protected int damage = 0;
    protected DecoratedCreature topDecorator;
        
    protected AbstractCreature(Player owner) {
        addObserver(CreatureObserver.observer);
        this.owner=owner;
    }

    public void tap() { 
        if (isTapped) {
            message = "creature " + topDecorator.name() + " already tapped";
            notifyMsg(message);
        } else {
            message = "tapping creature " + topDecorator.name();
            notifyMsg(message);
            isTapped=true;
            notifyObservers();
        }
    }

    public void untap() { 
        if (!isTapped) {
            message = "creature " + topDecorator.name() + " not tapped";
            notifyMsg(message);
        } else {
            message = "untapping creature " + topDecorator.name();
            notifyMsg(message);
            isTapped=false; 
        }
        notifyObservers();
    }

    public boolean isTapped() { return isTapped; }
    public boolean canAttack() { return !isTapped; }
    public void attack() { topDecorator.tap(); } 
    public boolean canDefend() { return !isTapped; }
    public void defend() {} 

    public void inflictDamage(int dmg) { 
        damage += dmg; 
        if (damage >= topDecorator.toughness())
            topDecorator.destroy();
        notifyObservers();
    }
    public int getDamage() { return damage; }


    public void destroy() {
        topDecorator.remove();
        notifyObservers();
    }
    public void onRemove() {
    }

    public void resetDamage() { damage = 0; }

    public void setTopDecorator(DecoratedCreature c) {
        topDecorator = c;
        if (damage >= topDecorator.toughness())
            topDecorator.destroy();
        notifyObservers();
    }
    public DecoratedCreature getTopDecorator() { return topDecorator; }


    public String toString() {
        return topDecorator.name() + "(" + topDecorator.power() + "/" + topDecorator.toughness() + ")";
    }
    
    public boolean isRemoved() { return topDecorator.isRemoved(); }
    
    
    public List<Effect> effects() { return new ArrayList<>(); }
    public List<Effect> avaliableEffects() { return new ArrayList<>(); }

    //targeting management
    private boolean possibleTarget = false;
    private int targetIndex;


    @Override
    public boolean isPossibleTarget() {
        return possibleTarget;
    }

    @Override
    public int getTargetIndex() {
        return targetIndex;
    }

    @Override
    public void setTargetIndex(int i) {
        targetIndex = i;
    }

    @Override
    public void setPossibleTarget() {
        System.out.println(this.getClass() + ": selezionabile come target");
        possibleTarget = true;
    }

    @Override
    public void endTargeting() {
        System.out.println(this.getClass() + ": non più selezionabile");
        possibleTarget = false;
    }
        
}

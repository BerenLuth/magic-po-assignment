
package Cardgame.Core;

import Cardgame.Controller.Observers.CardObserver;
import Cardgame.Controller.Observers.MsgCenter;

import java.util.List;


public class DecoratedCreature extends Observable implements Damageable, Creature{
    private Creature decorated;
    private Player owner;
    
    public DecoratedCreature(Player o, Creature c) {
        owner=o;
        decorated=c;
        decorated.setTopDecorator(this);
        addObserver(CardObserver.observer);
        addObserver(MsgCenter.msgCenter);
    }
    
    public void addDecorator(CreatureDecorator d) {
        decorated=d.decorate(decorated);
        if (getDamage()>=toughness()) remove();
        notifyObservers();
    }
    

    public void remove() { 
        owner.getCreatures().remove(this); 
        notifyObservers("removing " + name() + " from field");
        onRemove();
        CardGame.instance.getTriggers().trigger(Triggers.EXIT_CREATURE_FILTER);
    }
    public void onRemove() { 
        decorated.onRemove(); 
        decorated=null;
    }
    public boolean isRemoved() { return decorated==null; }
    public void accept(GameEntityVisitor v) { v.visit(this); }
    
    public String name() { return decorated.name(); }
    public int power() { return Math.max(0,decorated.power()); }
    public int toughness() { return Math.max(0,decorated.toughness()); }
    
    public void tap() { decorated.tap(); }
    public void untap() { decorated.untap(); }
    public boolean isTapped() { return decorated.isTapped(); }
    
    public boolean canAttack() { return decorated.canAttack(); }
    public void attack() { decorated.attack(); }
    public boolean canDefend() { return decorated.canDefend(); }
    public void defend() { decorated.defend(); }
    public void inflictDamage(int dmg) { owner.inflictDamage(decorated, dmg); }
    public int getDamage() { return decorated.getDamage(); }
    public void resetDamage() { decorated.resetDamage(); }
    public void destroy() { decorated.destroy(); }
    
    public List<Effect> effects() { return decorated.effects(); }
    
    public List<Effect> avaliableEffects() { return decorated.avaliableEffects(); }
    

    public void removeDecorator(CreatureDecorator d) { 
        if (decorated instanceof CreatureDecorator) {
            decorated=((CreatureDecorator)decorated).removeDecorator(d);  
            if (getDamage() >= toughness())
                destroy();
        }      
    }

    public void setTopDecorator(DecoratedCreature c) {}
    public DecoratedCreature getTopDecorator() { return this; }
    
    public String toString() { return decorated.toString(); }

    //targeting management
    private boolean possibleTarget;
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

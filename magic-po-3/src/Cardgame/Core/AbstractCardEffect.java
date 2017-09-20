
package Cardgame.Core;

import Cardgame.Controller.Observers.EffectObserver;

// utility class implementing code common to all effects linked with Cards:
// remove card from hand and place the effect on the stack on play
public abstract class AbstractCardEffect extends AbstractEffect {
    
    protected Player owner;
    protected Card card;
    
    protected AbstractCardEffect(Player p, Card c) {
        owner=p;
        card=c;
        addObserver(EffectObserver.observer);
    }
    
    public boolean play() { 
        owner.getHand().remove(card);
        card.remove();
        return super.play();
    }
    
    public String name() { return card.name(); }
    
    public String toString() { return card.toString(); }

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
        System.out.println("True");
    }

    @Override
    public void endTargeting() {
        System.out.println(this.getClass() + ": non più selezionabile");
        possibleTarget = false;
    }
    
}


package cardgame.Cards;

import cardgame.Game.Player;

// utility class implementing code common to all effects linked with cards:
// remove card from hand and place the effect on the stack on play
public abstract class AbstractCardEffect extends AbstractEffect {
    
    protected Player owner;
    protected Player opponent;
    protected Card card;
    
    protected AbstractCardEffect(Player p, Card c) {
        owner=p;
        card=c;
        opponent = owner.getOpponent();
    }
    
    public boolean play() { 
        owner.get_hand().remove(card);
        return super.play();
    }
    
    public String toString() { return card.toString();}

    public void setOwner(Player owner){
        this.owner=owner;
        this.opponent = this.owner.getOpponent();
    }

    public Card getCard(){
        return this.card;
    }

    public boolean hasTarget(){
        return false;
    }
}

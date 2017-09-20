package Cardgame.Cards;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Core.*;

import java.util.ArrayList;

public class Cancel extends AbstractCard {
    static private final String cardName = "Cancel";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new Cancel(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Instant"; }
    public String ruleText() { return "Counter target spell"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return true; }

    @Override
    public String getBackground() {
        return "image/Cancel.jpg";
    }

    private class CancelEffect extends AbstractCardEffect implements TargetingEffect {
        Effect target;
        
        public CancelEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [Counter " + target.name() + "]";
        }
        
        public void pickTarget() {
            //CardGame.instance.getModel().sendToModel( owner.name() + ": choose target for " + name() );
            CardStack stack = CardGame.instance.getStack();

            ArrayList<Effect> targets = new ArrayList<>();
            //creo lista di target
            for (Effect e : stack){
                targets.add(e);
            }

            TargetRequest request = new TargetRequest(targets,1);
            Requests.instance.addRequest(request);

            int idx= 0;
            try {
                idx = Requests.instance.getResult().get(0);
            if (idx<0 || idx>=stack.size()) target=null;
            else target=stack.get(idx);
            } catch (InterruptedException e) {
                target = null;
            }
        }
        
        public void resolve() {
            if (target==null) {
                //CardGame.instance.getModel().sendToModel(cardName + " has no target");
            } else if (CardGame.instance.getStack().indexOf(target)==-1) {
                //CardGame.instance.getModel().sendToModel(cardName + " target is not on the stack anymore");
            } else {
                //CardGame.instance.getModel().sendToModel(cardName + " removing " + target.name() + " from stack");
                CardGame.instance.getStack().remove(target);
            }
        }
    }

    public Effect getEffect(Player owner) { 
        return new CancelEffect(owner, this); 
    }
}


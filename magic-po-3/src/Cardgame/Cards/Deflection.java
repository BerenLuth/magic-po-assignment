package Cardgame.Cards;

import java.util.ArrayList;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Core.*;

public class Deflection extends AbstractCard {
    static private final String cardName = "Deflection";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new Deflection(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Instant"; }
    public String ruleText() { return "Change the target of target spell with a single target"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return true; }

    @Override
    public String getBackground() {
        return "image/Deflection.jpg";
    }

    private class DeflectionEffect extends AbstractCardEffect implements TargetingEffect {
        TargetingEffect target;
        
        public DeflectionEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [Change the target of " + target.name() + " with a single target]";
        }
        
        public void pickTarget() {
            //CardGame.instance.getModel().sendToModel( owner.name() + ": choose target for " + name() );
            CardStack stack = CardGame.instance.getStack();
            ArrayList<TargetingEffect> effects=new ArrayList<>();
            int i=1;
            for (Effect e:stack) {
                if (e instanceof TargetingEffect)
                {
                    TargetingEffect te=(TargetingEffect)e;
                    effects.add(te);
                    //CardGame.instance.getModel().sendToModel( i + ") " + te.name());
                    ++i;
                }
            }


            TargetRequest request = new TargetRequest(effects,1);
            Requests.instance.addRequest(request);

            try {
                int idx = Requests.instance.getResult().get(0);
                if (idx < 0 || idx >= effects.size()) target = null;
                else target = effects.get(idx);
            }catch (InterruptedException e){
                target = null;
            }
        }
        
        public void resolve() {
            if (target==null) {
                //CardGame.instance.getModel().sendToModel(cardName + " has no target");
            } else if (CardGame.instance.getStack().indexOf(target)==-1) {
                //CardGame.instance.getModel().sendToModel(cardName + " target is not on the stack anymore");
            } else {
                target.pickTarget();
            }
        }
    }

    public Effect getEffect(Player owner) { 
        return new DeflectionEffect(owner, this); 
    }
}


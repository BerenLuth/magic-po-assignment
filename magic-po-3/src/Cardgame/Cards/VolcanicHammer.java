package Cardgame.Cards;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Controller.Observers.MsgCenter;
import Cardgame.Core.*;

import java.awt.*;
import java.util.ArrayList;

public class VolcanicHammer extends AbstractCard {
    static private final String cardName = "Volcanic Hammer";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new VolcanicHammer(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Sorcery"; }
    public String ruleText() { return "Deal 3 damage to target creature or player"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return false; }

    @Override
    public String getBackground() {
        return "image/Volcanic_Hammer.jpg";
    }

    private class VolcanicHammerEffect extends AbstractCardEffect implements TargetingEffect {
        Damageable target;
        
        public VolcanicHammerEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [Deal 3 damage to " + target.name() + "]";
        }
        
        public void pickTarget() {
            //CardGame.instance.getModel().sendToModel( owner.name() + ": choose target for " + name() );
            
            ArrayList<Damageable> targets = new ArrayList<>();
            int i=1;
            
            Player curPlayer = CardGame.instance.getPlayer(0);
            //CardGame.instance.getModel().sendToModel(i+ ") " + curPlayer.name());
            targets.add(curPlayer);
            ++i;
            for (DecoratedCreature c:curPlayer.getCreatures()) {
               // CardGame.instance.getModel().sendToModel(i+ ") " + curPlayer.name() + ": " + c.name());
                targets.add(c);
                ++i;
            }
            
            curPlayer = CardGame.instance.getPlayer(1);
           // CardGame.instance.getModel().sendToModel(i+ ") " + curPlayer.name());
            targets.add(curPlayer);
            ++i;
            for (DecoratedCreature c:curPlayer.getCreatures()) {
                //CardGame.instance.getModel().sendToModel(i+ ") " + curPlayer.name() + ": " + c.name());
                targets.add(c);
                ++i;
            }

            TargetRequest request = new TargetRequest(targets,1);
            Requests.instance.addRequest(request);

            try {
                int idx = Requests.instance.getResult().get(0);
                if (idx < 0 || idx >= targets.size()) target = null;
                else target = targets.get(idx);
            }catch (InterruptedException e){
                target = null;
            }
        }
        
        public void resolve() {
            if (target==null) {
                //CardGame.instance.getModel().sendToModel(cardName + " has no target");
                MsgCenter.msgCenter.setState(cardName + " has no target");

            } else if (target.isRemoved() ) {
                //CardGame.instance.getModel().sendToModel(cardName + " target not in play anymore");
                MsgCenter.msgCenter.setState(cardName + " target not in play anymore");

            } else {
                target.inflictDamage(3);
            }
        }
    }

    public Effect getEffect(Player owner) { 
        return new VolcanicHammerEffect(owner, this); 
    }
}

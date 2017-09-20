package Cardgame.Cards;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Core.*;

import java.util.ArrayList;



public class AggressiveUrge extends AbstractCard {
    
    static private final String cardName = "Aggressive Urge";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new AggressiveUrge(); }
                } );
    
    private class AggressiveUrgeEffect extends AbstractCardEffect implements TargetingEffect {
        private DecoratedCreature target;
        public AggressiveUrgeEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        public void resolve() {
            if (target==null) return;
            
            if (target.isRemoved()) {
                //CardGame.instance.getModel().sendToModel("Attaching " + cardName + " to removed creature");
                return;
            }
            
            final AggressiveUrgeDecorator decorator=new AggressiveUrgeDecorator();
            TriggerAction action = new TriggerAction() {
                    public void execute() {
                        if (!target.isRemoved()){
                            //CardGame.instance.getModel().sendToModel("Triggered removal of " + cardName + " from " + target);
                            target.removeDecorator(decorator);
                        } else {
                            //CardGame.instance.getModel().sendToModel("Triggered dangling removal of " + cardName + " from removed target. Odd should have been invalidated!" );
                        }
                    }
                };
            //CardGame.instance.getModel().sendToModel("Ataching " + cardName + " to " + target.name() + " and registering end of turn trigger");
            CardGame.instance.getTriggers().register(Triggers.END_FILTER, action);
                        
            decorator.setRemoveAction(action);
            target.addDecorator(decorator);
        }
        
        public void pickTarget() {
            //CardGame.instance.getModel().sendToModel( owner.name() + ": choose target for " + cardName );

            ArrayList<DecoratedCreature> creatures = new ArrayList<>();
            int i = 1;

            Player player1 = CardGame.instance.getPlayer(0);
            Player player2 = CardGame.instance.getPlayer(1);

            for (DecoratedCreature c : player1.getCreatures()) {
                //CardGame.instance.getModel().sendToModel( i + ") " + player1.name() + ": " + c);
                ++i;
                creatures.add(c);
            }
            for (DecoratedCreature c : player2.getCreatures()) {
                //CardGame.instance.getModel().sendToModel( i + ") " + player2.name() + ": " + c);
                ++i;
                creatures.add(c);
            }

            TargetRequest request = new TargetRequest(creatures, 1);
            Requests.instance.addRequest(request);

            try {
                int idx = Requests.instance.getResult().get(0);
                if (idx < 0 || idx >= creatures.size()) target = null;
                else target = creatures.get(idx);
            } catch (InterruptedException azz) {
                target = null;
            }
        }


        public String toString() {
            if (target==null ) return super.toString() + " (no target)";
            else if (target.isRemoved()) return super.toString() + " (removed creature)";
            else return cardName + " [" + target.name() + " gets +1/+1 until end of turn]";
        }
                
    }

    public Effect getEffect(Player owner) { 
        return new AggressiveUrgeEffect(owner, this); 
    }
    
    class AggressiveUrgeDecorator extends AbstractCreatureDecorator {
        TriggerAction action;
        public void setRemoveAction(TriggerAction a) { action=a; }
        public void onRemove() {
            //CardGame.instance.getModel().sendToModel("Removing " + cardName + " and deregistering end of turn trigger");
            if (action!=null) 
                CardGame.instance.getTriggers().remove(action); 
            super.onRemove();
        }
        
        public int power() { return decorated.power()+1; }
        public int toughness() { return decorated.toughness()+1; }
    }
    
    public String name() { return cardName; }
    public String type() { return "Instant"; }
    public String ruleText() { return "Target creature gets +1/+1 until end of turn"; }
    public String toString() { return cardName + "[" + ruleText() +"]";}
    public boolean isInstant() { return true; }

    @Override
    public String getBackground() {
        return "image/Aggressive_Urge.jpg";
    }

}

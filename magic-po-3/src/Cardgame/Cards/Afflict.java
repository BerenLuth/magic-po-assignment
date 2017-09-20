package Cardgame.Cards;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Core.*;

import java.util.ArrayList;


//BOH BOH
public class Afflict extends AbstractCard {
    
    static private final String cardName = "Afflict";

    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new Afflict(); }
                } );

    private class AfflictEffect extends AbstractCardEffect implements TargetingEffect {
        private DecoratedCreature target;
        public AfflictEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        public void resolve() {
            if (target==null) return;
            
            if (target.isRemoved()) {
                notifyObservers();
                //CardGame.instance.getModel().sendToModel("Attaching " + cardName + " to removed creature");
                return;
            }
            
            final AfflictDecorator decorator=new AfflictDecorator();
            TriggerAction action = new TriggerAction() {
                    public void execute() {
                        if (!target.isRemoved()) {
                            //CardGame.instance.getModel().sendToModel("Triggered removal of " + cardName + " from " + target);
                            target.removeDecorator(decorator);
                            notifyObservers("Eseguito effetto Afflict");
                        } else {
                            notifyObservers("Afflict: creatura target rimossa");
                            //CardGame.instance.getModel().sendToModel("Triggered dangling removal of " + cardName + " from removed target. Odd should have been invalidated!");
                        }
                    }
                };
            //CardGame.instance.getModel().sendToModel("Ataching " + cardName + " to " + target.name() + " and registering end of turn trigger");
            CardGame.instance.getTriggers().register(Triggers.END_FILTER, action);

            decorator.setRemoveAction(action);
            target.addDecorator(decorator);
            notifyObservers();
        }
        
        public void pickTarget() {

            notifyObservers("Selezionare target di Afflict");
            ArrayList<DecoratedCreature> creatures=new ArrayList<>();
            int i=1;

            Player player1 = CardGame.instance.getPlayer(0);
            Player player2 = CardGame.instance.getPlayer(1);
            
            for (DecoratedCreature c: player1.getCreatures()) {
                //CardGame.instance.getModel().sendToModel(i + ") " + player1.name() + ": " + c);
                ++i;
                creatures.add(c);
            }
            for (DecoratedCreature c: player2.getCreatures()) {
                //CardGame.instance.getModel().sendToModel(i + ") " + player2.name() + ": " + c);
                ++i;
                creatures.add(c);
            }

            notifyObservers("Invio request per target Afflict");
            TargetRequest request = new TargetRequest(creatures,1);
            Requests.instance.addRequest(request);
            notifyObservers("Ricevuta risposta request di Afflict");

            try {
                int idx= Requests.instance.getResult().get(0);
                if (idx<0 || idx>=creatures.size()) {
                    target = null;
                    notifyObservers("Target null scelto");
                }
                else{
                    target=creatures.get(idx);
                    notifyObservers(target.name() + " scelto come target");
                }
            }catch (InterruptedException azz){
                target=null;
            }

        }
        
        public String toString() {
            if (target==null ) return super.toString() + " (no target)";
            else if (target.isRemoved()) return super.toString() + " (removed creature)";
            else return name() + " [" + target.name() + " gets -1/-1 until end of turn]";
        }
                
    }

    public Effect getEffect(Player owner) { 
        return new AfflictEffect(owner, this); 
    }
    
    class AfflictDecorator extends AbstractCreatureDecorator {
        TriggerAction action;
        public void setRemoveAction(TriggerAction a) { action=a; }
        public void onRemove() {
            //CardGame.instance.getModel().sendToModel("Removing " + cardName + " and deregistering end of turn trigger");
            notifyObservers("Rimosso Afflict e suoi trigger");
            if (action!=null) 
                CardGame.instance.getTriggers().remove(action); 
            super.onRemove();
        }
        
        public int power() { return decorated.power()-1; }
        public int toughness() { return decorated.toughness()-1; }
    }
    
    public String name() { return cardName; }
    public String type() { return "Instant"; }
    public String ruleText() { return "Target creature gets -1/-1 until end of turn"; }
    public String toString() { return name() + "[" + ruleText() +"]";}
    public boolean isInstant() { return true; }

    @Override
    public String getBackground() {
        return "image/Afflict.jpg";
    }

}

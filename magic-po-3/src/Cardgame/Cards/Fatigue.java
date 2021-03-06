package Cardgame.Cards;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Core.*;

import java.util.ArrayList;


public class Fatigue extends AbstractCard {
    static private final String cardName = "Fatigue";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new Fatigue(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Sorcery"; }
    public String ruleText() { return "Target player skips his next drawDeck phase"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return false; }

    @Override
    public String getBackground() {
        return "image/Fatigue.jpg";
    }

    private class FatigueEffect extends AbstractCardEffect implements TargetingEffect {
        Player target;
        
        public FatigueEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [" + target.name() + " skips his next drawDeck phase]";
        }
        
        public void pickTarget() {

            ArrayList<Player> players = new ArrayList<>();
            players.add(CardGame.instance.getPlayer(0));
            players.add(CardGame.instance.getPlayer(1));

            TargetRequest request = new TargetRequest(players,1);
            Requests.instance.addRequest(request);

            try {
                int idx = Requests.instance.getResult().get(0);
                if (idx < 0 || idx > 1) target = null;
                else target = CardGame.instance.getPlayer(idx);
            }catch (InterruptedException e){
                target = null;
            }
        }
        
        public void resolve() {
            if (target!=null)
                target.setPhase(Phases.DRAW, new SkipPhase(Phases.DRAW));
        }
    }

    public Effect getEffect(Player owner) { 
        return new FatigueEffect(owner, this); 
    }
}

package cardgame.cards;

import cardgame.*;



public class Fatigue extends AbstractCard {
    static private final String cardName = "Fatigue";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() { 
                public Card create() { return new Fatigue(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Sorcery"; }
    public String ruleText() { return "Target player skips his next draw phase"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
    
    private class FatigueEffect extends AbstractCardEffect implements TargetingEffect {
        Player target;
        
        public FatigueEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [" + target.name() + " skips his next draw phase]";
        }
        
        public void pickTarget() {
            System.out.println( owner.name() + ": choose target for " + name() );
            System.out.println("1) " + CardGame.instance.getPlayer(0).name());
            System.out.println("2) " + CardGame.instance.getPlayer(1).name());
            
            int idx = CardGame.instance.getScanner().nextInt()-1;
            if (idx<0 || idx>1) target=null;
            else target=CardGame.instance.getPlayer(idx);
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

package cardgame.cards;

import cardgame.*;



public class FalsePeace extends AbstractCard {
    static private final String cardName = "False Peace";
    
    static private StaticInitializer initializer = 
            new StaticInitializer(cardName, new CardConstructor() { 
                public Card create() { return new FalsePeace(); }
                } );
    
    public String name() { return cardName; }
    public String type() { return "Sorcery"; }
    public String ruleText() { return "Target player skips his next combat phase"; }
    public String toString() { return name() + " [" + ruleText() +"]";}
    public boolean isInstant() { return false; }
    
    private class FalsePeaceEffect extends AbstractCardEffect implements TargetingEffect {
        Player target;
        
        public FalsePeaceEffect(Player p, Card c) { super(p,c); }
        public boolean play() {
            pickTarget();
            return super.play();
        }
        
        public String toString() {
            if (target==null) return super.toString() + " (no target)";
            else return name() + " [" + target.name() + " skips his next combat phase]";
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
                target.setPhase(Phases.COMBAT, new SkipPhase(Phases.COMBAT));
        }
    }

    public Effect getEffect(Player owner) { 
        return new FalsePeaceEffect(owner, this); 
    }
}

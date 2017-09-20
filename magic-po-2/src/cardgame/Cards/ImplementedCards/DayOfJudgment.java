package cardgame.Cards.ImplementedCards;

import cardgame.Cards.AbstractCardEffect;
import cardgame.Cards.Card;
import cardgame.Cards.Effect;
import cardgame.Game.Player;

/**
 * Created by Fabio on 22/03/2016.
 */
public class DayOfJudgment implements Card{

    private class HomeopathyEffect extends AbstractCardEffect {
        public HomeopathyEffect(Player p, Card c) { super(p,c); }
        public void resolve() {
            //Ripulisco l'array creature di owner
            owner.get_creatures().clear();

            //Ripulisco l'array creature di opponent
            opponent.get_creatures().clear();
        }
    }

    public Effect get_effect(Player owner) {
        return new HomeopathyEffect(owner, this);
    }

    public String name() { return "Day of Judgment"; }
    public String type() { return "Sorcery"; }
    public String rule_text() { return name() + "Destroy all creatures"; }
    public String toString() { return name() + " [" + rule_text() +"]";}
    public boolean isInstant() { return false; }
}

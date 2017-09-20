package cardgame.Cards;

import cardgame.Game.Player;

import java.util.List;

public interface Creature {
    String name();
    boolean tap();
    boolean untap();
    boolean isTapped();
    boolean attack();
    boolean defend();
    void inflict_damage(int dmg);
    void reset_damage();
    int get_power();
    int get_toughness();
    void setAttackTrigger(int i);
    void setDamageLeftTrigger(int i);
    int getAttack();
    int getDamageLeft();
    Player getOwner();

    // returns all the effects associated to this creature
    List<Effect> effects();
    
    // returns only the effects that can be played currently
    // depending on state, e.g., tapped/untapped
    List<Effect> avaliable_effects();
}

package Magic.Cards;


import Magic.Personal.Player;

// Verrà implementata da Creature, Stregonerie e Istantanee.
public interface Spell {

    void effect();
    void setOwner(Player owner);
}

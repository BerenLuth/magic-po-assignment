package Magic.Cards;

import Magic.GameException.EndException;
import Magic.Personal.Player;


public interface Permanent {
    Boolean isTapped();
    void tap();
    void untap();
    void joinBattlefield();

    int getCostituzione();
    void setCostituzione(int costituzione);

    int getAttacco();
    void setAttacco(int attacco);

    void setDamage(int damage);
    int getDamage();

    void dealDamage(Creature c);
    void dealDamage(Player g) throws EndException;
    void solveDamage();
    
    void die();
}

package Cardgame.Core;


public interface Targetable {

    boolean isPossibleTarget();
    int getTargetIndex();

    void setTargetIndex(int i);
    void setPossibleTarget();

    void endTargeting();
}


package Cardgame.Core;


public interface GameEntity extends ObservableInterface, Targetable{
    
    String name();    
    boolean isRemoved();
    
    void accept(GameEntityVisitor v);
}

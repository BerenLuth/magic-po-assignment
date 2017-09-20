package Cardgame.Core;



public interface DamageStrategyDecorator extends DamageStrategy {
    DamageStrategyDecorator decorate(DamageStrategy c);  
    DamageStrategy removeDecorator(DamageStrategyDecorator d);
}

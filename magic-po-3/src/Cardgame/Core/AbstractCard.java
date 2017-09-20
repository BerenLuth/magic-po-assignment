/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cardgame.Core;

import Cardgame.Controller.Observers.CardObserver;


public abstract class AbstractCard extends AbstractGameEntity implements Card {
    public AbstractCard(){
        addObserver(CardObserver.observer);
    }
    public void accept(GameEntityVisitor v) { v.visit(this); }
}

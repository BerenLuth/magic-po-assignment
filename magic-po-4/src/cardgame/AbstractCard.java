/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgame;

/**
 *
 * @author andrea
 */
public abstract class AbstractCard extends AbstractGameEntity implements Card {
    public void accept(GameEntityVisitor v) { v.visit(this); }
}

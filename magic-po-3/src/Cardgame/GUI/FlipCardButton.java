package Cardgame.GUI;

import Cardgame.Core.Card;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 * Created by Fabio on 05/05/2016.
 */
public class FlipCardButton extends CardButton {

    private Card card;

    public FlipCardButton(Card c){
        super();
        card = c;
    }

    @Override
    public void paintComponent(Graphics g) {
        Image bg = pathToImage(retro);    //Disegno il retro della carta
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public FlipCardButton originalCard() {
        if(isUnseletable())
            return new FlipCardButton(card);
        else return this;
    }

    @Override
    public boolean isPossibleTarget(){
        return card.isPossibleTarget();
    }

    @Override
    public FlipCardButton unselectable() {
        return new FlipCardButton(card) {

            public FlipCardButton set() {
                this.setPreferredSize(new Dimension(new Double(getWIDTH()/1.05).intValue(), new Double(getHEIGHT()/1.05).intValue()));
                this.setBorderPainted(false);
                this.setFocusPainted(false);
                this.setUnseletable(true);
                return this;
            }

            @Override
            public void paintComponent(Graphics g) {
                BufferedImage bg = pathToImage(retro);

                ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
                op.filter(bg, bg);

                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }

        }.set();    //Richiamando il metodo set possiamo impostare i parametri
    }

    @Override
    public CardButton flip(){
        if(isUnseletable())
            return new CardButton(card).unselectable();
        else
            return new CardButton(card);
    }

}

package Cardgame.GUI;

import Cardgame.Core.Card;
import Cardgame.Core.Creature;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;


public class CardButton extends JButton {

    private static int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/12;
    private static int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/5;

    public static final String retro = "image/card_background.jpg";

    private boolean unseletable = false;

    private Card card;

    public CardButton(Card card) {
        super.setText(card.name());
        if (card instanceof Creature){
        super.setToolTipText(card.name() + ": " + card.ruleText()+"Atk:"+((Creature) card).power()+"Def:"+
                ((Creature) card).toughness()+"Damage:"+ ((Creature) card).getDamage());
        }
        else {
            super.setToolTipText(card.name() + ": " + card.ruleText());
        }
        super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.card = card;
    }

    public CardButton(){
        super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public boolean isUnseletable() {
        return unseletable;
    }

    public void setUnseletable(boolean unseletable) {
        this.unseletable = unseletable;
    }

    public static void setButtonDimension(Dimension screenSize){
        HEIGHT = (int) screenSize.getHeight()/5;
        WIDTH = (int) screenSize.getWidth()/12;
    }

    public Card getCard(){
        return card;
    }

    public static int getHEIGHT(){
        return HEIGHT;
    }
    public static int getWIDTH(){
        return WIDTH;
    }

    @Override
    public void paintComponent(Graphics g) {
        Image bg = pathToImage(card.getBackground());
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
    }

    /**
     * Dato un percorso ritorna l'immagine.
     *
     * @param imagePath: percorso dell'immagine.
     * @return l'immagine se esiste, null altrimenti.
     */
    public BufferedImage pathToImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            return null;
        }
    }


    public boolean isPossibleTarget(){
        return card.isPossibleTarget();
    }

    public Integer getButtonTargetIndex(){
        try{
            return card.getTargetIndex();
        }catch (NullPointerException npe){
            System.out.println("Il bottone non è associato ad una carta");
            return null;
        }

    }

    /**
     * In qualsiasi momento potremmo aver bisogno di ritornare la carta originale e non sapere se è girata o oscurata
     * questo metodo si occupa di fare il controllo e di ritornare sempre la carta originale
     *
     * @return
     */
    public CardButton originalCard() {
        if(isUnseletable())
            return new CardButton(card);
        else return this;
    }

    /**
     * Ritorna una nuova istanza di CardButton contentente la stessa carta, con dimensioni ridotte
     * e in bianco e nero, in questo modo l'utente noterà che non è possibile selezionare questa carta
     *
     * @return nuovo CardButton che a sua volta può ritornare l'originale.
     */
    public CardButton unselectable() {
        return new CardButton(card) {

            public CardButton set() {
                this.setPreferredSize(new Dimension(new Double(getWIDTH()/1.05).intValue(), new Double(getHEIGHT()/1.05).intValue()));
                this.setBorderPainted(false);
                this.setFocusPainted(false);
                this.setUnseletable(true);
                return this;
            }

            @Override
            public void paintComponent(Graphics g) {
                BufferedImage bg = pathToImage(card.getBackground());

                ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
                op.filter(bg, bg);

                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
            }

        }.set();    //Richiamando il metodo set possiamo impostare i parametri
    }

    public CardButton selectable(){
        return this;
    }

    public CardButton flip(){
        if(isUnseletable())
            return new FlipCardButton(getCard()).unselectable();
        else
            return new FlipCardButton(getCard());
    }

}

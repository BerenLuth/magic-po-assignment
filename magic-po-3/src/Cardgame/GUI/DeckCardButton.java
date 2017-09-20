package Cardgame.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import Cardgame.Core.Card;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Fonto on 05/05/16.
 */
public class DeckCardButton extends JButton {

    private static int WIDTH = 155;
    private static int HEIGHT = 250;
    private Card card;

    public DeckCardButton(Card card){
        this.card=card;
        super.setToolTipText(card.name() + ": " + card.ruleText());
    }

    @Override
    public void paintComponent(Graphics g) {
        Image bg = pathToImage(card.getBackground());
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
    }

    public BufferedImage pathToImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            return null;
        }
    }

    public Card getCard(){
        return card;
    }
 }

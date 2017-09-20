package Cardgame.GUI;

import Cardgame.Core.Card;
import Cardgame.Core.Creature;

import javax.accessibility.Accessible;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Fabio on 07/05/2016.
 */
public class CreatureButton extends JComponent implements Accessible{

    private JPanel buttonLayout;
    private JLabel attack;
    private JLabel defense;
    private JPanel valuePanel;
    private JButton name;

    private static int WIDTH = 155;
    private static int HEIGHT = 250;

    Creature creature;

    public CreatureButton(Creature creature){
        this.creature = creature;
        name.setText(creature.name());
        attack.setText("" + creature.power());
        defense.setText("" + creature.toughness());
        super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public CreatureButton(String name, String power, String def){
        buttonLayout.setBackground(new Color(200));
        this.name.setText(name);
        this.attack.setText(power);
        this.defense.setText(def);
        super.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public static void setButtonDimension(Dimension screenSize){
        HEIGHT = (int) screenSize.getHeight()/5;
        WIDTH = (int) screenSize.getWidth()/12;
    }

}

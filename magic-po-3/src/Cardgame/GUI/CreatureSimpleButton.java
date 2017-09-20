package Cardgame.GUI;

import Cardgame.Core.DecoratedCreature;
import Cardgame.Core.Effect;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Fabio on 08/05/2016.
 */
public class CreatureSimpleButton extends JButton {


    private static int WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/15;
    private static int HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/8;
    public static Color normal = new Color(200, 162, 200);
    public static Color notUsable = new Color(200, 200, 200);

    private DecoratedCreature creature;
    private String creatureText;
    private String creatureEffects = "";

    private boolean unselectable = false;

    public CreatureSimpleButton(DecoratedCreature creature){
        this.creature = creature;
        creatureText = creature.name() + ": " + creature.power() + "/" + creature.toughness();

        for(Effect e: creature.effects()){
            creatureEffects = creatureEffects + ", " + e.toString();
        }
        setToolTipText(creatureEffects);

        this.setText(creatureText);
        //this.setOpaque(true);
        //this.setBackground(Color.CYAN);
        //this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(normal);
    }

    public DecoratedCreature getCreature(){
        return creature;
    }

    public String getCreatureEffects(){
        return creatureEffects;
    }

    public boolean isUnselectable() {
        return unselectable;
    }

    public void setUnselectable(boolean unselectable) {
        this.unselectable = unselectable;
    }

    public CreatureSimpleButton unselectable(){
        this.setBackground(notUsable);
        setBorderPainted(false);
        setFocusPainted(false);
        setUnselectable(true);
        return this;
    }

    public CreatureSimpleButton selectable(){
        this.setBackground(normal);
        setBorderPainted(true);
        setFocusPainted(true);
        setUnselectable(false);
        return this;
    }

    public CreatureSimpleButton effectCreature(){
        this.setBackground(normal);
        this.setBorderPainted(true);
        this.setFocusPainted(true);
        this.setUnselectable(false);
        this.setText("Effect of: " + creature.name());
        this.setToolTipText(creature.effects().get(0).toString());
        return this;
    }

    public Integer getEffectTargetIndex(){
        try{
            return creature.effects().get(0).getTargetIndex();
        }catch (NullPointerException npe){
            System.out.println("Problemi con l'effetto della carta");
            return null;
        }
    }

    public Integer getButtonTargetIndex(){
        try{
            return creature.getTargetIndex();
        }catch (NullPointerException npe){
            System.out.println("Il bottone non è associato ad una creatura");
            return null;
        }

    }



    //ToDo: solo per test
    public CreatureSimpleButton(){
        creatureText = "Giangigi: 1/2";
        this.setText(creatureText);
        this.setBackground(normal);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //this.setBackground(new Color(0, 0, 0, 100));
    }

}

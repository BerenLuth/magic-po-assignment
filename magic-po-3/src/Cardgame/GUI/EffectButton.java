package Cardgame.GUI;

import Cardgame.Core.Effect;

import javax.swing.*;

/**
 * Created by Fonto on 08/05/16.
 */
public class EffectButton extends JButton {
    Effect effect;

    public EffectButton(Effect effect){
        this.effect=effect;
        super.setText(effect.name());
        setBackground(CreatureSimpleButton.normal);
    }

    public Integer getButtonTargetIndex(){
        try{
            return effect.getTargetIndex();
        }catch (NullPointerException npe){
            System.out.println("Il bottone non è associato ad una carta");
            return null;
        }

    }
}

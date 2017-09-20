package Cardgame.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import Cardgame.Cards.*;

/**
 * Created by Fonto on 05/05/16.
 */
public class DeckGUI implements Runnable{

    public static final DeckGUI instance = new DeckGUI();

    private DeckGUI(){}

    private JButton saveDeck;
    private JFrame frame;
    private JPanel main_panel;
    private JTextField fieldDeckName;
    private JPanel cardselector;
    private JPanel cardSelector;
    private JPanel nameselctor;
    private JPanel saver;
    private JPanel selector;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton button10;
    private JButton button11;
    private JButton button12;
    private JButton button13;
    private JButton button14;
    private JButton button15;
    private JButton button16;
    private JButton button17;
    private JPanel savedDeck;
    private JLabel count;
    private JPanel Count;
    private JLabel info;

    private ArrayList<String> deck = new ArrayList<>();
    private int number_of_cards = 0;
    @Override
    public void run() {

        System.out.println("selezione deck in GUI");
        frame = new JFrame();
        frame.setContentPane(main_panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


        savedDeck.setLayout(new GridLayout(-1, 1));

        setSelectionListener( button1);
        setSelectionListener( button2);
        setSelectionListener( button3);
        setSelectionListener( button4);
        setSelectionListener( button5);
        setSelectionListener( button6);
        setSelectionListener( button7);
        setSelectionListener( button8);
        setSelectionListener( button9);
        setSelectionListener( button10);
        setSelectionListener( button11);
        setSelectionListener( button12);
        setSelectionListener( button13);
        setSelectionListener( button14);
        setSelectionListener( button15);
        setSelectionListener( button16);
        setSelectionListener( button17);
        info.setToolTipText("Manca qualcosa!");

        saveDeck.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               createNewDeck();
            }

            @Override
            public void mouseEntered(MouseEvent e){
                if (!doneControl())
                info.setText("Manca qualcosa!");
                else
                info.setText("Ora puoi salvare!");
                savedDeck.updateUI();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                info.setText(" ");
            }
        });


        //// TODO: sistemare layout

    }


   private void createUIComponents() {
        button1 = new DeckCardButton(new Afflict());
        button2 = new DeckCardButton(new AggressiveUrge());
        button3 = new DeckCardButton(new BenevolentAncestor());
        button4 = new DeckCardButton(new BoilingEarth());
        button5 = new DeckCardButton(new BronzeSable());
        button6 = new DeckCardButton(new Cancel());
        button7 = new DeckCardButton(new Darkness());
        button8 = new DeckCardButton(new DayOfJudgment());
        button9 = new DeckCardButton(new Deflection());
        button10 = new DeckCardButton(new FalsePeace());
        button11 = new DeckCardButton(new Fatigue());
        button12 = new DeckCardButton(new Homeopathy());
        button13 = new DeckCardButton(new NorwoodRanger());
        button14 = new DeckCardButton(new Reflexologist());
        button15 = new DeckCardButton(new SavorTheMoment());
        button16 = new DeckCardButton(new VolcanicHammer());
        button17 = new DeckCardButton(new WorldAtWar());
    }


    private void setSelectionListener(final JButton button){
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton newButton = new JButton();
                DeckCardButton support = ((DeckCardButton) button);

                if(number_of_cards<30) {
                    newButton.setText(support.getCard().name());
                    savedDeck.add(newButton);
                    deck.add(support.getCard().name());
                    setRemoveListener(newButton);
                    savedDeck.updateUI();
                    refreshCounter(true);
                }
            }
        });

    }

    private void setRemoveListener(final JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                savedDeck.remove(button);
                deck.remove(button.getText());
                savedDeck.updateUI();
                refreshCounter(false);
            }
        });
    }


    private void refreshCounter(boolean b){
        if (b)
            number_of_cards++;
        else
            number_of_cards--;

        count.setText("Carte:"+number_of_cards+"/30");
        count.updateUI();

    }

    //metodo che salva un deck di 30 carte in un file di testo.
    private void createNewDeck(){
        if (!doneControl())
            return;
        else {
            FileOutputStream deck = null;
            try {
                deck = new FileOutputStream(fieldDeckName.getText()+".deck");
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PrintStream stream = new PrintStream(deck);

            for ( String c : this.deck ) {
                stream.println(c);
            }

            frame.setVisible(false);
        }
    }

    private boolean doneControl() {
        if (number_of_cards == 30 && fieldDeckName.getText().length()!=0){
            return true;
        }
        else return false;
    }
}

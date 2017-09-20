package Cardgame.GUI;

import Cardgame.Core.CardGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by Fabio on 14/04/2016.
 */
public class MainGUI implements Runnable{

    private JFrame frame;
    private JPanel mainLayout;
    private JTextField nameOne;
    private JTextField nameTwo;
    private JTextField deckOne;
    private JTextField deckTwo;
    private JButton done;
    private JButton creaDeckButton;
    private JLabel messageLabel;

    private Image backgroundImage;

    public MainGUI(){
        try {
            backgroundImage  = ImageIO.read(new File("image/background.jpg"));
            //backgroundImage = imageTemp.getScaledInstance(JFrame.MAXIMIZED_HORIZ, JFrame.MAXIMIZED_VERT, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            backgroundImage = null;
        }

        nameOne.setText("primo");
        nameTwo.setText("secondo");
        deckOne.setText("deck1.deck");
        deckTwo.setText("deck2.deck");
    }

    public void run() {

        frame = new JFrame();
        frame.setContentPane(mainLayout);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(300,400));



        done.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!doneControl()) {
                    messageLabel.setText("Manca qualcosa!");
                }
                else {
                    messageLabel.setText(" ");
                }
            }

            @Override
            public void mouseExited(MouseEvent e){
                messageLabel.setText(" ");
            }


            @Override
            public void mouseClicked(MouseEvent e) {
                if(doneControl()) {
                    startGame();
                }
                else
                    messageLabel.setText("Manca qualcosa!");
            }

        });

        creaDeckButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(DeckGUI.instance).start();
            }
        });

        //Setta il listener per ogni Field
        setFieldEnterEvent(nameOne);
        setFieldEnterEvent(nameTwo);
        setFieldEnterEvent(deckOne);
        setFieldEnterEvent(deckTwo);
    }


    private boolean doneControl(){
        if(nameOne.getText().length() == 0
                || nameTwo.getText().length() == 0
                || deckOne.getText().length() == 0
                || deckTwo.getText().length() == 0)
            return false;
        else
            return true;
    }

    private void startGame(){
        frame.setVisible(false);

        CardGame.instance.setup(nameOne.getText(), deckOne.getText(), nameTwo.getText(), deckTwo.getText());

        Thread gui = new Thread(GameGUI.instance);
        gui.setName("GameGUI");
        gui.start();
        /*Thread t = new Thread(CardGame.instance);
        t.start();*/
    }

    private void createUIComponents() {
        mainLayout = new JPanel(new BorderLayout()) {
            @Override public void paintComponent(Graphics g) {
                g.drawImage(backgroundImage, 0, 0,getWidth(), getHeight(), null);
            }
        };
    }

    //Imposta il listener su ogni campo di testo e alla pressione di invio (\n) fa il controllo e fa partire il gioco
    private void setFieldEnterEvent(JTextField field){
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == '\n')
                    if(doneControl()) {
                        startGame();
                    }
                    else
                        messageLabel.setText("Manca qualcosa!");
            }
        });
    }

}

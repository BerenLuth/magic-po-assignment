package Cardgame.GUI;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Controller.Observers.*;
import Cardgame.Core.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Fabio on 05/05/2016.
 */
public class GameGUI implements Runnable {

    private Dimension screenSize;

    public static final GameGUI instance = new GameGUI();

    public final int REQUEST_END_PHASE = 1;
    public final int REQUEST_NO_TARGET = 2;

    public static final int P1 = 1;
    public static final int P2 = 2;

    private JFrame frame;

    private JPanel main_panel;
    private JPanel panel_stack_main;
    private JPanel panel_stack;
    private JPanel panel_field_main;
    private JPanel panel_info_main;
    private JPanel panel_hand_2;
    private JPanel panel_hand_1;
    private JPanel panel_field_1;
    private JPanel panel_field_2;
    private JScrollPane scroll_pane_2;
    private JScrollPane scroll_pane_1;
    private JPanel panel_info_2;
    private JPanel panel_info_phase;
    private JPanel panel_info_1;
    private JButton button_player_1;
    private JLabel label_life_1;
    private JButton button_player_2;
    private JLabel label_life_2;
    private JTextArea text_phase_info;
    private JButton button_end_phase;
    private JTextArea text_message_info;
    private JPanel deck_player_1;
    private JPanel deck_player_2;
    private JScrollPane scrollPaneCheNonDovrebbeAllargarsi;

    private String turnText;
    private String phaseText;
    private ArrayList<String> gameMessages = new ArrayList<>();

    /**
     * Indica quale giocatore ha attualmente il controllo del gioco
     * playerShown=P1 oppure playerShown=P2
     */
    private int playerShown;
    private Player current_player;

    ArrayList<Card> hand1 = new ArrayList<>();
    ArrayList<Card> hand2 = new ArrayList<>();
    boolean hand1Flipped = false;
    boolean hand2Flipped = false;

    ArrayList<Effect> stack = new ArrayList<>();
    ArrayList<DecoratedCreature> field1 = new ArrayList<>();
    ArrayList<DecoratedCreature> field2 = new ArrayList<>();


    private GameGUI() {
    }

    @Override
    public void run() {
        System.out.println("partita la GUI");
        System.out.println("Inizio esecuzione GUI");
        frame = new JFrame();
        frame.setContentPane(main_panel);
        frame.setSize(screenSize = Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setPanelTransparent();
        setPanelsSize();

        CardButton.setButtonDimension(screenSize);
        CreatureButton.setButtonDimension(screenSize);

        initializeFlipHand();


        deck_player_1.add(new FlipCardButton(null));
        deck_player_2.add(new FlipCardButton(null));

        panel_stack.setLayout(new GridLayout(-1, 1));


        button_player_1.setText(CardGame.instance.getPlayer(0).name());
        button_player_2.setText(CardGame.instance.getPlayer(1).name());

        text_message_info.setText("Messaggio di esempio da parte del gioco");
        /*while (true)
            text_message_info.setText(MsgCenter.msgCenter.getState());*/

        startMessageCenterThread();


        //Dopo aver inizializzato la gui facciamo partire il gioco
        startCardGameThread();

        setEndButton(REQUEST_END_PHASE);
    }


    //ma fare un tasto per passare no?
    private synchronized void setEndButton(int intero_che_non_uso) {
        button_end_phase.setText("passa");
        button_end_phase.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("non scelgo altri target");
                indexes.add(-1);
                if (request == null) {
                    System.out.println("perchè diventa nulla?");
                }
                try {
                    request.answerRequest(indexes);
                } catch (NullPointerException err) {
                    System.out.println("nullpointer :/");
                }
                indexes = new ArrayList<Integer>();
                super.mouseClicked(e);
            }
        });
    }

    public void drawCardInField() {
        panel_field_1.removeAll();
        for (DecoratedCreature dc : field1) {

            panel_field_1.add(new CreatureSimpleButton(dc));
        }
        panel_field_2.removeAll();
        for (DecoratedCreature dc : field2) {
            System.out.println(dc.name());
            panel_field_2.add(new CreatureSimpleButton(dc));
        }
        main_panel.updateUI();
    }


    public void drawStack() {
        panel_stack.removeAll();
        for (Effect e : stack) {
            panel_stack.add(new JLabel(e.name())); //Test, servirà anche un effect button forse..
        }
        main_panel.updateUI();
    }

    private void drawCardInHand(Card c, int player, boolean bool) {
        CardButton card;
        if (bool == false) {
            card = new CardButton(c);
            card.addMouseListener(new MouseAdapter() {
                String prevMessage;

                @Override
                public void mouseEntered(MouseEvent e) {
                    prevMessage = text_message_info.getText();
                    text_message_info.setText(c.ruleText());
                    text_message_info.updateUI();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    text_message_info.setText(prevMessage);
                    text_message_info.updateUI();
                }
            });
        } else {
            card = new FlipCardButton(c);
        }
        if (player == P1) {
            panel_hand_1.add(card);
            panel_hand_1.updateUI();
        }
        if (player == P2) {
            panel_hand_2.add(card);
            panel_hand_2.updateUI();
        }

        //passa i target nulli e -1 (numero di target), in questo modo al giro delle carte le ricolora giuste
        //showTargettable(null, -1);
    }

    public void drawCardInHand(int player) {
        boolean bool;
        ArrayList<Card> card;
        if (player == P1) {
            //Queste cose le dobbiamo fare perché ci passano ogni volta TUTTA la mano
            panel_hand_1.removeAll();
            bool = hand1Flipped;
            card = hand1;
        } else if (player == P2) {
            //Queste cose le dobbiamo fare perché ci passano ogni volta TUTTA la mano
            panel_hand_2.removeAll();
            bool = hand2Flipped;
            card = hand2;
        } else return;  //Errore nel passaggio del giocatore
        for (Card c : card) {
            drawCardInHand(c, player, bool);
        }
    }

    public void setHand1(List<Card> cards) {
        hand1.clear();
        for (Card c : cards
                ) {
            hand1.add(c);
        }
    }

    public void setHand2(List<Card> cards) {
        hand2.clear();
        for (Card c : cards
                ) {
            hand2.add(c);
        }
    }

    public void setField1(List<DecoratedCreature> creatures) {
        field1.clear();
        for (DecoratedCreature c : creatures) {
            field1.add(c);
        }
    }

    public void setField2(List<DecoratedCreature> creatures) {
        field2.clear();
        for (DecoratedCreature c : creatures) {
            field2.add(c);
        }
    }

    public void setStack(List<Effect> cards) {
        stack.clear();
        for (Effect e : cards
                ) {
            stack.add(e);
        }

    }

    /*
            metodi per gestire i target ----------------------------
         */
    public TargetRequest request;
    private List<Integer> indexes = new ArrayList<>();

    //gestisce la richiesta della gui
    public void sendRequest(TargetRequest request) {
        System.out.println("Send request rivecuta");
        indexes = new ArrayList<>();
        this.request = request;
        indexes.clear();

        if (request.getPossibleTargets().size() == 0) {
            indexes.add(-1);
            request.answerRequest(indexes);
        } else {
            this.request.initTargets();
            setEndButton(REQUEST_NO_TARGET);

            //ad ogni richiesta cambia il player corrente (quello che deve giocare la carta)
            current_player = Requests.instance.getAskingPlayer();

            //Ad ogni richiesta gira le carte in base al giocatore che dovrà scegliere
            flipHand(Requests.instance.getAskingPlayer());

            showTargettable(request.getPossibleTargets(), request.getTargetsNumber());

        }
    }

    /**
     * Metodo pubblico per settare i target, passando una lista di Targettable
     *
     * @param targets        lista degli elementi selezionabili
     * @param numberOfTarget numero di elementi da selezionare
     */
    public void showTargettable(List<Targetable> targets, int numberOfTarget) {
        //La mano dell'avversario non può essere selezionabile, quindi salvo su delle variabili Panel e array
        //  in questo modo li posso usare successivamente senza distinzione.

        CardObserver.observer.update();
        EffectObserver.observer.update();
        CreatureObserver.observer.update();
        PlayerObserver.playerObserver.update();

        setPhaseInfo();


        if (numberOfTarget > 0)
            deliverMessage("Puoi selezionare " + numberOfTarget + " target.");

        drawTargettable(hand1, panel_hand_1, hand1Flipped);
        drawTargettable(hand2, panel_hand_2, hand2Flipped);
        drawTargettableField();
        drawTargettableStack();
        drawTargettablePlayer();

        main_panel.updateUI();
    }

    private void drawTargettablePlayer() {
        Player player;
        if ((player = CardGame.instance.getPlayer(0)).isPossibleTarget()) {
            setPlayerButtonClickable(button_player_1, true);
            final Player finalPlayer = player;
            button_player_1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    indexes.add(finalPlayer.getTargetIndex());
                    if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                        request.answerRequest(indexes);
                        indexes = new ArrayList<Integer>();
                        request = null;
                    }
                }
            });
        } else
            setPlayerButtonClickable(button_player_1, false);

        if ((player = CardGame.instance.getPlayer(1)).isPossibleTarget()) {
            setPlayerButtonClickable(button_player_2, true);
            final Player finalPlayer1 = player;
            button_player_2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    indexes.add(finalPlayer1.getTargetIndex());
                    if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                        request.answerRequest(indexes);
                        indexes = new ArrayList<Integer>();
                        request = null;
                    }
                }
            });
        } else
            setPlayerButtonClickable(button_player_2, false);

    }

    private void setPlayerButtonClickable(JButton button, boolean clickable) {
        button.setBorderPainted(clickable);
        button.setFocusPainted(clickable);

        if (!clickable) {
            //Essendo un pulsante che rimane sempre e non viene cancellato, dobbiamo togliere il listener
            //  quando non è selezionabile
            button.setBackground(CreatureSimpleButton.notUsable);
            button.addMouseListener(null);
        }else
            button.setBackground(CreatureSimpleButton.normal);
    }


    private void drawTargettableField() {
        panel_field_1.removeAll();
        panel_field_2.removeAll();
        for (DecoratedCreature dc : field1) {
            if (dc.isPossibleTarget()) {
                CreatureSimpleButton creatureButton = new CreatureSimpleButton(dc).selectable();
                panel_field_1.add(creatureButton);
                creatureButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        indexes.add(creatureButton.getButtonTargetIndex());
                        if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                            request.answerRequest(indexes);
                            indexes = new ArrayList<Integer>();
                        }
                    }
                });
            }
            else
                panel_field_1.add(new CreatureSimpleButton(dc).unselectable());


            //Selta degli effetti delle creature da attaccare :(
            if (!dc.effects().isEmpty()) {
                if (dc.effects().get(0).isPossibleTarget()) {
                    CreatureSimpleButton creatureButton = new CreatureSimpleButton(dc).effectCreature();
                    panel_field_1.add(creatureButton);
                    creatureButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            indexes.add(creatureButton.getEffectTargetIndex());
                            if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                                request.answerRequest(indexes);
                                indexes = new ArrayList<Integer>();
                            }
                        }
                    });
                }
            }
        }
        for (DecoratedCreature dc : field2) {
            if (dc.isPossibleTarget()) {
                CreatureSimpleButton creatureButton = new CreatureSimpleButton(dc).selectable();
                panel_field_2.add(creatureButton);
                creatureButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        indexes.add(creatureButton.getButtonTargetIndex());
                        if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                            request.answerRequest(indexes);
                            indexes = new ArrayList<Integer>();
                        }
                    }
                });
            }else
                panel_field_2.add(new CreatureSimpleButton(dc).unselectable());


            //Selta degli effetti delle creature da attaccare :(
            if (!dc.effects().isEmpty()) {
                if (dc.effects().get(0).isPossibleTarget()) {
                    CreatureSimpleButton creatureButton = new CreatureSimpleButton(dc).effectCreature();
                    panel_field_2.add(creatureButton);
                    creatureButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            indexes.add(creatureButton.getEffectTargetIndex());
                            if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                                request.answerRequest(indexes);
                                indexes = new ArrayList<Integer>();
                            }
                        }
                    });
                }
            }
        }
    }

    private void drawTargettableStack() {
        panel_stack.removeAll();
        for (Effect c : stack) {
            if (c.isPossibleTarget()) {
                EffectButton button = new EffectButton(c);
                panel_stack.add(button);
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        indexes.add(button.getButtonTargetIndex());

                        if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                            request.answerRequest(indexes);
                            indexes = new ArrayList<Integer>();
                        }

                    }
                });
            } else {
                JButton button = new JButton(c.name());
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setBackground(CreatureSimpleButton.notUsable);
                panel_stack.add(button);
            }

        }
    }

    /**
     * ToDo: funziona solo con un arraylist di carte, ma stack è un array di Effect e i campi hanno Creature :(
     * <p>
     * Prende in input un ArrayList di bottoni e un Pannello, e ridisegna i singoli bottoni:
     * normali se sono selezionabili, grigi e più piccoli se non lo sono.
     *
     * @param cards arraylist di bottoni
     * @param panel Jpanel associato all'arraylist di bottoni
     */
    private void drawTargettable(ArrayList<Card> cards, JPanel panel, boolean flipped) {
        panel.removeAll();   //Tolgo tutti i bottoni per poi rimetterli bene
        for (Card c : cards) {    //Scorro tutto il contenuto del panel (contenuto salvato nell'array)
            if (c.isPossibleTarget()) {
                System.out.println("ehiehiehi");
                //Se è targettabile inserisco la carta in versione selectable (inutile visto che ritorna la stessa carta
                //  ma rende più comprensibile il codice, in quanto le carte seleionabili sono normali
                if (!flipped) {
                    CardButton cb = new CardButton(c);
                    panel.add(cb);
                    cb.selectable();
                    setTargettableListener(cb);
                } else {
                    CardButton cbflip = new FlipCardButton(c);
                    panel.add(cbflip);
                    cbflip.selectable();
                    setTargettableListener(cbflip);
                }

            } else {
                //Le carte non selezionabili sono piccole e in bianco e nero, quindi metto nel campo questa carta unselectable.
                if (!flipped) {
                    CardButton cb = new CardButton(c);
                    panel.add(cb.unselectable());
                } else {
                    CardButton cb = new FlipCardButton(c);
                    panel.add(cb.unselectable());
                }
            }
        }
    }

    /**
     * Prende in input un bottone e setta il listener che metterà il bottone selezionato in una lista di target scelti
     *
     * @param button bottone sul quale mettere il listener
     *               <p>
     *               ToDO: dovremo anche resettare tutti i listener alla fine della scelta del target.
     */
    private void setTargettableListener(CardButton button) {
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                //ToDo: mettere la carta nelle scelte
                System.out.println("cliccato il coso");
                indexes.add(button.getButtonTargetIndex());
                if (indexes.size() >= request.getTargetsNumber() && request.getTargetsNumber() >= 0) {
                    request.answerRequest(indexes);
                    indexes = new ArrayList<Integer>();
                }
                //flipHand();

            }
        });
    }

    //------------------------------------------------------------


    private void initializeFlipHand() {
        Player current = CardGame.instance.getCurrentPlayer();
        if (current == CardGame.instance.getPlayer(0)) {
            hand2Flipped = true;
            playerShown = P1;
            System.out.println("Girate carte del giocatore 2");
        } else {
            hand1Flipped = true;
            playerShown = P2;
            System.out.println("Girate carte del giocatore 1");
        }
    }

    public void flipHand(Player player) {
        if (player == CardGame.instance.getPlayer(0))
            flipHand(P1);
        else
            flipHand(P2);
    }

    /**
     * Metodo che richiama flipHand() e che quindi gira le carte, con la particolarità di avere un parametro
     * in ingresso che specifica quale giocatore si vuole mostrare
     *
     * @param playerToShow
     */
    public void flipHand(int playerToShow) {
        //Se il giocatore da mostrare è diverso da quello mostrato, gira le carte, altrimenti non fa nulla
        if (playerToShow != playerShown)
            flipHand();
    }

    /**
     * Passando il giocatore corrente, questo si occuperà di ridisegnare le sue carte scoperte
     * e di coprire le carte dell'avversario
     */
    public void flipHand() {

        //Quando le gira segna quale giocatore stiamo mostrando
        if (playerShown == P1)
            playerShown = P2;
        else
            playerShown = P1;
        hand1Flipped = !hand1Flipped;
        hand2Flipped = !hand2Flipped;


        drawCardInHand(P1);
        drawCardInHand(P2);

        main_panel.updateUI();
    }

    private ArrayList<CardButton> flipArrayCards(ArrayList<CardButton> cards, boolean flipped) {
        ArrayList<CardButton> buttons = new ArrayList<>();
        for (CardButton cb : cards) {
            if (flipped)
                buttons.add(new CardButton(cb.getCard()));
            else
                buttons.add(new FlipCardButton(cb.getCard()));
        }
        return buttons;
    }

    public void nextTurn() {
        flipHand(CardGame.instance.getCurrentPlayer());
        setPhaseInfo();
        //ToDo: aggiungere qui eventuali cose da fare al cambio del turno
    }

    public void nextPhase() {
        setPhaseInfo();
        //ToDo: aggiungere qui eventuali cose da fare al cambio della fase
    }

    private void setPhaseInfo() {
        button_end_phase.setText("Basta Targets");
        turnText = "Turno: " + CardGame.instance.getCurrentPlayer().name();
        phaseText = "Fase: " + CardGame.instance.getCurrentPlayer().currentPhase().toString();
        text_phase_info.setText(turnText + "\n\n" + phaseText);
    }

    public void deliverMessage(String s) {

        //Splitta la stringa ogni 30 caratteri (circa) senza spezzare le parole a metà
        String tokenized = "";
        int c = 30;
        StringTokenizer st = new StringTokenizer(s);
            while(st.hasMoreTokens()){
                if(tokenized.length() > c) {
                    tokenized = tokenized + "\n";
                    c *= 2;
                }
                tokenized = tokenized + " " + st.nextToken();
            }


        gameMessages.add(tokenized);
        if (gameMessages.size() > 10)
            gameMessages.remove(0);
        String message = "";
        for (String string : gameMessages) {
            message = string + "\n\n" + message;
        }

        text_message_info.setText(message);
        main_panel.updateUI();
    }


    /**
     * GUI setup methods
     **/

    private void setPanelTransparent() {
        Color transparent = new Color(0, 0, 0, 0);
        Color opaque = new Color(0, 0, 0, 100);

        panel_field_main.setBackground(transparent);
        panel_hand_1.setBackground(transparent);
        panel_hand_2.setBackground(transparent);
        panel_info_1.setBackground(transparent);
        panel_info_2.setBackground(transparent);
        panel_stack.setBackground(transparent);
        scroll_pane_1.setBackground(opaque);
        scroll_pane_2.setBackground(opaque);
        panel_field_1.setBackground(transparent);
        panel_field_2.setBackground(transparent);
        panel_info_main.setBackground(transparent);
        panel_info_1.setBackground(CreatureSimpleButton.notUsable);
        panel_info_2.setBackground(CreatureSimpleButton.notUsable);
        panel_info_phase.setBackground(transparent);
        panel_stack_main.setBackground(opaque);
        deck_player_1.setBackground(transparent);
        deck_player_2.setBackground(transparent);


        //ToDo: non so perché ma se metto questi cosi trasparenti oltre al testo poi mi disegna la carta sotto -.-
        /*text_message_info.setBackground(transparent);
        text_phase_info.setBackground(transparent);*/
    }

    private void setPanelsSize() {
        panel_hand_1.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        panel_hand_2.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        panel_field_1.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        panel_field_2.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        scroll_pane_1.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        scroll_pane_2.setSize((int) screenSize.getWidth(), (int) ((screenSize.getHeight()) / 5));
        panel_stack.setMinimumSize(new Dimension((int) screenSize.getWidth() / 10, -1));
        panel_info_main.setMinimumSize(new Dimension(CardButton.getWIDTH(), -1));
        deck_player_1.setSize(CardButton.getWIDTH(), CardButton.getHEIGHT());
        deck_player_2.setSize(CardButton.getWIDTH(), CardButton.getHEIGHT());
        panel_info_phase.setSize(CardButton.getWIDTH(), (int) ((screenSize.getHeight()) / 5));
        text_message_info.setSize(CardButton.getWIDTH(), (int) ((screenSize.getHeight()) / 5));
        text_message_info.setMaximumSize(new Dimension(CardButton.getWIDTH(), -1));
        scrollPaneCheNonDovrebbeAllargarsi.setSize(CardButton.getWIDTH(), (int) ((screenSize.getHeight()) / 5));
    }

    private void createUIComponents() {
        panel_info_phase = new JPanel(new BorderLayout()) {
            @Override
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("image/frame_phase.jpg")), 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    System.out.println("errore nel caricamento del frame");
                }
            }
        };

        main_panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("image/texture_gioco.jpg")), 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    System.out.println("errore nel caricamento della texture");
                }
            }
        };

    }


    /**
     * Thread methods
     **/

    private void startCardGameThread() {
        Thread t = new Thread(CardGame.instance);
        t.setName("CardGameThread");
        t.start();
    }

    private void startMessageCenterThread() {
        new Thread(() -> {
            while (true)
                GameGUI.instance.deliverMessage(MsgCenter.msgCenter.getState());
        }).start();
    }

    public void startPlayer1ObserverThread() {
        new Thread(() -> {
            while (true)
                label_life_1.setText("Life:  " + PlayerObserver.playerObserver.getLife1() + "    Card in Deck:" + PlayerObserver.playerObserver.getDeckSize1());
        }).start();
    }

    public void startPlayer2ObserverThread() {
        new Thread(() -> {
            while (true)
                label_life_2.setText("Life:  " + PlayerObserver.playerObserver.getLife2() + "    Card in Deck:" + PlayerObserver.playerObserver.getDeckSize2());
        }).start();
    }
}

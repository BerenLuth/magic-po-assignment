package Cardgame.Core;

import Cardgame.Controller.GUIRequests.Requests;
import Cardgame.Controller.GUIRequests.TargetRequest;
import Cardgame.Controller.Observers.MsgCenter;
import Cardgame.Controller.Observers.PhaseObserver;
import Cardgame.Controller.Observers.PlayerObserver;

import java.util.*;

import static java.lang.Thread.sleep;

/*
Player
responsabilities:
- manage life
- execute turn
- manages phases
- holds library and hand
- manages creatures in play

collaborators:
- library
- game
- phase 
- phase manager (strategy)
- card
- creature
*/
public class Player extends Observable implements Damageable {
    // basic properties: name, library, deck, and life
    private String name;
    public String name() {return name;}
    public void setName(String n) {name=n;}
    
    public boolean isRemoved() { return false; }
    public void accept(GameEntityVisitor v) { v.visit(this); }

    private final Library library = new Library(this);

    public void setDeck(Iterator<Card> deck) {
        library.add(deck);
        notifyObservers(this);
    }
    public Library getDeck() { return library; }
    
    
    
    private int life=10;
    public synchronized int getLife() {return life;}
    public void setLife(int pts) {
        life=pts;
        notifyObservers(this);
    }
    public void changeLife(int pts) {
        life+=pts;
        notifyObservers(this);
    }
    
    DamageStrategy damage_strategy_stack = new DefaultDamageStrategy(this);
    public void addDamageStrategy(DamageStrategyDecorator d) { 
        damage_strategy_stack=d.decorate(damage_strategy_stack);
    }
    public void removeDamageStrategy(DamageStrategyDecorator d) { 
        if (damage_strategy_stack instanceof DamageStrategyDecorator) {
            damage_strategy_stack = 
                    ((DamageStrategyDecorator)damage_strategy_stack).removeDecorator(d);
        }
    }
    
    public void inflictDamage(int pts) { damage_strategy_stack.inflictDamage(pts); }
    public void heal(int pts) { damage_strategy_stack.heal(pts);}
    public void lose(String s) { damage_strategy_stack.lose(s); }            
            
    public void inflictDamage(Creature c, int pts) {
        damage_strategy_stack.inflictDamage(c, pts);
        notifyObservers(this);
    }
    
    
    
    
    public Player() {
        // Aggiungo questo player agli osservatori del playerObserver.
        PlayerObserver.getPlayerObserver(this);
        addMsgObserver(MsgCenter.msgCenter);

        // Se entrambi i player sono impostati allora lancia l'osservatore in un thread,
        //  cosi monitora per conto suo la vita dei giocatori
        if(PlayerObserver.isReady()) {
            Thread playerObserver = new Thread(PlayerObserver.playerObserver);
            playerObserver.setName("Player Observer");
            playerObserver.start();
        }
        notifyObservers(this);

        phase_manager_stack.push(new DefaultPhaseManager());
        
        phases.put(Phases.DRAW, new ArrayDeque<Phase>());
        setPhase(Phases.DRAW, new DefaultDrawPhase());
        
        phases.put(Phases.UNTAP, new ArrayDeque<Phase>());
        setPhase(Phases.UNTAP, new DefaultUntapPhase());

        phases.put(Phases.COMBAT, new ArrayDeque<Phase>());
        setPhase(Phases.COMBAT, new DefaultCombatPhase());

        phases.put(Phases.MAIN, new ArrayDeque<Phase>());
        setPhase(Phases.MAIN, new DefaultMainPhase());
        
        phases.put(Phases.END, new ArrayDeque<Phase>());
        setPhase(Phases.END, new DefaultEndPhase());
        
        phases.put(Phases.NULL, new ArrayDeque<Phase>());

    }

    
    void executeTurn() {
        System.out.println("inizio executeTurn");

        notifyMsg(name + "'s turn");
        notifyMsg(name + " life " + getLife());

        if (getCreatures().isEmpty()) {
            notifyMsg ("No creatures in play ");
        } else {
            notifyMsg ("Creatures in play:");
            for (DecoratedCreature c:getCreatures()) {
                notifyMsg (c.toString());
            }
        }
        Player adversary=CardGame.instance.getCurrentAdversary();
        notifyMsg (adversary.name() + " life " + adversary.getLife());
        if (adversary.getCreatures().isEmpty()) {
            notifyMsg ("No creatures in play");
        } else {
            notifyMsg ("Creatures in play:");
            for (DecoratedCreature c:adversary.getCreatures()) {
                notifyMsg (c.toString());
            }
        }
        
        
        
        Phase cur_phase;
        while ( ( cur_phase=getPhase(nextPhase()) ) != null)  {
            try {
                cur_phase.execute();
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    


    
    

    // phase management
    
    // phases maps a phaseID to a stack of phase implemenations
    // top one is active
    private EnumMap<Phases, Deque<Phase> > phases = new EnumMap<>(Phases.class);
    public Phase getPhase(Phases p) { return phases.get(p).peek(); }
    public void setPhase(Phases id, Phase p) { phases.get(id).push(p); }
    public void removePhase(Phases id, Phase p) { phases.get(id).remove(p); }
    
    // 
    private Deque<PhaseManager> phase_manager_stack = new ArrayDeque<PhaseManager>();
    public void setPhaseManager(PhaseManager m) { phase_manager_stack.push(m); }
    public void removePhaseManager(PhaseManager m) { phase_manager_stack.remove(m); }
    public AbstractPhaseManager getPhaseManager(){return (AbstractPhaseManager) phase_manager_stack.peek();}
    public Phases currentPhase() { return phase_manager_stack.peek().currentPhase(); }
    public Phases nextPhase() { return phase_manager_stack.peek().nextPhase(); }
    
    

    
    
    // hand management
    private ArrayList<Card> hand = new ArrayList<>();
    private int max_hand_size=7;
    
    public List<Card> getHand() { return hand; }
    public int getMaxHandSize() { return max_hand_size; }
    public void setMaxHandSize(int size) { max_hand_size=size; }
    
    public void draw() {
        Card drawn = library.draw();
        hand.add(drawn);
        drawn.notifyObservers();
        notifyObservers(this);
    }
    
    public void selectDiscard() {

        TargetRequest request = new TargetRequest(hand,1);
        int i = 0;
        for (Card c: hand) {
            c.setTargetIndex(i++);
        }
        Requests.instance.addRequest(request);
        try {
            int idx=Requests.instance.getResult().get(0);
            if (idx>=0 && idx<hand.size())
                hand.remove(idx);
        }catch (InterruptedException azz){}

    }
    
    // creature management
    private final ArrayList<DecoratedCreature> creatures  = new ArrayList<>();
    public List<DecoratedCreature> getCreatures() { return creatures; }



    //targeting management
    private boolean possibleTarget;
    private int targetIndex;


    @Override
    public boolean isPossibleTarget() {
        return possibleTarget;
    }

    @Override
    public int getTargetIndex() {
        return targetIndex;
    }

    @Override
    public void setTargetIndex(int i) {
        targetIndex = i;
    }

    @Override
    public void setPossibleTarget() {
        possibleTarget = true;
    }

    @Override
    public void endTargeting() {
        possibleTarget = false;
    }

    //private final ArrayList<Creature> creatures = new ArrayList<>();
    //public List<Creature> get_creatures() {return creatures;}
    

    
}

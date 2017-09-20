package cardgame.Game;

import cardgame.Cards.Card;
import cardgame.Cards.Creature;
import cardgame.Game.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Scanner;

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
public class Player {
    // basic properties: name, library, deck, and life
    private String name;
    private boolean combatPrevent;
    private Player opponent;
    int damagePrevent = 0;

    public Player getOpponent() {
        return opponent;
    }
    protected void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public String get_name() {return name;}
    public void set_name(String n) {name=n;}

    private final Library library = new Library(this);

    public void set_deck(Iterator<Card> deck) { library.add(deck); }
    public Library get_deck() { return library; }
    
    
    
    private int life=10;
    public int get_life() {return life;}
    
    // need to attach strategy/decorator
    public void inflict_damage(int pts) {
        int sup = pts;
        if(pts < damagePrevent)
            pts = 0;
        else
            pts -= damagePrevent;
        life -= pts;
        if (life <=0) lose("received fatal damage");
        if(damagePrevent - sup < 0)
            damagePrevent = 0;
        else
            damagePrevent-=sup;
        System.out.println(name+" punti rimanenti: "+get_life());

    }
    
    public void heal(int pts) { life += pts; }
            
            
    // player looses. might need strategy/decorator
    public void lose(String s) { throw new EndOfGame(name + " lost the game: "+ s); }

    public int getDamagePrevent() {
        return damagePrevent;
    }

    public void setDamagePrevent(int damagePrevent) {
        this.damagePrevent += damagePrevent;
    }

    public void resetDamagePrevent(){damagePrevent=0;}
    public boolean isCombatPrevent() {
        return combatPrevent;
    }

    public void setCombatPrevent(boolean combatPrevent) {
        this.combatPrevent = combatPrevent;
    }

    public Player() {
        
        phase_manager_stack.push(new DefaultPhaseManager(phases));
        
        phases.put(Phases.DRAW, new ArrayDeque<Phase>());
        set_phase(Phases.DRAW, new DefaultDrawPhase());
        
        phases.put(Phases.UNTAP, new ArrayDeque<Phase>());
        set_phase(Phases.UNTAP, new DefaultUntapPhase());
        
        phases.put(Phases.COMBAT, new ArrayDeque<Phase>());
        set_phase(Phases.COMBAT, new DefaultCombatPhase());
        
        phases.put(Phases.MAIN, new ArrayDeque<Phase>());
        set_phase(Phases.MAIN, new DefaultMainPhase());
        
        phases.put(Phases.END, new ArrayDeque<Phase>());
        set_phase(Phases.END, new DefaultEndPhase());
        
        phases.put(Phases.NULL, new ArrayDeque<Phase>());
    }

    
    void execute_turn() {
        System.out.println(name + "'s turn");
        Phase cur_phase;
        while ((cur_phase=next_phase())!=null) {
            cur_phase.execute();
        }
    }
    
    


    
    

    // phase management
    
    // phases maps a phaseID to a stack of phase implemenations
    // top one is active
    private EnumMap<Phases, Deque<Phase> > phases = new EnumMap<>(Phases.class);
    public Phase get_phase(Phases p) { return phases.get(p).peek(); }
    public void set_phase(Phases id, Phase p) { phases.get(id).push(p); }
    public void remove_phase(Phases id, Phase p) { phases.get(id).remove(p); }
    
    // 
    private Deque<PhaseManager> phase_manager_stack = new ArrayDeque<PhaseManager>();
    public void set_phase_manager(PhaseManager m) { phase_manager_stack.push(m); }
    public void remove_phase_manager(PhaseManager m) { phase_manager_stack.remove(m); }
    Phases current_phase_id() { return phase_manager_stack.peek().current_phase(); }
    Phase next_phase() { return get_phase(phase_manager_stack.peek().next_phase()); }
    
    

    
    
    // hand management
    private ArrayList<Card> hand = new ArrayList<>();
    private int max_hand_size=7;
    
    public List<Card> get_hand() { return hand; }
    public int get_max_hand_size() { return max_hand_size; }
    public void set_max_hand_size(int size) { max_hand_size=size; }
    
    public void draw() {
        Card drawn = library.draw();
        System.out.println(get_name()+" draw card: " + drawn.name());
        hand.add(drawn);
    }
    
    public void select_discard() {
        Scanner reader = CardGame.instance.get_scanner();
        
        System.out.println(get_name()+" Choose a card to discard");
        for(int i=0; i!=hand.size(); ++i) {
            System.out.println(Integer.toString(i+1)+") " + hand.get(i) );
        }
        int idx= reader.nextInt()-1;
        if (idx>=0 && idx<hand.size())
            hand.remove(idx);         
    }
    
    // creature management
    private final ArrayList<Creature> creatures = new ArrayList<>();
    
    public List<Creature> get_creatures() {return creatures;}
    
    // destroy a creature in play
    public void destroy(Creature c) {
        System.out.println(c.name() + " (" + name + ") distrutta");
        creatures.remove(c);
    }
    
}

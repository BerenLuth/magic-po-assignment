package cardgame.Game;

import cardgame.Cards.Effect;

import java.util.ArrayDeque;
import java.util.Iterator;


public class CardStack implements Iterable<Effect> {
    private final ArrayDeque<Effect> stack = new ArrayDeque<>();
    
    public Iterator<Effect> iterator() { return stack.iterator(); }
    
    public void add(Effect e) { 
        stack.push(e); 
    }
    
    public void remove(Effect e) { stack.remove(e); }

    public int getSize(){
        return stack.size();
    }

    public Effect pop(){
        return stack.pop();
    }

    public boolean contains(Effect e){
        return stack.contains(e);
    }
    
    public void resolve() {
        while(!stack.isEmpty()) { 
            Effect e = stack.pop();
            
            System.out.println("Stack: resolving " + e);
            
            e.resolve(); 
        }
    }
}

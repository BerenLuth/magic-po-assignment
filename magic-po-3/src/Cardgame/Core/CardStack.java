package Cardgame.Core;

import Cardgame.Controller.Observers.MsgCenter;

import java.util.ArrayList;
import java.util.Iterator;


public class CardStack extends Observable implements Iterable<Effect>{

    private final ArrayList<Effect> stack = new ArrayList<>();

    public CardStack(){
        this.addObserver(MsgCenter.msgCenter);
    }
    
    public Iterator<Effect> iterator() { return stack.iterator(); }

    public ArrayList<Effect> getStack(){
        return stack;
    }
    
    public void push(Effect e) { 
        stack.add(e); 
    }
    
    public Effect pop() {
        return stack.remove(stack.size()-1);
    }
    
    public void remove(Effect e) {  
        stack.remove(e);  
        e.remove();
    }
    
    public int size() { return stack.size(); }
    public Effect get(int i) { return stack.get(i); }
    public int indexOf(Effect e) { return stack.indexOf(e); }
    
    public void resolve() {
        while(!stack.isEmpty()) { 
            Effect e = pop();
            
            notifyObservers("Stack: resolving " + e);
            e.remove();
            e.resolve();
        }
    }
}

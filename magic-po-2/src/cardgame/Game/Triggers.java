package cardgame.Game;

import java.util.ArrayList;


public class Triggers {
    private class Pair { 
        public int filter; 
        public TriggerAction action;
        public Pair(int f, TriggerAction a) { filter=f; action=a; }
        
    }
    
    private ArrayList<Triggers.Pair> actions = new ArrayList<Triggers.Pair>();

    public ArrayList<Pair> getActions() {
        return actions;
    }

    public void register(int phase_trigger, TriggerAction a) {
        actions.add(new Triggers.Pair(phase_trigger, a));
    }
    
      
    public void trigger(int phase) {
        for (int i=actions.size()-1; i>=0; --i)
        {
            Triggers.Pair p=actions.get(i);
            if ((p.filter & phase)!=0) {
                actions.remove(p);
                p.action.execute();
            }
        }
    }
}

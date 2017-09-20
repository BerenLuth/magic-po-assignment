/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Magic.Game;

import Magic.Cards.Command;
import java.util.ArrayList;


public abstract class Phase implements PhaseInterface {

    private ArrayList<Command> begin = new ArrayList<>();
    private ArrayList<Command> end = new ArrayList<>();

    /**
     * adds the command to be invoked at the beginning of the phase
     * @param c command to be added
     */
    public ArrayList<Command> addBeginCommand(Command c) {
        begin.add(c);
        return begin;
    }


    public ArrayList<Command> getBegin() {
        return begin;
    }

    public ArrayList<Command> getEnd() {
         return end;
    }

    /**
     * adds the command to be invoked at the end of the phase
     * @param c command to be added
     */
    public ArrayList<Command> addEndCommand(Command c) {
        end.add(c);
        return end;
    }

    /**
     * executes commands in the list passed as input and removes them if they are finished
     * @param vect list of commands
     */
    public void execute(ArrayList<Command> vect){
        for(Command c: vect) {
            c.invoke();
        }
        int i=0;
        Command c;
        while(i<vect.size()){
            c = vect.get(i);
            c.removeCommand();
            i++;
        }
    }
}

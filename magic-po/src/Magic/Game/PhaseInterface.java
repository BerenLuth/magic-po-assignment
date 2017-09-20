package Magic.Game;

import Magic.Cards.Command;
import Magic.GameException.EndException;
import Magic.Personal.Player;

import java.util.ArrayList;

public interface PhaseInterface {

    void start(Player g) throws EndException;
    ArrayList<Command> addBeginCommand(Command c);
    ArrayList<Command> addEndCommand(Command c);
    void execute(ArrayList<Command> c);
}

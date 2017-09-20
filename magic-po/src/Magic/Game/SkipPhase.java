package Magic.Game;

import Magic.Cards.Command;
import Magic.Personal.Player;

import java.util.ArrayList;


public class SkipPhase implements PhaseInterface {


	/**
	 * does nothing (Null object
	 * @param g does nothing (Null object)
	 */
	public void start(Player g){
	}

	/**
	 * does nothing (Null object)
	 * @param c does nothing (Null object)
	 */
	@Override
	public ArrayList<Command> addBeginCommand(Command c) {
		return null;
	}

	/**
	 * does nothing (Null object)
	 * @param c command to be added
	 */
	@Override
	public ArrayList<Command> addEndCommand(Command c) {
		return null;
	}

	/**
	 * does nothing (Null object)
	 * @param vect does nothing (Null object)
     */
	@Override
	public void execute(ArrayList<Command> vect){
	}


}
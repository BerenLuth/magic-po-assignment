package Magic.GameException;


import Magic.Personal.Player;

public class LifeException extends EndException{

    public LifeException(Player loser){
        super(loser);
        System.out.println("I punti vita di " + loser.getName() + " sono scesi a 0");
    }
}

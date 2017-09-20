package Magic.GameException;


import Magic.Personal.Player;

public class EndException extends Exception{

    public EndException(Player loser){
        System.out.println("\n"+loser.getOpponent().getName()+" ha vinto");
        System.out.println(loser.getName()+" ha perso\n");
    }
}

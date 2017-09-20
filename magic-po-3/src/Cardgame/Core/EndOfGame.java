package Cardgame.Core;

public class EndOfGame extends RuntimeException {


    public EndOfGame() {
    }


    public EndOfGame(String msg) {
        super(msg);
    }
}

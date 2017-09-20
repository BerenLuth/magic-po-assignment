package Cardgame.Controller;


public class DiscardException extends RuntimeException {

    public DiscardException(){}

    public DiscardException(String n){
        super(n);
    }
}

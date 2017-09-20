package Magic.GameException;


public class DrawException extends Exception {

    public DrawException(){
        System.out.println("Sono finite le carte!");
    }
}

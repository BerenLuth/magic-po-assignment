package Magic.Game;

import java.util.ArrayList;

import Magic.Cards.Command;
import Magic.Personal.Player;
import Magic.Utils.Reader;

public class MainPhase extends Phase {

    /**
     * starts this phase execution
     * @param g the player who is running the phase
     */
    public void start(Player g){
        execute(getBegin());
        System.out.println("\tInizia la MainPhase");

        System.out.println("Vuoi giocare una carta? ('si')");
        String choice = Reader.readString();
        if(g.getHand().sizeMano() != 0 && choice.equals("si")) {
            System.out.println("Scegli la carta da giocare");
            g.getHand().printHand();
            int n = Reader.readIntRange(g.getHand().sizeMano());
            g.playSpell(n); // MainPhase --> Player(->Hand->Carta) --> Magic.Game --> GameStack

            g.getHand().getCarteMano().remove(n);
            //GameStack.getInstance().fill(g.getOpponent());
            GameStack.getInstance().fillStack(g.getOpponent());
            GameStack.getInstance().resolve();
        }else{
            if(choice.equals("si"))
                System.out.println(g.getName() + " non ha carte in mano da giocare");
            else
                System.out.println("Va bene :)");
        }

        execute(getEnd());
    }



}
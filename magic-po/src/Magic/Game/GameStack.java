package Magic.Game;

import java.util.Stack;

import Magic.Cards.Instant;
import Magic.Cards.Permanent;
import Magic.Cards.Spell;
import Magic.Personal.Player;
import Magic.Utils.Reader;

public class GameStack {
    private static final GameStack gm = new GameStack();
    private  Stack<Spell> stack;

    private GameStack() {
        this.stack = new Stack<>();
    }

    /**
     * getter of gamestack instance
     * @return instance of gamestack
     */
    public static GameStack getInstance(){
        return gm;
    }

    /**
     * getter of the stack structure
     * @return stack
     */
    public Stack<Spell> getStack() {
        return stack;
    }

    /**
     * adds a spell to the stack
     * @param m spell to be added to the stack
     */
    public void addMagia(Spell m){
        stack.push(m);
    }


    //Chiede al giocatore se vuole rispondere ad una magia
    //In caso positivo chiede quale magia giocare e lancia fillStack per l'avversario
    //fino a quando qualcuno non smette di rispondere o non ha più istantanee disponibili

    /**
     * this method handles responses with Instants.
     * @param player target player who is asked to play the Instant.
     */
    public void fillStack(Player player){
        if(player.getHand().hasInstant()){
            System.out.println(player.getName() + ": vuoi rispondere con un'istantanea?");
            boolean answer = playerAnswer();
            //Legge la risposta di player e torna true (si) o false (no)
            if(answer){
                Spell spell = playerCardChoice(player);
                addMagia(spell);
                //Rilancia fillStack con l'avversario fino a quando uno non risponderà "no"
                fillStack(player.getOpponent());
            }
        }
        else
            System.out.println(player.getName() + " non ha istantanee con cui rispondere.\n");

    }

    /**
     * resolves the stack
     */
    public void resolve(){
        if(!stack.isEmpty())
            System.out.println("Risoluzione stack:");
        while(!stack.isEmpty()) {
            Spell m = stack.pop();
            System.out.println(m.getClass().getSimpleName());
            m.effect();
            if(m instanceof Permanent)
                ((Permanent) m).joinBattlefield();
        }
    }

    //Legge la risposta del giocatore, e se non è valida la richiede
    //Continua finchè non trova un si o un no
    private boolean playerAnswer(){
        String answer;
        boolean ok;
        do{
            System.out.println("Puoi rispondere 'si' oppure 'no'");
            answer = Reader.readString();
            ok = answer.equals("si") | answer.equals("no");
            if(!ok)
                System.out.println("Non ho capito, riprova!");
        }while(!ok);
        return (answer.equals("si"));
    }

    //Stampa le istantanee di player e dopo che il giocatore ha scelto quale giocare, ritorna l'istantanea
    //continua fin che il giocatore non seleziona la carta valida
    private Spell playerCardChoice(Player player) {
        Spell spell;
        do{
            System.out.println("Queste sono le carte con cui puoi rispondere");
            player.getHand().printInstantInHand();

            spell = player.getHand().getCarta(Reader.readIntRange(player.getHand().sizeMano()));
            if (!(spell instanceof Instant)){
                System.out.println("La carta selezionata non è un'istantanea");
            }
        }while (!(spell instanceof Instant));
        player.getHand().removeCarta(spell);
        return spell;
    }
}

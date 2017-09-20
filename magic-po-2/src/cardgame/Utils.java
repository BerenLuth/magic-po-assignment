package cardgame;

import cardgame.Cards.Creature;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Fabio on 24/03/2016.
 */
public class Utils {

    //funzioni statiche utilizzabili ovunque per semplificare il codice

    /**
     * reads the string from keyboard (standard input)
     * @return the line that was read
     */
    public static String readString() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    /**
     * reads an integer from keyboard (standard input)
     * @return the integer read
     */
    public static int readInt() {
        String s;
        Scanner input = new Scanner(System.in);
        s = input.nextLine();
        try{
            Integer.parseInt(s);
        }catch (NumberFormatException nfe){
            System.out.println("Scrivi qualcosa di sensato!");
            return readInt();
        }
        return Integer.parseInt(s);
    }

    /**
     * reads and integer that represents the number of the card to be chosen.
     * checks for errors in choosing the card
     * @param inf min value
     * @param sup max value
     * @return the integer read
     */
    public static int readIntRange(int inf, int sup){
        int i = readInt();
        if(i>= inf && i<=sup)
            return i;
        else{
            System.out.println("Il numero non è valido");
            return readIntRange(inf, sup);
        }
    }

    public static int readIntRange(int value){
        return readIntRange(0, value);
    }

    public static Creature creatureTargetChoice(ArrayList<Creature> creatures){
        int x = 1;
        //System.out.println(owner.get_name() + ": SCegliere la creatura target: ");

        for(Creature c: creatures){
            System.out.println(x + ". " + c.name()
                    +"(" + c.getOwner().get_name()
                    + " " + c.getAttack()
                    + "/" + c.get_toughness()+")");
            x++;
        }
        x = Utils.readIntRange(1, creatures.size());   //leggo l'indice dall'utente
        x--;    //l'indice parte da 1 quindi lo normalizzo a 0

        return creatures.get(x);    //ritorno la creatura scelta come target
    }
}

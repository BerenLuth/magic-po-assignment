package Magic.Utils;

import java.util.Scanner;


public class Reader {

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
     * @param range maximum value
     * @return the integer read
     */
    public static int readIntRange(int range){
        int i = readInt();
        if(i<range && i>=0)
            return i;
        else{
            System.out.println("Il numero non è valido");
            return readIntRange(range);
        }
    }



}

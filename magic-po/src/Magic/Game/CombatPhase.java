package Magic.Game;

import Magic.Cards.Permanent;
import Magic.Personal.Field;
import Magic.Personal.Player;

import java.util.ArrayList;
import java.util.Scanner;

public class CombatPhase extends Phase {

    private ArrayList<Permanent> attaccanti = new ArrayList<>();
    private ArrayList<Permanent> bloccanti = new ArrayList<>();

    /**
     * starts this phase execution
     * @param g the player who is running the phase
     */
    public void start(Player g){
        // TODO: implementare la combat phase
        //sceltaAttaccanti(g.getField());
        //sceltaBloccanti(g.getOpponent().getField());
        System.out.println("\tInizia la CombatPhase");
        execute(getBegin());
        execute(getEnd());
    }

    private void sceltaAttaccanti(Field f){
        System.out.println("Vuoi attaccare");
        Scanner input = new Scanner(System.in);
        String s = input.nextLine();
        if(s.equals("s")){
            System.out.println("Con quante creature vuoi attaccare?");
            input = new Scanner(System.in);
            s = input.nextLine();
            int n = Integer.parseInt(s);
            System.out.println("Scegli le creature attaccanti");
            while(n>0){
                input = new Scanner(System.in);
                s = input.nextLine();
                n = Integer.parseInt(s) % f.getField().size();
                attaccanti.add(f.selectTarget(n));
                n--;
            }
        }
    }

    private void sceltaBloccanti(Field f){
        System.out.println("Vuoi bloccare?");
        Scanner input = new Scanner(System.in);
        String s = input.nextLine();
        if(s.equals("s")){
            System.out.println("Con quante creature vuoi bloccare?");
            input = new Scanner(System.in);
            s = input.nextLine();
            int n = Integer.parseInt(s);
            System.out.println("Scegli le creature bloccanti");
            while(n>0){
                input = new Scanner(System.in);
                String x = input.nextLine();
                n = Integer.parseInt(x) % f.getField().size();
                bloccanti.add(f.selectTarget(n));
                n--;
            }
        }
    }

    private void assegnazioneCreatureBloccanti(Field f){
        Scanner input;
        String s;
        int n;
        for(Permanent c: attaccanti){
            System.out.println("Vuoi bloccare?");
            input = new Scanner(System.in);
            s = input.nextLine();
            n = Integer.parseInt(s);
            f.selectTarget(n);

        }
    }
    
}
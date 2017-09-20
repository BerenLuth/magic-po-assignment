package cardgame.cards;

import cardgame.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabio on 16/05/2016.
 */
public class AbzanAdvantage extends AbstractCard {
    static private final String cardName = "Abzan Advantage";

    static private StaticInitializer initializer =
            new StaticInitializer(cardName, new CardConstructor() {
                public Card create() { return new AbzanAdvantage(); }
            } );

    @Override
    public Effect getEffect(Player owner) {
        return new AbzanAdvantageEffect(owner, this);
    }

    @Override
    public String type() {
        return "Instant";
    }

    @Override
    public String ruleText() {
        return "Target player sacrifies an enchantment." +
                "\nBolster 1. (Choose a creature with the least toughness among creatures you control and put a +1/+1 counter on it.)";
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public String name() {
        return cardName;
    }

    @Override
    public String toString() { return name() + " [" + ruleText() +"]";}

    private class AbzanAdvantageEffect extends AbstractCardEffect implements TargetingEffect{
        DecoratedCreature sacrificeTarget;
        DecoratedCreature bolsterTarget;
        Player playerTarget;

        protected AbzanAdvantageEffect(Player p, Card c) {
            super(p, c);
        }

        public boolean play() {
            pickTarget();
            return super.play();
        }

        /**
         * Controlla se i target sono vuoti o se sono stati rimossi, se sono validi applica gli effetti.
         */
        @Override
        public void resolve() {
            if(sacrificeTarget != null)
                if(sacrificeTarget.isRemoved()){
                    System.out.println("Try to attach " + cardName + " to removed creature");
                }
                else {
                    System.out.println(playerTarget + " sacrifice target: " + sacrificeTarget.name());
                    sacrifice();
                }

            if(bolsterTarget != null)
                if(bolsterTarget.isRemoved()){
                    System.out.println("Try to attach " + cardName + " to removed creature");
                }
                else {
                    System.out.println("Bolster 1 apply permament +1/+1 to " + bolsterTarget);
                    bolster();
                }
        }

        @Override
        public void pickTarget() {
            System.out.println( owner.name() + ": choose target for " + name() );
            System.out.println("1) " + CardGame.instance.getPlayer(0).name());
            System.out.println("2) " + CardGame.instance.getPlayer(1).name());

            //Scelgo il giocatore target che dovrà sacrificare la creatura.
            int idx;
            //Controllo in un ciclo perché il giocatore target non può essere null.
            do {
                idx = CardGame.instance.getScanner().nextInt() - 1;
                if (idx<0 || idx>1)
                    System.out.println("Seleiona un indice valido (0 o 1)");
            }while (idx<0 || idx>1);

            playerTarget=CardGame.instance.getPlayer(idx);

            //Se c'è un target
            if(playerTarget != null) {
                //Se il target ha creature ne scelgo una
                if (!playerTarget.getCreatures().isEmpty()) {
                    chooseCreature(playerTarget);
                } else
                    System.out.println("There are no creatures to sacrifice.");
            }

            //BOLSTER
            //Se il proprietario della carta ha creature, scelgo quella con la vita minore per applicare il +1/+1
            if(!owner.getCreatures().isEmpty()){
                possibleBolsterTarget();
            }else
                System.out.println("Bolster 1 doesn't have target.");
        }

        /**
         * Dato un giocatore, sceglie una creatura target da distruggere.
         * (Chiamato da pickTarget())
         */
        private void chooseCreature(Player targetPlayer){
            System.out.println( playerTarget.name() + ": choose target to sacrifice for " + name() );
            int i = 1;
            for (DecoratedCreature c: targetPlayer.getCreatures()) {
                System.out.println( i + ") " + c);
                i++;
            }

            int idx= CardGame.instance.getScanner().nextInt()-1;
            if (idx<0 || idx>=targetPlayer.getCreatures().size()){
                System.out.println("Error while picking target.\nRetry!");
                chooseCreature(targetPlayer);   //Per semplificare il controllo, se l'indice è sbagliato fa ripartire il metodo
            }else
                sacrificeTarget = targetPlayer.getCreatures().get(idx); //Salvo il target da sacrificare.
        }

        /**
         * Trova i possibili target di bolster
         * (Chiamato da pickTarget())
         */
        private void possibleBolsterTarget(){
            ArrayList<DecoratedCreature> targets = new ArrayList<>();
            Integer minToughness = null;
            for(DecoratedCreature c : owner.getCreatures()){
                if(minToughness == null) {
                    minToughness = c.toughness();
                    targets.add(c);
                }
                else
                    if(minToughness == c.toughness())
                        targets.add(c);
                    else
                        if(minToughness > c.toughness()){
                            minToughness = c.toughness();
                            targets.clear();
                            targets.add(c);
                        }
            }

            if(!targets.isEmpty()){
                chooseBolster(targets);   //Se ci sono dei possibili target lancio
            }else
                //Non dovrebbe mai entrarci perché c'è già il controllo sul picktarget
                System.out.println("There are no creature for Bolster 1");
        }

        /**
         * Sceglie il target per Bolster
         * (Nel caso in cui ci siano più target la scelta spetta all'utente)
         * @param creatureList lista contente i possibili target
         */
        private void chooseBolster(List<DecoratedCreature> creatureList){
            //Controllo se c'è un solo target, in caso l'effetto viene applicato senza scelta dell'utente.
            if(creatureList.size() == 1) {
                bolsterTarget = creatureList.get(0);
                System.out.println("There are only one possible target for bolster 1: " + bolsterTarget.name());
            }
            else{
                int i = 1;
                System.out.println( owner.name() + ": choose target for " + name()  + "(Bolster 1)");
                for(DecoratedCreature d : creatureList){
                    System.out.println( i + ") " + d);
                    i++;
                }

                int idx= CardGame.instance.getScanner().nextInt()-1;
                if (idx<0 || idx>=creatureList.size()){
                    System.out.println("Error while picking target.\nRetry!");
                    chooseBolster(creatureList);
                }else bolsterTarget = creatureList.get(idx);
            }
        }

        /**
         * Distrugge la creatura target da sacrificare.
         * (Chiamato da resolve())
         */
        private void sacrifice(){
            sacrificeTarget.destroy();
        }

        /**
         * Applica il decorator alla creatura target per bolster
         * (Chiamato da resolve())
         */
        private void bolster(){
            AbzanAdvantageDecorator abzanDecorator = new AbzanAdvantageDecorator();
            bolsterTarget.addDecorator(abzanDecorator);
        }
    }

    class AbzanAdvantageDecorator extends AbstractCreatureDecorator {
        public int power() { return decorated.power()+1; }
        public int toughness() { return decorated.toughness()+1; }
    }

}

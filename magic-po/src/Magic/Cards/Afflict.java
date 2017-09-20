package Magic.Cards;

import Magic.Game.EndPhase;
import Magic.Personal.Player;

// Esempio carta: -1/-1 alla creatura fino alla endPhase
public class Afflict extends Instant implements Command {

	Creature target;
	Player opponent;
	public Afflict(){}

	/**
	 * Set the owner of this card
	 * @param owner the owner of the card
     */
	@Override
	public void setOwner(Player owner){
		super.setOwner(owner);
		opponent = owner.getOpponent();
	}

	/**
	 * Execute the effect of the card
	 *
     */
	public void effect(){
		System.out.println("\tAfflict fa 1 danno alla prima creatura sul campo!\n");
		// card sarà la creatura target
		// Aggiungi Command che dice che l'effetto finisce nella EndPh
		setSave(addEndCommand(this, EndPhase.class, getOwner()));
		// Selezioni target
		if (opponent.getField().getField().size()!=0) {
			target = (Creature) opponent.getField().selectTarget(0);
			// Effetto da applicare al target
			target.setAttacco(target.getAttacco() - 1);
			target.setCostituzione(target.getCostituzione() - 1);
			if(target.getCostituzione()<=0)
				target=null;
		}
		else
			target = null;
	}


	/**
	 * End of effect:
	 * Restore previous parameters of the target card
	 */
	public void invoke(){
		if(target!=null) {
			target.setAttacco(target.getAttacco() + 1);
			target.setCostituzione(target.getCostituzione() + 1);
			System.out.println(target.getClass().getSimpleName()+" attacco = "+target.getAttacco()+" costituzione = "+target.getCostituzione());
		}
	}

	/**
	 * removes the command
	 */
	public void removeCommand(){
		getSave().remove(this);
	}
}
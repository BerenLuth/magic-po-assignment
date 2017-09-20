package Magic.Cards;


import Magic.Game.DrawPhase;
import Magic.Game.EndPhase;
import Magic.Game.PhaseManager;
import Magic.Game.SkipPhase;
import Magic.Personal.Player;

public class Fatigue extends Sorcery implements Command {

	private int turni=1;
	Player opponent;
	SkipPhase nevv;
	DrawPhase old; // L'idea di questo attributo sarebbe quella di preservare
					// le eventuali caratteristiche attuali di quela fase in modo 
					// tale da non perderle al ripristino.

	public Fatigue(){}

	/**
	 * setter of owner of this card
	 * @param owner parameter to be set as owner
     */
	@Override
	public void setOwner(Player owner){
		super.setOwner(owner);
		opponent = owner.getOpponent();
	}

	/**
	 * execute effect of this card
	 */
	@Override
	public void effect(){
		System.out.println("\tFatigue fa saltare la DrawPhase dell'avversario!\n");
		// Aggiungi command che fa ritornare tutto come prima
		setSave(addEndCommand(this, EndPhase.class, opponent));
		// Dal PhaseManager dell'avversario chiamo metodo replace
		// che sostituisce il combat con una SkipPhase
		PhaseManager p= opponent.getGF();
		old = (DrawPhase) p.getPhase(DrawPhase.class);
        nevv = new SkipPhase();
		opponent.getGF().replacePhase(old, nevv);
	}

	/**
	 * End of effect:
	 * Restore previous parameters of the target card
	 */
	@Override
	public void invoke(){
		// Rimette per il prossimo turno la combat phase che aveva
		// prima di subire l'effetto, con le vecchie caratteristiche.
		turni--;

	}

	/**
	 * removes the command if it's effect is over.
	 * otherwise nothing happens.
	 */
	public void removeCommand(){
		if(turni==0){
			opponent.getGF().replacePhase(nevv, old);
			getSave().remove(this);

		}
	}

}
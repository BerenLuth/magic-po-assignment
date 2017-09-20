package Magic.Game;

import Magic.GameException.EndException;
import Magic.Personal.Player;

public class Game {
	//private static final Magic.Game game= new Magic.Game();
	private static final Game game = new Game();
	private Boolean end;
	private Player player1;
	private Player player2;

	private Game(){}

	/**
	 * setter of players and standard execution of this class.
	 * This method must be called in order to have a Game instance.
	 * @param pl1 player 1
	 * @param pl2 player 2
     */
	public void setGame(Player pl1, Player pl2){
		this.player1 = pl1;
		this.player2 = pl2;
		this.end = false;
		startGame();
	}

    /**
	 * getter of Game instance
	 * @return instance of Game
	 */
	public static Game getInstance(){
            return game;
	}

	/**
	 * getter end parameter
	 * @return parameter end
     */
    public boolean isEnd(){
            return end;
	}

    /**
	 * setter of parameter End to true.
	 */
    public void setEnd(){
            end=true;
        }

	/**
	 * message of end game.
	 * @param player winner of the game
     */
	public void EndGame(Player player){
            System.out.println("il giocatore"+player.getOpponent().getName()+"ha vinto\n");
            System.out.println("il giocatore"+player.getName()+"ha vinto\n");
	}

	/**
	 * this method starts the effective game.
	 * deals with setting basic information of players and first draw.
	 */
	public void startGame(){
		player1.setOpponent(player2);
		player1.startingDeck(); // Per un discorso di trasparenza, inoltriamo la resp. al giocatore che poi la inoltra all deck
		
		player2.setOpponent(player1);
		player2.startingDeck();
		Turn turn = new Turn(player1, player2);

		try{
			//pesca iniziale di 5 carte
			player1.initDraw();
			player2.initDraw();
		}catch (EndException e){
			System.out.println("Uno dei due mazzi è inizializzato male");
		}

		//Cominciano i turni, inizio effettivo del gioco
		turn.start();
	}


}
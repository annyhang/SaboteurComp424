package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {
	
	private boolean hasBonus = false;
	private boolean hasMalus = false;
	// private boolean isMalused = false;		this exists as a getter: SaboteurBoardState.getNbMalus(int playerNb)
	
	private boolean hasMapCard = false;
	private int heightPathGoal = 7;					//check???
	private boolean hasDestroyCard = false;
	
	private boolean isNuggetFound = false;			
	private int[] whereIsNugget = {-1};				//format of board???
		
	private ArrayList<Integer[]> existingPaths = new ArrayList<Integer[]>();

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260803297");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
    	//get the updated board state
    	
    	//MCTS to get the best move
    	

    	return null;
    }
    

    
    
}
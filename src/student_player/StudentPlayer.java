package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

import java.util.ArrayList;

//added for updating myBoard
import Saboteur.cardClasses.*;


/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {
	
	int roundNumber = 1;

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
    	if (roundNumber == 1) {
    		
    	}
    	MyTools myTools = new MyTools(BoardState);
    	SaboteurMove myMove = myTools.getMyMove();
    	return myMove;
    }
	
	
}
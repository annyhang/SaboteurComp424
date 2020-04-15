package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

import java.util.ArrayList;
import java.util.Arrays;

//added for updating myBoard
import Saboteur.cardClasses.*;


/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {
	
	int roundNumber = 1;
	
	int[][] intBoard;
	SaboteurTile[][] tileBoard;
	StudentBoardState previousBS;

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
        System.out.println("student player acting as player number: "+boardState.getTurnPlayer());

    	StudentBoardState studentBS = new StudentBoardState(boardState);

    	//initialise the empty boards if we are player 2
    	if (studentBS.getPlayerNumber() == 2 && roundNumber == 1) {
    		this.intBoard = studentBS.getInitIntBoard();
    		this.tileBoard = studentBS.getInitTileBoard();
    	}
    	
    	//save the board to get the opponent's move
    	this.intBoard = studentBS.getIntBoard();
    	this.tileBoard = studentBS.getTileBoard();
    	
    	//choose a move
    	
    	SaboteurMove myMove = studentBS.chooseMove();
    	
    	//update attributes
    	roundNumber++;
    	this.previousBS = studentBS;
    	
//    	System.out.println(Arrays.toString(studentBS.getNugget()));
//    	int[] posPlayed = myMove.getPosPlayed();
//    	System.out.println("==" + myMove.getCardPlayed().getName() + " " + posPlayed[0] + " " + posPlayed[1]);
//    	//
//    	System.out.println("This is the board status: " + studentBS.getBoardStatus());

    	return myMove;
    }
	
	
}
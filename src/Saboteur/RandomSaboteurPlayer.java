package Saboteur;

import java.util.Arrays;

import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

/**
 * @author mgrenander
 */
public class RandomSaboteurPlayer extends SaboteurPlayer {	
	
	private int[][] myBoard;
	int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };

	
    public RandomSaboteurPlayer() {
        super("RandomPlayer");
    }

    public RandomSaboteurPlayer(String name) {
        super(name);
    }

    @Override
    public Move chooseMove(SaboteurBoardState boardState) {
        System.out.println("random player acting as player number: "+boardState.getTurnPlayer());


    	SaboteurTile[][] hiddenBoard = boardState.getHiddenBoard();
    	this.myBoard = boardState.getHiddenIntBoard().clone();
    	
        for (int i=0; i<myBoard.length; i++) {
        	for (int j=0; j<myBoard.length; j++) {
        		if (myBoard[i][j] < 0) {
        			System.out.print(" ");
        		} 
        		else if(hiddenBoard[i/3][j/3].getIdx().contentEquals("entrance")) {
        			System.out.print("o");
        		}
        		else {
        			System.out.print(myBoard[i][j]);
        		}
        		
        	}
        	System.out.println();
        }
    	
        return  boardState.getRandomMove();
    }
}

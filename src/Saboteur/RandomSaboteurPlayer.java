package Saboteur;

import java.util.Arrays;

import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

/**
 * @author mgrenander
 */
public class RandomSaboteurPlayer extends SaboteurPlayer {	
	
	private int[][] myBoard;
	
    public RandomSaboteurPlayer() {
        super("RandomPlayer");
    }

    public RandomSaboteurPlayer(String name) {
        super(name);
    }

    @Override
    public Move chooseMove(SaboteurBoardState boardState) {
        System.out.println("random player acting as player number: "+boardState.getTurnPlayer());
        
//    	SaboteurTile[][] hiddenBoard = boardState.getHiddenBoard();
//    	this.myBoard = boardState.getHiddenIntBoard().clone();
//    	int objectiveNb = 2;
//    	
//    	//replace empty positions to objectives value
//    	for (int i=0; i<hiddenBoard.length; i++) {
//    		for (int j=0; j<hiddenBoard.length; j++) {
//    			if (hiddenBoard[i][j]!=null) {
//	    			if (hiddenBoard[i][j].getIdx().equals("8")) {
//		    			for (int h=0; h<3; h++) {
//		    				for (int k=0; k<3; k++) {
//			    				this.myBoard[i*3+h][j*3+k] = objectiveNb;
//		    				}
//		    			}
//		    			objectiveNb++;
//	    			}
//    			}
//    		}
//    	}
//
//        for (int i=0; i<myBoard.length; i++) {
//        	for (int j=0; j<myBoard.length; j++) {
//        		if (myBoard[i][j] < 0) {
//        			System.out.print(myBoard[i][j]);
//        		} else {
//        			System.out.print(myBoard[i][j]+""+myBoard[i][j]);
//        		}
//        		
//        	}
//        	System.out.println();
//        }
        
        return  boardState.getRandomMove();
    }
}

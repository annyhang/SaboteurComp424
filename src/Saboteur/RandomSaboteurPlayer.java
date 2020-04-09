package Saboteur;

import java.util.Arrays;

import Saboteur.cardClasses.SaboteurTile;
import boardgame.Move;

/**
 * @author mgrenander
 */
public class RandomSaboteurPlayer extends SaboteurPlayer {	
    public RandomSaboteurPlayer() {
        super("RandomPlayer");
    }

    public RandomSaboteurPlayer(String name) {
        super(name);
    }

    @Override
    public Move chooseMove(SaboteurBoardState boardState) {
        System.out.println("random player acting as player number: "+boardState.getTurnPlayer());
        


//        for (int i=0; i<board.length; i++) {
//        	for (int j=0; j<board.length; j++) {
//        		if (board[i][j] < 0) {
//        			System.out.print(board[i][j]);
//        		} else {
//        			System.out.print(board[i][j]+""+board[i][j]);
//        		}
//        		
//        	}
//        	System.out.println();
//        }
        
        return  boardState.getRandomMove();
    }
}

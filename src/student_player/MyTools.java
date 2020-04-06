package student_player;

import java.util.ArrayList;

import boardgame.Move;
import Saboteur.SaboteurBoardState;



public class MyTools {
	
    public static double getSomething() {
        return Math.random();
    }
    
    //TODO
    /**
     * Selection method
     * 
     * @return the most promising move based on existing paths
     */
    public Move selection() {
    	ArrayList<SaboteurMove> allMoves = SaboteurBoardState.getAllLegalMoves();
    	
    	return null;
    }
    
    //TODO
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    
    //TODO
    /**
     * Simulation
     * 
     * @return an utility value from the simulation of the game
     */
    
    //TODO
    /**
     * Propagation
     * 
     * @return the updated tree
     */
    
    
}
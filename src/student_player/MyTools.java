package student_player;

import java.util.ArrayList;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;



public class MyTools {
	
	
    //TODO
    /**
     * MCTS: Selection
     * 
     * @return the most promising move based on existing paths
     */
    public Move selection(int[][] myBoard, ArrayList<SaboteurMove> allLegalMoves) {
    	/*
    	 * selection depends first on the state of the board
    	 * 		if there are two spaces to the nugget (opp can win) we need to prioritise blocking that path, destroy, malus
    	 * 		if we dont know where the nugget is, we need to prioritise the map card unless we can deduce where it is
    	 * 		if we are malused, we could either wait for the opp to build the path to the nugget, malus, bonus, destroy if they are winning
    	 * 		
    	 */
    	
    	
    	return null;
    }
    
    //TODO Anny
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    
    //TODO Massy
    /**
     * Simulation
     * 
     * Utility value: 
     * 
     * 
     * @return an utility value from the simulation of the game
     */
    
    //TODO Massy
    /**
     * Propagation
     * 
     * @return the updated tree
     */
    
    
}
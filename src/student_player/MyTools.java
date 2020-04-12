package student_player;

import java.util.ArrayList;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;




public class MyTools {
	
	
    //TODO
    /**
     * MCTS: Selection
     * 
     * @return the most promising move based on existing paths
     */
    public SaboteurMove selection(int[][] myBoard, int[] nuggetPos, ArrayList<SaboteurMove> allLegalMoves, int nbMyMalus, int nbOppMalus) {    	
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
    
    
    /**
     * Get the distance between the nugget/objectives and the closest path
     */
    public int distanceNuggetPath(int[][] myBoard, int[] nuggetPos) {
    	int smallestDistance = myBoard.length;
    	
    	//if we know where the nugget is, the distance should be between the closest path and the nugget
    	if (nuggetPos[0] != -1 && nuggetPos[1] != -1) {
    		for (int i=myBoard.length; i>0; i--) {
    			for (int j=0; j<myBoard.length; i++) {
    				//we want a 1 (open path) and that tile to have a 1 in the middle (mostly open path)
    				if (myBoard[i][j] == 1 && myBoard[(i/3)*3+1][(j/3)*3+1] == 1) {
    					int height = myBoard.length - i;
    					int length = nuggetPos[1] - j;
    					int distanceNuggPath = (int) Math.pow(Math.pow(height - (nuggetPos[0]*3), 2) + Math.pow(length - (nuggetPos[1]*3), 2), 0.5);
    					if (distanceNuggPath < smallestDistance) {
    						smallestDistance = distanceNuggPath;
    					}
    				}
    				if (smallestDistance > myBoard.length-i || smallestDistance > j) {
    			    	return smallestDistance;
    				}
    			}
    		}
    	} 
    	//if we don't know where the nugget is, the height should be between any of the objectives and its closest path
    	else {
    		for (int i=myBoard.length-6; i>0; i--) {
    			for (int j=0; j<myBoard.length; i++) {
    				if (myBoard[i][j] == 1) {
    					return myBoard.length - i;
    				}
    			}
    		}
    	}
    	return -1;
    }
    
    
    /**
     * Calculate the upper bound of given action and state (UCT)
     */
    private int upperConfidence() {
    	
    }
    
    /**
     * Default policy: 
     * make a random move from a selected optimal set of cards
     */
    
    
    
    
}
 



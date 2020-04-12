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
     * @return the move that makes the path that is closest to the goal even closer
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
     * Get the distance between the nugget/objectives and the closest path.
     * Here, the closest path is assumed to be a feasible path.
     */
    public int distanceNuggetPath(int[][] objectivesPos, int[] nuggetPos, int objectivesFound, int[][] myBoard, SaboteurBoardState boardState) {
    	SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    	int shortestDistance = tileBoard.length;
    	
    	int nuggPosy = nuggetPos[0];
    	int nuggPosx = nuggetPos[1];
    	//if we know where the nugget is (or can deduce it), the distance should be between the closest path and the nugget
    	if ((nuggPosx != -1 && nuggPosy != -1) || objectivesFound == 1) {
    		if (objectivesFound == 1) {
    			nuggPosy = objectivesPos[2][0];
    			nuggPosx = objectivesPos[2][1];
    		}
    		for (int i=tileBoard.length-1; i<=0; i++) {
    			for (int j=0; j<tileBoard.length; j++) {
    				if (tileBoard[i][j] != null) {
    					//if we encouter an objective, we need to check if can add a tile from that objective first
    					if (myBoard[i*3][j*3] > 2) {
    						int[][] path12 = {{0,0,0},{0,1,1},{0,0,0}};
    						int[] posDown = {i+1, j};
    						if (boardState.verifyLegit(path12, posDown)) {
    							int height = tileBoard.length - i;
    							int length = nuggPosx - j;
    							int distanceNuggPath = (int) Math.pow(Math.pow(height, 2) + Math.pow(length, 2), 0.5);
    							if (distanceNuggPath < shortestDistance) {
    	    						shortestDistance = distanceNuggPath;
    	    					}
    						}
    					}
    				}
    			}
    		}
    		return shortestDistance;
    	}
    	//if we don't know where the nugget is, the height of the closest path and the objectives is enough
    	for (int i=tileBoard.length-3; i<=0; i++) {
			for (int j=0; j<tileBoard.length; j++) {
				if (myBoard[i][j] != null) {
					return tileBoard.length;
				}
			}
    	}
    	
    	return -1;
    }
    
    
    
    
    
}

class Node {	// a node represents a board state
	State state;
	ArrayList<Node> parents;	//all used paths from that tile
	ArrayList<Node> childArray;	//all open paths from that tile
	
	boolean isBlockTile = false;
	boolean gotDestroyed = false;
	int[] tileBoardPos = {-1, -1};
	
	String idx;
	int[][] tilePath;
	int[] tilePos;
	
	Node(String idx, int[][] tilePath, int[] tilePos) {
		this.idx = idx;
		this.tilePath = tilePath.clone();
	}
	
	// the parents are the tiles that already existed before this tile and that are connected to this tile
	public void setParents() {
		
	}
	//the children of a node/tile is all the open paths from that tile
	public void setChildren(SaboteurBoardState boardState) {
		//the number of children depends on the number of open paths the tile has.
		
		
	}
}
class Tree {
	Node root;
}
class State {
    SaboteurBoardState board;
    int playerNo;
    int visitCount;
    double winScore;
 
    // copy constructor, getters, and setters
 
    public ArrayList<State> getAllPossibleStates() {
        // constructs a list of all possible states from current state
    }
    public void randomPlay() {
        /* get a list of all possible positions on the board and 
           play a random move */
    }
}
 






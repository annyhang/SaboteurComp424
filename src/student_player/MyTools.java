package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;




public class MyTools {
	
	
    //TODO
    /**
     * MCTS: Selection
     * 
     * @return the board that makes the path that is closest to the goal even closer
     */
    public Node selection(Node rootNode) {    	
        Node node = rootNode;
        while (node.getChildren().size() != 0) {
            node = findBestNodeWithUCT(node);
        }
        return node;
    }
    
    /**
     * UCT to get the best node to expand the tree
     */
    private double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    	if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }
    private Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getState().getVisitCount();
        return Collections.max(
        		node.getChildren(),
        		Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getVisitCount())));
    }
    
    
    //TODO Anny
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    public void expand(Node node) {

    }
    
    
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
	ArrayList<Node> parents = new ArrayList<Node>();	//all used paths from that tile
	ArrayList<Node> children = new ArrayList<Node>();	//all open paths from that tile
	
	boolean isBlockTile = false;
	boolean gotDestroyed = false;
	int[] tileBoardPos = {-1, -1};
	
	String idx;
	int[][] tilePath;
	int[] tilePos;
	int maxNbChildren = 0;
	
	Node(State state, String idx, int[][] tilePath, int[] tilePos) {
		this.state = state;
		this.idx = idx;
		this.tilePath = tilePath.clone();
		this.tilePos = tilePos.clone();
		if (this.tilePath[0][1] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[1][0] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[1][2] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[2][1] == 1) {
			maxNbChildren++;
		}
	}
	
	public void setParents(ArrayList<Node> parents) {
		this.parents = parents;
		maxNbChildren -= parents.size();
		for (Node parent : this.parents) {
			parent.addChild(this);
		}
	}
	public void addChild(Node child) {
		if (maxNbChildren > 0) {
			this.children.add(child);
		}
	}
	
	/*
	 * if the tile got destroyed, we want to keep it in the tree in case putting it back creates a path to the nugget.
	 * we need to remove the child in order to free the open path for another tile.
	 * However, since we still keep it, keep the node connected to the tree
	 */
	public void setDestroyed() {
		this.gotDestroyed = true;
		//since the tile no longer exist, the parents can add another tile/child at that position
		for (Node parent : this.parents) {
			parent.removeChild(this);
		}
	}
	public void removeChild(Node child) {
		this.maxNbChildren--;
	}
	
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	public State getState() {
		return this.state;
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
 






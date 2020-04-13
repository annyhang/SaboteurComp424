package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;


public class MyTools {
	
	static final int WIN_SCORE = 0;
    int level;
    int opponent;
	
	
    //TODO
    /**
     * MCTS: Selection
     * 
     * @return the board that makes the path that is closest to the goal even closer
     */
    public Node selection(Node root) {    	
        Node node = root;
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
        int parentVisit = node.getState().getNodeVisit();
        return Collections.max(
        		node.getChildren(),
        		Comparator.comparing(c -> uctValue(parentVisit, c.getState().getWinScore(), c.getState().getNodeVisit())));
    }
    
    
    //TODO Anny
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    public void expand(Node node) {
    	ArrayList<StudentPlayer> possibleStates = node.getState().getAllPossibleStates();
    	for (StudentPlayer state : possibleStates) {
    		SaboteurTile tile = (SaboteurTile) state.getMyMove().getCardPlayed();
    		Node newNode = new Node(state, tile.getIdx(), state.getMyMove().getPosPlayed());
    		newNode.setParent(node);
    		newNode.getState().switchPlayers();
    		node.addChild(newNode);
    	}
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

    private void backPropogation(Node nodeToExplore, int playerNo) {
    	Node tempNode = nodeToExplore;
    	ArrayList<Node> parents = tempNode.getParents();

    	while (tempNode != null) {
    		for(int i=0; i<=parents.size(); i++) {
    			tempNode.getState().incrementVisit();
    			if (tempNode.getState().getPlayerNo() == playerNo) {
    				tempNode.getState().addScore(WIN_SCORE);
    			}
    			tempNode = parents.get(i);
    		}
    		parents = tempNode.getParents();
    	}
    }
    /*
     * pick a random node and simulate a random play out from it. 
     * Also, we will have an update function to propagate score and visit count starting from leaf to root:
     */
    private int simulateRandomPlayout(Node node) {
    	Node tempNode = new Node(node);
    	StudentPlayer tempState = tempNode.getState();
    	int boardStatus = tempState.getWinner();
    	if (boardStatus == opponent) {
    		for (Node parent : tempNode.getParents()) {
    			parent.getState().setWinScore(Integer.MIN_VALUE);
    		}
    		return boardStatus;
    	}
    	while (boardStatus == -1) {
    		tempState.switchPlayers();
    		tempState.getRandomMove();
    		boardStatus = tempState.checkStatus();
    	}
    	return boardStatus;
    }

    
    public StudentPlayer findNextMove(StudentPlayer board, int playerNo) {
        // define an end time which will act as a terminating condition
 
    	opponent = 3 - playerNo;
    	Tree tree = new Tree(board);
    	Node rootNode = tree.getRoot();

    	rootNode.getState();//.setBoard(board);

    	rootNode.getState().getRandomMove(); //change

    	rootNode.getState().switchPlayers();

    	while (System.currentTimeMillis() <2000) {
    		Node promisingNode = selection(rootNode);
    		if (((StudentPlayer) promisingNode.getState()).checkStatus() == -1) {
    			if (promisingNode.getState().checkStatus() == -1) {
    				expand(promisingNode);
    			}
    			Node nodeToExplore = promisingNode;
    			if (promisingNode.getChildren().size() > 0) {
    				nodeToExplore = promisingNode.getRandomChildNode();
    			}
    			int playoutResult = simulateRandomPlayout(nodeToExplore);
    			backPropogation(nodeToExplore, playoutResult);
    		}

    		Node winnerNode = rootNode.getChildWithMaxScore();
    		tree.setRoot(winnerNode);
    		return winnerNode.getState();
    	}
    	return null;
    }
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
    			if (tileBoard[i][j] != null) {
    				return tileBoard.length;
    			}
    		}
    	}

    	return -1;
    }

    
}



class Node {
	StudentPlayer boardState;
	ArrayList<Node> parents = new ArrayList<Node>();	//all used paths from that tile
	ArrayList<Node> children = new ArrayList<Node>();	//all open paths from that tile
	
	boolean isBlockTile = false;
	boolean gotDestroyed = false;
	
	SaboteurTile tile;
	int[][] tilePath;
	int[] tilePos;
	int maxNbChildren = 0;
	
	Node(StudentPlayer boardState, String idx, int[] tilePos) {
		this.boardState = boardState;
		this.tile = new SaboteurTile(idx);
		this.tilePath = this.tile.getPath();
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
	public Node getChildWithMaxScore() {
		return Collections.max(this.children, Comparator.comparing(c -> {
            return c.getState().getNodeVisit();
        }));
	}
	public Node getRandomChildNode() {
		int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.children.get(selectRandom);
	}
	Node(Node node) {
		this.boardState = node.getState();
		this.tile = node.getTile();
		this.tilePath = this.tile.getPath();
		this.tilePos = node.getTilePos();
	}
	
	

	public void setParent(Node parent) {
		this.parents.add(parent);
		maxNbChildren -= parents.size();
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
	
	public ArrayList<Node> getParents() {
		return this.parents;
	}
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	public StudentPlayer getState() {
		return this.boardState;
	}
	public SaboteurTile getTile() {
		return this.tile;
	}
	public int[] getTilePos() {
		return this.tilePos;
	}
	
	
	
	
}



class Tree {

	Node root;

	Tree(StudentPlayer boardState) {
		int[] entrancePos = {5, 5};
		root = new Node(boardState, "entrance", entrancePos);
	}


	public void setRoot(Node winnerNode) {
		this.root = winnerNode;
		// TODO Auto-generated method stub
		
	}

	public Node getRoot() {
		// TODO Auto-generated method stub
		return root;
	}
	


}



class BoardState {
	SaboteurTile[][] tileBoard;
	int nodeVisit = 0;
	double winScore;
	
	public int getNodeVisit() {
		return this.nodeVisit;
	}
	public double getWinScore() {
		return this.winScore;
	}
}





//class BoardState {
//	SaboteurTile[][] tileBoard;
//	int nodeVisit = 0;
//	double winScore;
//	
//	public int getNodeVisit() {
//		return this.nodeVisit;
//	}
//	public double getWinScore() {
//		return this.winScore;
//	}
//}







 






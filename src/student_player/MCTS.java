//Massiva Mahamli 260806869
//Anny Hang

package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;

public class MCTS {
	
	static final int WIN_SCORE = 0;
    int level;
    int opponent;
    
    

    
    public SaboteurMove findNextMove(StudentPlayer board, int playerNo) {
        // define an end time which will act as a terminating condition
 
    	opponent = 3 - playerNo;
    	Tree tree = new Tree(board);
    	Node rootNode = tree.getRoot();

    	rootNode.getBoardState();//.setBoard(board);

    	rootNode.getBoardState().getRandomMove(); //change


        rootNode.getBoardState().switchPlayers();
 
        while (System.currentTimeMillis() <2000) {
            Node promisingNode = selection(rootNode);

            if (((StudentPlayer) promisingNode.getBoardState().getBoard()).checkStatus() 
              == -1) {

            if (promisingNode.getBoardState().checkStatus() == -1) {

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
        return rootNode.getBoardState().getRandomMove();
         
        }

    	rootNode.getBoardState().switchPlayers();

    	while (System.currentTimeMillis() <2000) {
    		Node promisingNode = selection(rootNode);
    		if (((StudentPlayer) promisingNode.getBoardState()).checkStatus() == -1) {
    			if (promisingNode.getBoardState().checkStatus() == -1) {
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
    		;
    	}
    	 return rootNode.getBoardState().getRandomMove();
    }
    
    /**
     * Select promising node
     * 
     */
    
    
    public Node selection(Node root) {    	
        Node node = root;
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }
    
    /**
     * Expansion
     * @return the tree with an added node at the branch selected
     * 
     */
    
    public void expand(Node node) {
    	ArrayList<StudentBoardState> possibleStates = node.getBoardState().getAllPossibleStates();
    	for (StudentBoardState state : possibleStates) {
    		//getting the move value of the given node from the possible states
    		SaboteurMove move =  state.getMyMove();
    		//create a new node with the correct tile 
    		//TODO : verify if this returns the correct move
    		Node newNode = new Node(state, move);
    		newNode.setParent(node);
    		//TODO: I'm not sure why we're changing the player 
    		newNode.getBoardState().switchPlayers();
    		node.addChild(newNode);
    	}
    }
    /**
     * Simulation
     * pick a random node and simulate a random play out from it. 
     * Also, we will have an update function to propagate score and visit count starting from leaf to root:
     * 
     */
    
    private int simulateRandomPlayout(Node node) {
    	//make a temp node with the current node
    	Node tempNode = new Node(node);
    	StudentBoardState tempState = tempNode.getBoardState();
    	int boardStatus = tempState.getWinner();
    	if (boardStatus == opponent) {
    		for (Node parent : tempNode.getParents()) {
    			parent.getBoardState().setWinScore(Integer.MIN_VALUE);
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

    
    /**
     * Propagation
     * 
     */

    private void backPropogation(Node nodeToExplore, int playerNo) {
    	Node tempNode = nodeToExplore;
    	ArrayList<Node> parents = tempNode.getParents();

    	while (tempNode != null) {
    		for(int i=0; i<=parents.size(); i++) {
    			tempNode.getBoardState().incrementVisit();
    			if (tempNode.getBoardState().getPlayerNo() == playerNo) {
    				tempNode.getBoardState().addScore(WIN_SCORE);
    			}
    			tempNode = parents.get(i);
    		}
    		parents = tempNode.getParents();
    	}
    }

}

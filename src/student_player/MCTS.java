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
    
    /**
     * MCTS: Selection
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
     * iin our case for this particular game
     * wi/ni + c sqrt(ln t)/ni
     * in this case, wi, is the number of wins after the ith move
     * ni is the number of simulations after the ith move
     * c is the exploration parameter (sqrt2)why 
     * t is the total; number of simulations for the parent node
     */
    
    //has been checked and is good, all variables make sense
    private double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    	if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }
    //need to ch
    private Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getBoardState().getNodeVisit();
        return Collections.max(
        		node.getChildren(),
        		Comparator.comparing(c -> uctValue(parentVisit, c.getBoardState().getWinScore(), c.getBoardState().getNodeVisit())));
    }
    
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    public void expand(Node node) {
    	ArrayList<StudentPlayer> possibleStates = node.getBoardState().getAllPossibleStates();
    	for (StudentPlayer state : possibleStates) {
    		SaboteurTile tile = (SaboteurTile) state.getMyMove().getCardPlayed();
    		Node newNode = new Node(state, tile.getIdx(), state.getMyMove().getPosPlayed());
    		newNode.setParent(node);
    		newNode.getState().switchPlayers();
    		node.addChild(newNode);
    	}
    }
    
    
    /**
     * Simulation
     * 
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

    
    public SaboteurMove findNextMove(StudentPlayer board, int playerNo) {
        // define an end time which will act as a terminating condition
 
    	opponent = 3 - playerNo;
    	Tree tree = new Tree(board);
    	Node rootNode = tree.getRoot();

    	rootNode.getState();//.setBoard(board);

    	rootNode.getState().getRandomMove(); //change


        rootNode.getState().switchPlayers();
 
        while (System.currentTimeMillis() <2000) {
            Node promisingNode = selection(rootNode);

            if (((StudentPlayer) promisingNode.getState().getBoard()).checkStatus() 
              == -1) {

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
        return rootNode.getState().getRandomMove();
         
        }

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
    		;
    	}
    	 return rootNode.getState().getRandomMove();
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
    			tempNode.getState().incrementVisit();
    			if (tempNode.getState().getPlayerNo() == playerNo) {
    				tempNode.getState().addScore(WIN_SCORE);
    			}
    			tempNode = parents.get(i);
    		}
    		parents = tempNode.getParents();
    	}
    }

}

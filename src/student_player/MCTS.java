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
    public MCTS() {
        this.level = 3;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }
    
    public SaboteurMove findNextMove(StudentBoardState board, int playerNo) {
        // define an end time which will act as a terminating condition
    	long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel() * 2000;
        
    	opponent = Math.abs(1 - playerNo);
    	Tree tree = new Tree(board);
    	Node rootNode = tree.getRoot();
    	
    	//rootNode.getBoardState();//.setBoard(board);
        rootNode.getBoardState().setPlayerNumber(opponent);

        while (System.currentTimeMillis() < end) {
            Node promisingNode = selection(rootNode);

        	int boardStatus = promisingNode.getBoardState().getBoardStatus();

        	if (boardStatus == -1) {
                expand(promisingNode);
            }


            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
                System.out.println("gotrandomchild" + nodeToExplore);
            }
            System.out.println("about to simulate");
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropogation(nodeToExplore, playoutResult);
        }
 
        Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getBoardState().getRandomMove();
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
     * 
     */
    public void expand(Node node) {

    	
    	ArrayList<StudentBoardState> possibleStates = node.getBoardState().getAllPossibleStates();
    	
    	for (StudentBoardState state : possibleStates) {

    		//getting the move value of the given node from the possible states

    		
    		
    		//getting the move value of the given node from the possible states
    		SaboteurMove move = state.getMyMove();
    		//create a new node with the correct tile 
    		//TODO : verify if this returns the correct move
    		Node newNode = new Node(state,move);
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
    	
    	
    	int boardStatus = tempState.getBoardStatus();

    	System.out.println("This is simulation board status" + boardStatus);

    	if (boardStatus == opponent) {
    			tempNode.getParent().getBoardState().setWinScore(Integer.MIN_VALUE);
    		return boardStatus;
    	}
    	while (boardStatus == -1) {
    		
    		tempState.switchPlayers();
    		tempState.getRandomMove();
    		boardStatus = tempState.getBoardStatus();
    		System.out.println("board status in the loop" + boardStatus);
    	}

    	return boardStatus;
    
    }

    /**
     * Propagation
     * 
     */
    private void backPropogation(Node nodeToExplore, int playerNo) {
    	Node tempNode = nodeToExplore;
    	
    	while (tempNode != null) {
    		
    			tempNode.getBoardState().incrementVisit();
    			if (tempNode.getBoardState().getPlayerNumber() == playerNo) {
    				tempNode.getBoardState().addScore(WIN_SCORE);
    			}
    			tempNode = tempNode.getParent();
    		}
    		
    	}
    }



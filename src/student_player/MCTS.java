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
    	long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel() * 30000;
        
        
        // define an end time which will act as a terminating condition
    	opponent = Math.abs(1 - playerNo);
    	Tree tree = new Tree(board);
    	Node rootNode = tree.getRoot();
    	
    	
    	//rootNode.getBoardState();//.setBoard(board);
        rootNode.getBoardState().setPlayerNumber(opponent);
        
        int count = 0;
        
        while (System.currentTimeMillis() < end) {
        	
        	count++;
            //System.out.println(count);

            Node promisingNode = selection(rootNode);


        	//System.out.println("this is the board status wtf" + promisingNode.getBoardState().getBoardStatus());

            System.out.println("before boardstatus");
        	//System.out.println(promisingNode.getBoardState().getBoardStatus());
        	
            //System.out.println(boardStatus);
            System.out.println("after boardstatus");



            //if (boardStatus == -1) {
                System.out.println("printing the boardStatus:");

                expand(promisingNode);
            //}
            Node nodeToExplore = promisingNode;
            if (promisingNode.getChildren().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
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
        //System.out.println("this is the root" + root);
        while (node.getChildren().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
       System.out.println("this is the best node selected"+node);
        return node;
    }
    
    /**
     * Expansion
     * @return the tree with an added node at the branch selected
     * 
     */
    public void expand(Node node) {
    	System.out.println("we entered expand");
    	ArrayList<SaboteurMove> possibleStates = node.getBoardState().getAllLegalTileMoves();
    	
    	for (SaboteurMove state : possibleStates) {
    		System.out.println("we entered the for loop in expand");
    		//getting the move value of the given node from the possible states
    		
    		//create a new node with the correct tile 
    		//TODO : verify if this returns the correct move
    		Node newNode = new Node(node.getBoardState(),state);
    		newNode.setParent(node);
    		System.out.println("This is the node we picked" + newNode);
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
    	System.out.println("Simulation starting"+ node);
    	Node tempNode = new Node(node);
    	StudentBoardState tempState = tempNode.getBoardState();
    	int boardStatus = tempState.getBoardStatus();
    	if (boardStatus == opponent) {
    			tempNode.getParent().getBoardState().setWinScore(Integer.MIN_VALUE);
    		return boardStatus;
    	}
    	while (boardStatus == -1) {
    		tempState.switchPlayers();
    		tempState.getRandomMove();
    		boardStatus = tempState.getBoardStatus();
    	}
    	System.out.println("board status" + boardStatus);
    	return boardStatus;
    
    }

    /**
     * Propagation
     * 
     */
    private void backPropogation(Node nodeToExplore, int playerNo) {
    	Node tempNode = nodeToExplore;
    	
    	System.out.println("Node to explore"+ nodeToExplore);
    	while (tempNode != null) {
    		
    			tempNode.getBoardState().incrementVisit();
    			if (tempNode.getBoardState().getPlayerNumber() == playerNo) {
    				tempNode.getBoardState().addScore(WIN_SCORE);
    			}
    			tempNode = tempNode.getParent();
    		}
    		
    	}
    }



package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

import java.util.ArrayList;

//added for updating myBoard
import Saboteur.cardClasses.*;


/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {
	
		
	private MyTools myTools;
	
	private int nbRound = 0;
	
	private int[][] myBoard;
	private int myNumber;
	private int oppNumber;
	
	//what gets updated from SaboteurBoardState methods:
	private ArrayList<SaboteurCard> hand;
	private int nbMyMalus;
	private int nbOppMalus;
	private ArrayList<SaboteurMove> allLegalMoves;
	
	private SaboteurMove oppMove;
		

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260803297");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
    	this.nbRound++;
    	
    	//Initialize the board if it is the first round, else update the board and find opponent's move
    	if (this.nbRound == 1) {
    		initialiseBoard(boardState);
    		
    	} else {
    		updateBoard(boardState);
    	}
    	getFromBoard(boardState);
    	

    	//MCTS to get the best move
    	
    	
    	//update the board with our move if necessary
    	

    	return null;
    }
    
    
    private void getFromBoard(SaboteurBoardState boardState) {
    	this.hand = boardState.getCurrentPlayerCards();
    	this.nbMyMalus = boardState.getNbMalus(this.myNumber);
    	this.nbOppMalus = boardState.getNbMalus(this.oppNumber);
    	this.allLegalMoves = boardState.getAllLegalMoves();	
    }
    
    /**
     * Initialise the board.
     * The values on board represent:
     * 		tunnels=1, walls=0, empty_space=-1, hidden_objective=2, nugget=3
     */
    private void initialiseBoard(SaboteurBoardState boardState) {
    	SaboteurTile[][] hiddenBoard = boardState.getHiddenBoard();
    	this.myBoard = boardState.getHiddenIntBoard().clone();
    	
    	//replace empty positions to objectives value
    	for (int i=0; i<hiddenBoard.length; i++) {
    		for (int j=0; j<hiddenBoard.length; j++) {
    			if (hiddenBoard[i][j]!=null) {
	    			if (hiddenBoard[i][j].getIdx().equals("8")) {
		    			for (int h=0; h<3; h++) {
		    				for (int k=0; k<3; k++) {
			    				this.myBoard[i*3+h][j*3+k] = 2;
		    				}
		    			}
	    			}
    			}
    		}
    	}
    	
    	//get players' number
    	this.myNumber = boardState.getTurnPlayer();
    	if (this.myNumber == 0) {
    		this.oppNumber = 1;
    	} else {
    		this.oppNumber = 0;
    	}
    	
    }
    
    /**
     * Update the board and get the opponent's move
     */
    private void updateBoard(SaboteurBoardState boardState) {
    	//check if a malus was used on us
    	if (this.nbMyMalus != boardState.getNbMalus(this.myNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurMalus(), 0, 0, this.oppNumber);
    	} 
    	//check if a bonus was used
    	else if (this.nbOppMalus != boardState.getNbMalus(this.oppNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurBonus(), 0, 0, this.oppNumber);
    	} 
    	//parse through the board to find which tile was added/removed
    	else {
    		int[][] newIntBoard = boardState.getHiddenIntBoard();
    		
    		//we only have to check the middle position of each tile
    		boardloop:
    		for (int i=1; i<newIntBoard.length; i+=3) {
    			for (int j=1; j<newIntBoard.length; j+=3) {
    				if (this.myBoard[i][j] == 2 || this.myBoard[i][j] == 3) {
    					continue;
    				} else if (this.myBoard[i][j] == newIntBoard[i][j]) {
    					continue;
    				}
    				else {
    					//destroyed tile
    					if (this.myBoard[i][j] > newIntBoard[i][j]) {
    						this.oppMove = new SaboteurMove(new SaboteurDestroy(), i/3, j/3, this.oppNumber);
    						break boardloop;
    					} 
    					//added tile
    					else {
    						SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    						String addedTileIdx = tileBoard[i/3][j/3].getIdx();
    						this.oppMove = new SaboteurMove(new SaboteurTile(addedTileIdx), i/3, j/3, this.oppNumber);
    						break boardloop;
    					}
    				}
    			}
    		}
    	}
    	//if we can't find anything, the opponent must've played a map card or dropped a card. Assume they dropped a card
    	this.oppMove = new SaboteurMove(new SaboteurDrop(), 0, 0, this.oppNumber);
    }


}
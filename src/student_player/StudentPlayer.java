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
	public String[] blockTiles = {"1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15"};
	
	private int[][] myBoard;
	private int myNumber;
	private int oppNumber;
	
	//what gets updated from SaboteurBoardState methods:
	private ArrayList<SaboteurCard> hand;
	private int nbMyMalus;
	private int nbOppMalus;
	private ArrayList<SaboteurMove> allLegalMoves;
	private int[] destroyedCard;
	
	//what gets updated after a move
	private SaboteurBoardState boardState;
	private SaboteurMove myMove;
	private SaboteurMove oppMove;
	private int objectivesFound = 0;	//this value indicates which objective to uncover next and not the total number of times we used the map card
	private int[][] objectivesPos = { {12, 3}, {12, 5}, {12, 7} };		//the board is fixed for the game
	private int[] nuggetPos = {-1, -1};									//14x14
	
	//tree for MCTS
	private Tree tree;

    int visitCount;

	private int nodeVisit;
	private int winScore;



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
    	this.boardState = boardState;
    	this.nbRound++;
    	
    	/*
    	 * Initialize the board if it is the first round, else update the board and find opponent's move
    	 */
    	if (this.nbRound == 1) {
    		initialiseBoard(boardState);
    		
    	} else {
    		updateBoard(boardState);
    	}
    	getFromBoard(boardState);
    	

    	/*
    	 * Before using MCTS, we check the state of the player 
    	 */
    	//if we don't know where the nugget is, prioritise the map card
    	if (this.nuggetPos[0] == -1 && this.nuggetPos[1] == -1) {
    		for (SaboteurMove move : this.allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurMap) {
    				this.myMove = new SaboteurMove(new SaboteurMap(), objectivesPos[objectivesFound][0], objectivesPos[objectivesFound][1], this.myNumber);
    				objectivesFound++;
    			}
    		}
    	}
    	//if we got a malus card and we are close from the goal, prioritise a bonus card
    	else if (this.nbMyMalus > 0 && myTools.distanceNuggetPath(objectivesPos, nuggetPos, objectivesFound, myBoard, boardState) < myBoard.length/2) {
    		for (SaboteurMove move : this.allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurBonus) {
    				this.myMove = move;
    			}
    		}
    	}
    	//if we are close from the goal and the opponent can still play, prioritise a malus card
    	else if (nbOppMalus == 0 && myTools.distanceNuggetPath(objectivesPos, nuggetPos, objectivesFound, myBoard, boardState) < myBoard.length/2) {
    		for (SaboteurMove move : this.allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurMalus) {
    				this.myMove = move;
    			}
    		}
    	}
    	//if we got a malus and we only have tile cards, drop a block tile card
    	else if(nbMyMalus > 0) {
    		for (SaboteurCard handCard : this.hand) {
    			if (handCard instanceof SaboteurMap) {
    				this.myMove = new SaboteurMove(new SaboteurDrop(), this.hand.indexOf(handCard), 0, this.myNumber);
    			}
    			if (handCard instanceof SaboteurTile) {
    				SaboteurTile handCardTile = (SaboteurTile) handCard;
    				for (String idxBlockTiles : blockTiles) {
    					if (handCardTile.getIdx().equals(idxBlockTiles)) {
    						this.myMove = new SaboteurMove(new SaboteurDrop(), this.hand.indexOf(handCard), 0, this.myNumber);
    					}
    				}
    			}
    		}
    	}
    	/*
    	 * MCTS to get the best move between the tile cards or destroy cards
    	 */
    	else {
    		
    	}

    	

    	return this.myMove;
    }
    public Move chooseMove(SaboteurMove move) {
    	this.myMove = move;
    	return this.myMove;
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
     * 		tunnels=1, walls=0, empty_space=-1, objective1=2, objective2=3, objective3=4, nugget=5
     */
    private void initialiseBoard(SaboteurBoardState boardState) {
    	SaboteurTile[][] hiddenBoard = boardState.getHiddenBoard();
    	this.myBoard = boardState.getHiddenIntBoard().clone();
    	int objectiveNb = 2;
    	
    	//replace empty positions to objectives value
    	for (int i=0; i<hiddenBoard.length; i++) {
    		for (int j=0; j<hiddenBoard.length; j++) {
    			if (hiddenBoard[i][j]!=null) {
	    			if (hiddenBoard[i][j].getIdx().equals("8")) {
		    			for (int h=0; h<3; h++) {
		    				for (int k=0; k<3; k++) {
			    				this.myBoard[i*3+h][j*3+k] = objectiveNb;
		    				}
		    			}
		    			objectiveNb++;
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
		int[][] newIntBoard = boardState.getHiddenIntBoard();
		
    	//update the board from our previous move if we played map or destroy card
    	SaboteurCard cardPlayed = this.myMove.getCardPlayed();
    	int[] posPlayed = this.myMove.getPosPlayed();
    	if (posPlayed[1] > 0) {
    		if (cardPlayed instanceof SaboteurDestroy) {
    			for (int i=0; i<3; i++) {
    				for (int j=0; j<3; j++) {
    					this.myBoard[i][j] = -1;
    				}
    			}
    		} else if (cardPlayed instanceof SaboteurMap) {
    			SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    			if (tileBoard[posPlayed[0]][posPlayed[1]].getIdx().equals("nugget")) {
    				this.nuggetPos = posPlayed;
    				for (int i=0; i<3; i++) {
        				for (int j=0; j<3; j++) {
        					this.myBoard[i][j] = 5;
        				}
        			}
    			} else {
    				for (int i=0; i<3; i++) {
    					for (int j=0; j<3; i++) {
    						this.myBoard[posPlayed[0]*3+i][posPlayed[1]*3+j] = newIntBoard[posPlayed[0]*3+i][posPlayed[1]*3+j];
    					}
    				}
    			}
    		}
    	}
    	
//we still need to find the nugget to win
//    	//check if we can deduce the nugget. Note: the order of using a map card on the objectives is always the same.
//    	if (nuggetPos[0] == -1 && nuggetPos[1] == -1 && objectivesFound == 1) {
//    		objectivesFound++;
//    		nuggetPos[0] == objectivesPos[objectivesFounds][0];
//    		nuggetPos[1] == objectivesPos[objectivesFounds][1];
//    	}
    	
    	//check if a malus was used on us
    	if (this.nbMyMalus != boardState.getNbMalus(this.myNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurMalus(), 0, 0, this.oppNumber);
    	} 
    	//check if a bonus was used
    	else if (this.nbOppMalus != boardState.getNbMalus(this.oppNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurBonus(), 0, 0, this.oppNumber);
    	} 
    	//parse through the board to find which tile was added/removed, and update it. Also update our move if we played tile card
    	else {    		
    		//we only have to check the middle position of each tile
    		for (int i=1; i<newIntBoard.length; i+=3) {
    			for (int j=1; j<newIntBoard.length; j+=3) {
    				//objectives
    				if (this.myBoard[i][j] == 2 || this.myBoard[i][j] == 3) {
    					continue;
    				} 
    				//no change
    				else if (this.myBoard[i][j] == newIntBoard[i][j]) {
    					continue;
    				} 
    				//our previous move
    				else if ((i/3)==posPlayed[0] && (j/3)==posPlayed[1]) {
    					for (int h=-1; h<2; h++) {
    						for (int k=-1; k<2; k++) {
    							this.myBoard[i+h][j+k] = newIntBoard[i+h][j+k];
    						}
    					}
    				}
    				else {
    					//destroyed tile
    					if (this.myBoard[i][j] > newIntBoard[i][j]) {
    						this.oppMove = new SaboteurMove(new SaboteurDestroy(), i/3, j/3, this.oppNumber);
    					} 
    					//added tile
    					else {
    						SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    						String addedTileIdx = tileBoard[i/3][j/3].getIdx();
    						this.oppMove = new SaboteurMove(new SaboteurTile(addedTileIdx), i/3, j/3, this.oppNumber);
    					}
    				}
    			}
    		}
    	}
    	//if we can't find anything, the opponent must've played a map card or dropped a card. Assume they dropped a card
    	this.oppMove = new SaboteurMove(new SaboteurDrop(), 0, 0, this.oppNumber);
    }
    
    /**
     * These methods are for the implementation of the MCTS
     * 
     */
    public ArrayList<SaboteurTile> getHandOfTiles(){
    	String[] tiles ={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    	ArrayList<SaboteurTile> currentTiles= new ArrayList<SaboteurTile>();
    	for(int i=0; i<=this.hand.size(); i++) {
    		for(int j=0; j<=tiles.length; j++) {
    			if (this.hand.get(i) instanceof SaboteurTile) {
    				SaboteurTile currentCard = (SaboteurTile) this.hand.get(i);
    				if(currentCard.getIdx() == tiles[j]) {
    					currentTiles.add(currentCard);
    				}
    			}
    		}
    	}
    	return currentTiles;
    }
    
//    public ArrayList<SaboteurTile> getAllLegalTileMoves(ArrayList<SaboteurTile> handOfTiles) {
//    	ArrayList<SaboteurTile> currentLegal = new ArrayList<SaboteurTile>();
//    	for(int i=0; i<=handOfTiles.size();i++) {
//    		for(int j=0; j<allLegalMoves.size(); j++) {
//    			SaboteurTile currentTile = handOfTiles.get(i);
//    			ArrayList<int[]> possiblePositions = this.boardState.possiblePositions(currentTile);
//    			if( possiblePositions.size() > 0 ) {
//    				currentLegal.add(currentTile);
//    			}
//    		}
//    	}
//    	return currentLegal;
//    }
    public ArrayList<SaboteurMove> getAllLegalTileMoves() {
    	ArrayList<SaboteurMove> allLegalTileMoves = new ArrayList<SaboteurMove>();
    	for (SaboteurMove move : this.allLegalMoves) {
    		if (move.getCardPlayed() instanceof SaboteurTile) {
    			allLegalTileMoves.add(move);
    		}
    	}
    	return allLegalTileMoves;
    }
    
	public void setWinScore(int winScore) {
		this.winScore = winScore;
	}
	public int getNodeVisit() {
		return this.nodeVisit;
	}
	public double getWinScore() {
		return this.winScore;
	}
	public int incrementVisit() {
		return nodeVisit ++;
	}

	public int getPlayerNo() {
		return myNumber;
	}
	public void addScore(int winScore) {
		this.winScore = winScore;
	}
<<<<<<< HEAD
	public ArrayList<StudentPlayer> getAllPossibleStates() {
		ArrayList<StudentPlayer> allPossibleStates = new ArrayList<StudentPlayer>();
		ArrayList<SaboteurMove> allLegalTileMoves = getAllLegalTileMoves();
		for (SaboteurMove move : allLegalTileMoves) {
			StudentPlayer student = new StudentPlayer();
			student.chooseMove(move);
			allPossibleStates.add(student);
		}
		return allPossibleStates;
	}
	public SaboteurMove getMyMove() {
		return this.myMove;
	}
	public void switchPlayers() {
		int temp = this.oppNumber;
		this.oppNumber = this.myNumber;
		this.myNumber = temp;
=======

	public void addScore(int winScore2) {
		
		winScore = winScore2;
		
		
		
		// TODO Auto-generated method stub
		
>>>>>>> b30b3139b251903ec1936714df094a17103b661c
	}
	

}
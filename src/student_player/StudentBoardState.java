package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurBonus;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDestroy;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurMalus;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.cardClasses.SaboteurTile;
import Saboteur.SaboteurBoardState;

/*
 * This class contains any board state information that is useful for MCTS
 */
public class StudentBoardState {
	
	
	//fixed values
	int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
	public String[] blockTiles = {"1", "2", "2_flip", "3", "3_flip", "4", "4_flip", "11", "11_flip", "12", "12_flip", "13", "14", "14_flip", "15"};
	
	//Board part of this class
	MCTS mcts;
	SaboteurBoardState boardState;
	SaboteurTile[][] tileBoard;
	int[][] intBoard;
	int playerNumber;
	
	int nbMyMalus;
	int nbOppMalus;
	SaboteurMove myMove;
	SaboteurMove oppMove;

	int hiddenUnmappedCount;
	
	//State part of this class
	int nodeVisit = 0;
	double winScore;

	

	StudentBoardState(SaboteurBoardState boardState) {
		this.boardState = boardState;
		this.tileBoard = boardState.getHiddenBoard();
		this.intBoard = boardState.getHiddenIntBoard();
		this.playerNumber = boardState.getTurnPlayer();
		this.nbMyMalus = boardState.getNbMalus(playerNumber);
		int oppNumber = getOpponentNumber();
		this.nbOppMalus = boardState.getNbMalus(oppNumber);
		
	}
	
	StudentBoardState(int[][] intBoard, SaboteurTile[][] tileBoard, SaboteurMove move) {
		this.myMove = move;
		this.playerNumber = move.getPlayerID();
		this.intBoard = intBoard.clone();
		this.tileBoard = tileBoard.clone();
		SaboteurTile tileAdded = (SaboteurTile) move.getCardPlayed();
		int[] tilePos = move.getPosPlayed();
		int[][] tilePath = tileAdded.getPath();

		//add the move to the int board
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				this.intBoard[ tilePos[0]*3 + i][ tilePos[1]*3 + j] = tilePath[j][Math.abs(2-i)];
			}
		}

		//add the move to the tile board
		this.tileBoard[tilePos[0]][tilePos[1]] = tileAdded;		
	}
	
	StudentBoardState(int[][] intBoard, SaboteurTile[][] tileBoard, int playerNumber) {
		this.intBoard = intBoard.clone();
		this.tileBoard = tileBoard.clone();
		this.playerNumber = playerNumber;
	}

	/**
	 * Get a move to play
	 * 
	 * We start with the hardcoded section, which plays the non-tile cards.
	 * Then we play the tile cards based on MCTS.
	 */
	public SaboteurMove chooseMove() {
		int oppNumber = getOpponentNumber();
		int[] nuggetPos = getNugget();
		ArrayList<SaboteurMove> allLegalMoves = this.boardState.getAllLegalMoves();
		int nbMyMalus = this.boardState.getNbMalus(this.playerNumber);
		int nbOppMalus = this.boardState.getNbMalus(oppNumber);
		int distanceToNugg = distanceNuggetPath();

		//if we don't know where the nugget is, prioritise the map card
    	if (nuggetPos[0] == -1 && nuggetPos[1] == -1) {
    		for (SaboteurMove move : allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurMap) {
    				this.myMove = new SaboteurMove(new SaboteurMap(), objPos[hiddenUnmappedCount][0], objPos[hiddenUnmappedCount][1], this.playerNumber);
    				return this.myMove;
    			}
    		}
    	}
    	//if we got a malus card and we are close from the goal, prioritise a bonus card
    	if (nbMyMalus > 0 && distanceToNugg < tileBoard.length/2) {
    		for (SaboteurMove move : allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurBonus) {
    				this.myMove = move;
    				return this.myMove;
    			}
    		}
    	}
    	//if we are close from the goal and the opponent can still play, prioritise a malus card
    	if (nbOppMalus == 0 && distanceToNugg < tileBoard.length/2) {
    		for (SaboteurMove move : allLegalMoves) {
    			if (move.getCardPlayed() instanceof SaboteurMalus) {
    				this.myMove = move;
    				return this.myMove;
    			}
    		}
    	}
//    	//if there are two empty tiles to the nugget from a path, use destroy on that last tile
//    	else if (distanceToNugg == 2) {
//    		
//    	}
    	//if we got a malus and we only have tile cards, drop a block tile card
    	if(nbMyMalus > 0) {
    		ArrayList<SaboteurCard> hand = this.boardState.getCurrentPlayerCards();
    		for (SaboteurCard handCard : hand) {
    			if (handCard instanceof SaboteurMap) {
    				this.myMove = new SaboteurMove(new SaboteurDrop(), hand.indexOf(handCard), 0, this.playerNumber);
    			}
    			if (handCard instanceof SaboteurTile) {
    				SaboteurTile handCardTile = (SaboteurTile) handCard;
    				for (String idxBlockTiles : blockTiles) {
    					if (handCardTile.getIdx().equals(idxBlockTiles)) {
    						this.myMove = new SaboteurMove(new SaboteurDrop(), hand.indexOf(handCard), 0, this.playerNumber);
    						return this.myMove;
    					}
    				}
    			}
    		}
    	}
    	
    	/*
    	 * MCTS to get the best move between the tile cards or destroy cards
    	 */
    	System.out.println("======MCTS======");
		this.myMove = mcts.findNextMove(this,this.playerNumber);
    	return this.myMove;
	}
	
	/**
	 * Get the opponent's move based on what was added to the board
	 */
 	public SaboteurMove getOpponentMove(StudentBoardState previousBS) {
		int oppNumber = getOpponentNumber();
    	//check if a malus was used on us
    	if (nbMyMalus != previousBS.getNbMalus(playerNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurMalus(), 0, 0, oppNumber);
        	return this.oppMove;
    	} 
    	//check if a bonus was used
    	else if (nbOppMalus != previousBS.getNbMalus(oppNumber)) {
    		this.oppMove = new SaboteurMove(new SaboteurBonus(), 0, 0, oppNumber);
        	return this.oppMove;
    	} 
    	//parse through the board to find which tile was added/removed. Ignore if its our move
    	else {    		
    		SaboteurTile[][] previousTileBoard = previousBS.getTileBoard();
    		int[] previousMyMovePos = previousBS.getMyMove().getPosPlayed();
    		for (int i=1; i<tileBoard.length; i++) {
    			for (int j=1; j<tileBoard.length; j++) {
    				//objectives
    				if ( (i==objPos[0][0] && j==objPos[0][1]) || (i==objPos[1][0] && j==objPos[1][1]) || (i==objPos[2][0] && j==objPos[2][1]) ) {
    					continue;
    				} 
    				//no change
    				else if (tileBoard[i][j] == previousTileBoard[i][j]) {
    					continue;
    				} 
    				//our previous move
    				else if ( i==previousMyMovePos[0] && j==previousMyMovePos[1]) {
    					continue;
    				}
    				//destroyed tile
    				else if (previousTileBoard[i][j] != null && this.tileBoard == null) {
    					this.oppMove = new SaboteurMove(new SaboteurDestroy(), i, j, oppNumber);
    					return this.oppMove;
    				} 
    				//added tile
    				else if (previousTileBoard[i][j] == null && this.tileBoard != null){
    					String addedTileIdx = tileBoard[i][j].getIdx();
    					this.oppMove = new SaboteurMove(new SaboteurTile(addedTileIdx), i, j, oppNumber);
    					return this.oppMove;
    				}
    			}
    		}
    	}
    	//if we can't find anything, the opponent must've played a map card or dropped a card. Assume they dropped a card
    	this.oppMove = new SaboteurMove(new SaboteurDrop(), 0, 0, oppNumber);
    	return this.oppMove;
	}
	
	/**
	 * Get the status of the board.
	 * If the position of the nugget is unknown, we assume that all three objectives are the goal.
	 * 
	 * To find if we've won, we parse through all 1's starting from the nugget. 
	 * If there is a path that leads to the entrance, playerNumber has won.
	 * 
	 * @return -1 if no one has won, 0 if player 0 wins, 1 if player 1 wins.
	 */
	public int getBoardStatus() {
		int[] nugget = getNugget();
		int rowNuggetPos = nugget[0];
		int colNuggetPos = nugget[1];
		ArrayList<int[]> prevPos = new ArrayList<int[]>();

		//if we don't know where the nugget is, consider each objectives
		if (rowNuggetPos == -1 && colNuggetPos == -1) {
			int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
			for (int i=0; i<3; i++) {
				int isWinForThisObj = pathStartToEntrance(objPos[i], prevPos);
				if (isWinForThisObj == 2) {
					return this.playerNumber;
				}
			}
		}

		//if the game is still in progress, there should be no 1's around the nugget/objectives
		int isWin = pathStartToEntrance(nugget, prevPos);
		if (isWin == 2) {
			return this.playerNumber;
		}

		return -1;
	}

	/**
	 * Find if there is a path from a given position (there should be a tile) to the entrance.
	 * 
	 * @param the position of the position we are interested in. Based on intBoard
	 * @return 2 if there is a path to the entrance
	 */
	public int pathStartToEntrance(int[] startPos, ArrayList<int[]> prevPos) {
		int startValue = 0;
		
		if (tileBoard[ startPos[0]/3 ][ startPos[1]/3 ].getIdx().equals("Entrance")) {
			return 2;
		}
		else if (intBoard[ startPos[0] ][ startPos[1] ] == 1) {
			int[] nextPos = new int[2];
			//left
			try {
				nextPos[0] = startPos[0];
				nextPos[1] = startPos[1]-1;
				if (intBoard[ nextPos[0] ][ nextPos[1] ] == 1) {
					for (int[] onePrevPos : prevPos) {
						if (nextPos.equals(onePrevPos)) {
							throw new Exception();
						}
					}
					ArrayList<int[]> newPrevPos = new ArrayList<int[]>();
					for (int[] oldPrevPos : prevPos) {
						newPrevPos.add(oldPrevPos);
					}
					newPrevPos.add(startPos);
					int nextValue = pathStartToEntrance(nextPos, newPrevPos);
					if (nextValue == 2) {
						return 2;
					}
				}
			} catch (Exception e) {}
			//right
			try {
				nextPos[0] = startPos[0];
				nextPos[1] = startPos[1]+1;
				if (intBoard[ nextPos[0] ][ nextPos[1] ] == 1) {
					for (int[] onePrevPos : prevPos) {
						if (nextPos.equals(onePrevPos)) {
							throw new Exception();
						}
					}
					ArrayList<int[]> newPrevPos = new ArrayList<int[]>();
					for (int[] oldPrevPos : prevPos) {
						newPrevPos.add(oldPrevPos);
					}
					newPrevPos.add(startPos);
					int nextValue = pathStartToEntrance(nextPos, newPrevPos);
					if (nextValue == 2) {
						return 2;
					}
				}
			} catch (Exception e) {}
			//down
			try {
				nextPos[0] = startPos[0]-1;
				nextPos[1] = startPos[1];
				if (intBoard[ nextPos[0] ][ nextPos[1] ] == 1) {
					for (int[] onePrevPos : prevPos) {
						if (nextPos.equals(onePrevPos)) {
							throw new Exception();
						}
					}
					ArrayList<int[]> newPrevPos = new ArrayList<int[]>();
					for (int[] oldPrevPos : prevPos) {
						newPrevPos.add(oldPrevPos);
					}
					newPrevPos.add(startPos);
					int nextValue = pathStartToEntrance(nextPos, newPrevPos);
					if (nextValue == 2) {
						return 2;
					}
				}
			} catch (Exception e) {}
			//up
			try {
				nextPos[0] = startPos[0]+1;
				nextPos[1] = startPos[1];
				if (intBoard[ nextPos[0] ][ nextPos[1] ] == 1) {
					for (int[] onePrevPos : prevPos) {
						if (nextPos.equals(onePrevPos)) {
							throw new Exception();
						}
					}
					ArrayList<int[]> newPrevPos = new ArrayList<int[]>();
					for (int[] oldPrevPos : prevPos) {
						newPrevPos.add(oldPrevPos);
					}
					newPrevPos.add(startPos);
					int nextValue = pathStartToEntrance(nextPos, newPrevPos);
					if (nextValue == 2) {
						return 2;
					}
				}
			} catch (Exception e) {}

		}
		
		return startValue;
	}
	
    /**
     * Get the distance between the nugget/objectives and the closest path.
     * Here, the closest path is assumed to be a feasible path.
     */
    public int distanceNuggetPath() {
    	int shortestDistance = tileBoard.length*2;
    	
    	int[] nuggetPos = getNugget();
    	int nuggPosy = nuggetPos[0];
    	int nuggPosx = nuggetPos[1];
    	//if we know where the nugget is, the distance should be between the closest path and the nugget
    	if ((nuggPosx != -1 && nuggPosy != -1)) {
    		for (int i=tileBoard.length-1; i<=0; i++) {
    			for (int j=0; j<tileBoard.length; j++) {
    				if (tileBoard[i][j] != null) {
    					//if we encounter an objective, we need to check if can add a tile from that objective first
    					if ( (i==objPos[0][0] && j==objPos[0][1]) || (i==objPos[1][0] && j==objPos[1][1]) || (i==objPos[2][0] && j==objPos[2][1]) ) {
    						int[][] path12 = {{0,0,0},{0,1,1},{0,0,0}};
    						int[] posDown = {i+1, j};
    						if (boardState.verifyLegit(path12, posDown)) {
    							int height = tileBoard.length - i;
    							int length = nuggPosx - j;
    							int distanceNuggPath = height + length;
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
	
	/**
	 * Get the position of the nugget.
	 * 
	 * @return the position of the nugget. If it is unknown, it can either be deduced or return {-1, -1}
	 */
    public int[] getNugget() {
		boolean[] hiddenUnmapped = {false, false, false};
		this.hiddenUnmappedCount = 0;
		int[] nuggetPos = {-1, -1};

		for (int i=0; i<3; i++) {
			String idx = this.tileBoard[ objPos[i][0] ][ objPos[i][1] ].getIdx();
			if (idx.equals("nugget")) {
				return objPos[0];
			} 
			else if (idx.equals("hidden2") || idx.equals("hidden2")) {
				hiddenUnmapped[i] = true;
				hiddenUnmappedCount++;

			}
		}
		
		//nugget hasn't been unmapped but we can deduce where it is
		if (hiddenUnmappedCount == 2) {
			for (int i=0; i<hiddenUnmapped.length; i++) {
				if (hiddenUnmapped[i] == false) {
					return objPos[i];
				}
			}
		}
		return nuggetPos;
	}
	
    public ArrayList<SaboteurTile> getHandOfTiles(){
    	String[] tiles ={"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
    	ArrayList<SaboteurCard> hand = this.boardState.getCurrentPlayerCards();
    	ArrayList<SaboteurTile> currentTiles= new ArrayList<SaboteurTile>();
    	for(int i=0; i<=hand.size(); i++) {
    		for(int j=0; j<=tiles.length; j++) {
    			if (hand.get(i) instanceof SaboteurTile) {
    				SaboteurTile currentCard = (SaboteurTile) hand.get(i);
    				if(currentCard.getIdx() == tiles[j]) {
    					currentTiles.add(currentCard);
    				}
    			}
    		}
    	}
    	return currentTiles;
    }

    public ArrayList<SaboteurMove> getAllLegalTileMoves() {
    	ArrayList<SaboteurMove> allLegalMoves = this.boardState.getAllLegalMoves();
    	ArrayList<SaboteurMove> allLegalTileMoves = new ArrayList<SaboteurMove>();
    	for (SaboteurMove move : allLegalMoves) {
    		if (move.getCardPlayed() instanceof SaboteurTile) {
    			allLegalTileMoves.add(move);
    		}
    	}
    	return allLegalTileMoves;
    }
	
	public void addVisit() {
		this.nodeVisit++;
	}
	public void setWinScore(int winScore) {
		this.winScore = winScore;
	}
	public SaboteurTile[][] getTileBoard() {
		return this.tileBoard;
	}
	public int[][] getIntBoard() {
		return this.intBoard;
	}
	public SaboteurMove getMyMove() {
		return this.myMove;
	}
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	
	public void setPlayerNumber(int playerNo) {
		this.playerNumber = playerNo;
	}
	public int getOpponentNumber() {
		int oppNumber = Math.abs(this.playerNumber - 1);
		return oppNumber;
	}
	public int getNbMalus(int playerNb) {
		if (playerNb == this.playerNumber) {
			return this.nbMyMalus;
		}
		else {
			return this.nbOppMalus;
		}
	}
	public int getNodeVisit() {
		return this.nodeVisit;
	}
	public double getWinScore() {
		return this.winScore;
	}

	public ArrayList<StudentBoardState> getAllPossibleStates() {
		ArrayList<StudentBoardState> allPossibleStates = new ArrayList<StudentBoardState>();
		ArrayList<SaboteurMove> allLegalTileMoves = getAllLegalTileMoves();
		
		//for everyone of the legal moves create a new state and add it to an array list of possible states
		for (SaboteurMove move : allLegalTileMoves) {
			StudentBoardState student = new StudentBoardState(this.intBoard, this.tileBoard, move);
			allPossibleStates.add(student);
		}
		return allPossibleStates;
	}
	
	
	
	
	public void switchPlayers() {
		this.playerNumber = Math.abs(this.playerNumber - 1);
	}
	
	public SaboteurMove getRandomMove() {
		return this.boardState.getRandomMove();
	}

	public int[][] getInitIntBoard() {
		int[][] initIntBoard = new int[14*3][14*3];
		for (int i = 0; i < 14*3; i++) {
			for (int j = 0; j < 14*3; j++) {
				initIntBoard[i][j] = -1;
			}
		}
		int[][] entrancePath = {{0,1,0},{1,1,1},{0,1,0}};
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				initIntBoard[ 5*3 + i][ 5*3 + j] = entrancePath[j][Math.abs(2-i)];
			}
		}
		return initIntBoard;
	}
	
	public SaboteurTile[][] getInitTileBoard() {
		SaboteurTile[][] initTileBoard = new SaboteurTile[14][14];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 14; j++) {
            	initTileBoard[i][j] = null;
            }
        }
        initTileBoard[5][5] = new SaboteurTile("entrance");
        for (int i=0; i<3; i++) {
        	initTileBoard[objPos[i][0]][objPos[i][1]] = new SaboteurTile("8");
        }
        return initTileBoard;

	}

	public void incrementVisit() {
		
		this.nodeVisit++;
		// TODO Auto-generated method stub
		
	}

	public void addScore(int winScore2) {
		
		if(this.winScore != Integer.MIN_VALUE)
			this.winScore += winScore2;
		// TODO Auto-generated method stub
		
	}

	
}

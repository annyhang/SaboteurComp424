package student_player;

<<<<<<< Updated upstream
import Saboteur.cardClasses.SaboteurTile;
import Saboteur.SaboteurMove;
import Saboteur.SaboteurBoardState;

public class StudentBoardState {

	
	//SaboteurBoardState boardState;
	SaboteurTile[][] tileBoard;
	int[][] intBoard;
	int playerNumber;
	
	int nodeVisit = 0;
	double winScore;
	
	
	StudentBoardState(int[][] intBoard, SaboteurTile[][] tileBoard, SaboteurMove move, int playerNumber) {
		this.playerNumber = playerNumber;
		this.intBoard = intBoard.clone();
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
		this.tileBoard = tileBoard.clone();
		this.tileBoard[tilePos[0]][tilePos[1]] = tileAdded;		
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
		
		//if we don't know where the nugget is, consider each objectives
		if (rowNuggetPos == -1 && colNuggetPos == -1) {
			int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
			threeObjectivesLoop:
				for (int h=0; h<3; h++) {
//					for (int i=0; i<3; i++) {
//						for (int j=0; j<3; j++) {
//							if (intBoar)
//						}
//					}
				}
		}
		
		//if the game is still in progress, there should be no 1's around the nugget/objectives
		
		
		return -1;
	}
	
	/**
	 * Find if there is a path from a given position (there should be a tile) to the entrance
	 * 
	 * @return true if there is a path
	 */
	public boolean pathToEntrance(int[] startingPos) {
		//we need to parse up down left and right for each int until we end up in the entrance tile or there are no other 1's
		
	}
	
	/**
	 * Get the position of the nugget.
	 * 
	 * @return the position of the nugget. If it is unknown, it can either be deduced or return {-1, -1}
	 */
	public int[] getNugget() {
		int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
		boolean[] hiddenUnmapped = {false, false, false};
		int hiddenUnmappedCount = 0;
		int[] nuggetPos = {-1, -1};

		for (int i=0; i<3; i++) {
			String idx = this.tileBoard[ objPos[i][0] * 3 ][ objPos[i][1] * 3 ].getIdx();
			if (idx.equals("nugget")) {
				nuggetPos = objPos[0];
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
					nuggetPos = objPos[i];
					break;
				}
			}
		}
		return nuggetPos;
	}
	
	
	public int getNodeVisit() {
		return this.nodeVisit;
	}
	public double getWinScore() {
		return this.winScore;
	}
	
=======
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;

public class StudentBoardState {
	//SaboteurBoardState boardState;
		SaboteurTile[][] tileBoard;
		int[][] intBoard;
		int playerNumber;
		
		int nodeVisit = 0;
		double winScore;
		
		
		StudentBoardState(int[][] intBoard, SaboteurTile[][] tileBoard, SaboteurMove move, int playerNumber) {
			this.playerNumber = playerNumber;
			this.intBoard = intBoard.clone();
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
			this.tileBoard = tileBoard.clone();
			this.tileBoard[tilePos[0]][tilePos[1]] = tileAdded;		
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
			
			//if we don't know where the nugget is, consider each objectives
			if (rowNuggetPos == -1 && colNuggetPos == -1) {
				int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
				threeObjectivesLoop:
					for (int h=0; h<3; h++) {
//						for (int i=0; i<3; i++) {
//							for (int j=0; j<3; j++) {
//								if (intBoar)
//							}
//						}
					}
			}
			
			//if the game is still in progress, there should be no 1's around the nugget/objectives
			
			
			return -1;
		}
		
		/**
		 * Find if there is a path from a given position (there should be a tile) to the entrance
		 * 
		 * @return true if there is a path
		 */
		public boolean pathToEntrance(int[] startingPos) {
			//we need to parse up down left and right for each int until we end up in the entrance tile or there are no other 1's
			
		}
		
		/**
		 * Get the position of the nugget.
		 * 
		 * @return the position of the nugget. If it is unknown, it can either be deduced or return {-1, -1}
		 */
		public int[] getNugget() {
			int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
			boolean[] hiddenUnmapped = {false, false, false};
			int hiddenUnmappedCount = 0;
			int[] nuggetPos = {-1, -1};

			for (int i=0; i<3; i++) {
				String idx = this.tileBoard[ objPos[i][0] * 3 ][ objPos[i][1] * 3 ].getIdx();
				if (idx.equals("nugget")) {
					nuggetPos = objPos[0];
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
						nuggetPos = objPos[i];
						break;
					}
				}
			}
			return nuggetPos;
		}
		
		
		public int getNodeVisit() {
			return this.nodeVisit;
		}
		public double getWinScore() {
			return this.winScore;
		}
>>>>>>> Stashed changes
}

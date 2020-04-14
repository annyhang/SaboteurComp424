package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;


public class MyTools {
	
	SaboteurBoardState boardState;
	int[][] objPos = { {12, 3}, {12, 5}, {12, 7} };
	
	
	MyTools(SaboteurBoardState boardState) {
		this.boardState = boardState;
	}

	
	
	
	
	/**
	 * Get the position of the nugget.
	 * 
	 * @return the position of the nugget. If it is unknown, it can either be deduced or return {-1, -1}
	 */
	public int[] getNugget() {
		boolean[] hiddenUnmapped = {false, false, false};
		int hiddenUnmappedCount = 0;
		int[] nuggetPos = {-1, -1};

		for (int i=0; i<3; i++) {
			SaboteurTile[][] tileBoard = this.boardState.getHiddenBoard();
			String idx = tileBoard[ objPos[i][0] * 3 ][ objPos[i][1] * 3 ].getIdx();
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

    /**
     * Get the distance between the nugget/objectives and the closest path.
     * Here, the closest path is assumed to be a feasible path.
     */
    public int distanceNuggetPath(int[] nuggetPos, int objectivesFound, int[][] myBoard, SaboteurBoardState boardState) {
    	SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    	int shortestDistance = tileBoard.length;
    	
    	int nuggPosy = nuggetPos[0];
    	int nuggPosx = nuggetPos[1];
    	//if we know where the nugget is (or can deduce it), the distance should be between the closest path and the nugget
    	if ((nuggPosx != -1 && nuggPosy != -1) || objectivesFound == 1) {
    		if (objectivesFound == 1) {
    			nuggPosy = objPos[2][0];
    			nuggPosx = objPos[2][1];
    		}
    		for (int i=tileBoard.length-1; i<=0; i++) {
    			for (int j=0; j<tileBoard.length; j++) {
    				if (tileBoard[i][j] != null) {
    					//if we encounter an objective, we need to check if can add a tile from that objective first
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
	
	
	 SaboteurBoardState getBoardState() {
		return this.boardState;
	}
    
}

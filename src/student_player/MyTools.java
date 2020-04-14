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


	
	
	 SaboteurBoardState getBoardState() {
		return this.boardState;
	}
    
}

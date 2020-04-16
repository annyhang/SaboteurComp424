package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;
import Saboteur.SaboteurBoardState;

/*
 * A node represents a board state given a tile move. 
 * 
 * If the tile got destroyed, we want to keep it in the tree in case putting it back creates a path to the nugget.
 * Destroyed tiles are marked destroyed (gotDestroyed).
 * If it is from path from entrance to nugget, or it is the most optimal path to get there, we need to be able to put it back.
 * 
 */
public class Node {
	
	StudentBoardState boardState;
	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();	//all open paths from that tile

	boolean isBlockTile = false;
	boolean gotDestroyed = false;
	

	Node(StudentBoardState boardState, SaboteurMove move) {
		int[][] intBoard = boardState.getIntBoard().clone();
		SaboteurTile[][] tileBoard = boardState.getTileBoard().clone();
		this.boardState = new StudentBoardState(intBoard, tileBoard, move);
	}
	
	Node(Node node) {
		this.boardState = node.getBoardState();
	}
	
	Node(StudentBoardState studentBS) {
//		int[][] initIntBoard = studentBS.getInitIntBoard();
//		SaboteurTile[][] initTileBoard = studentBS.getInitTileBoard();
//		int playerNumber = studentBS.getPlayerNumber();
		//this.boardState = new StudentBoardState(initIntBoard, initTileBoard, playerNumber);
		this.boardState = studentBS;
	}
	
	
	public Node getChildWithMaxScore() {
		return Collections.max(this.children, Comparator.comparing(c -> {
			return c.getBoardState().getNodeVisit();
		}));
	}
	public Node getRandomChildNode() {
		int noOfPossibleMoves = this.children.size();
		int selectRandom = (int) (Math.random() * noOfPossibleMoves);
		return this.children.get(selectRandom);
	}

	
	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void addChild(Node child) {
			this.children.add(child);
	}
	public void setDestroyed() {
		this.gotDestroyed = true;
	}

	
	public Node getParent() {
		return this.parent;
	}
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	public StudentBoardState getBoardState() {
		return this.boardState;
	}
	public boolean getDestroyed() {
		return this.gotDestroyed;
	}


}

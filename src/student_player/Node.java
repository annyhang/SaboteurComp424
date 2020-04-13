package student_player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Saboteur.cardClasses.SaboteurTile;

public class Node {
	

	StudentPlayer boardState;
	ArrayList<Node> parents = new ArrayList<Node>();	//all used paths from that tile
	ArrayList<Node> children = new ArrayList<Node>();	//all open paths from that tile

	boolean isBlockTile = false;
	boolean gotDestroyed = false;

	SaboteurTile tile;
	int[][] tilePath;
	int[] tilePos;
	int maxNbChildren = 0;
	

	Node(StudentBardState boardState, String idx, int[] tilePos) {
		this.boardState = boardState;
		this.tile = new SaboteurTile(idx);
		this.tilePath = this.tile.getPath();
		this.tilePos = tilePos.clone();
		if (this.tilePath[0][1] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[1][0] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[1][2] == 1) {
			maxNbChildren++;
		}
		if (this.tilePath[2][1] == 1) {
			maxNbChildren++;
		}
	}
	
	Node(Node node) {
		this.boardState = node.getState();
		this.tile = node.getTile();
		this.tilePath = this.tile.getPath();
		this.tilePos = node.getTilePos();
	}
	
	
	public Node getChildWithMaxScore() {
		return Collections.max(this.children, Comparator.comparing(c -> {
			return c.getState().getNodeVisit();
		}));
	}
	public Node getRandomChildNode() {
		int noOfPossibleMoves = this.children.size();
		int selectRandom = (int) (Math.random() * noOfPossibleMoves);
		return this.children.get(selectRandom);
	}




	public void setParent(Node parent) {
		this.parents.add(parent);
		maxNbChildren -= parents.size();
	}

	public void addChild(Node child) {
		if (maxNbChildren > 0) {
			this.children.add(child);
		}
	}

	/*
	 * if the tile got destroyed, we want to keep it in the tree in case putting it back creates a path to the nugget.
	 * we need to remove the child in order to free the open path for another tile.
	 * However, since we still keep it, keep the node connected to the tree
	 */
	public void setDestroyed() {
		this.gotDestroyed = true;
		//since the tile no longer exist, the parents can add another tile/child at that position
		for (Node parent : this.parents) {
			parent.removeChild(this);
		}
	}
	public void removeChild(Node child) {
		this.maxNbChildren--;
	}

	public ArrayList<Node> getParents() {
		return this.parents;
	}
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	public StudentPlayer getState() {
		return this.boardState;
	}
	public SaboteurTile getTile() {
		return this.tile;
	}
	public int[] getTilePos() {
		return this.tilePos;
	}


}

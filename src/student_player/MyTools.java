package student_player;

import java.util.ArrayList;

import boardgame.Move;
import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.*;




public class MyTools {
	
	
    //TODO
    /**
     * MCTS: Selection
     * 
     * @return the most promising move based on existing paths
     */
    public SaboteurMove selection(int[][] myBoard, int[] nuggetPos, ArrayList<SaboteurMove> allLegalMoves, int nbMyMalus, int nbOppMalus) {    	
    	//if we don't know where the nugget is, prioritise the map card
    	if (nuggetPos[0] == -1 && nuggetPos[1] == -1) {
    		for (int i=0; i<allLegalMoves.size(); i++) {
    			if (allLegalMoves.get(i).getCardPlayed() instanceof SaboteurMap) {
    				return allLegalMoves.get(i);
    			}
    		}
    	}
    	//if we got a malus card and we are close from the goal, prioritise a bonus card
    	else if (nbMyMalus > 0 && distanceNuggetPath(myBoard, nuggetPos) < myBoard.length/2) {
    		for (int i=0; i<allLegalMoves.size(); i++) {
    			if (allLegalMoves.get(i).getCardPlayed() instanceof SaboteurBonus) {
    				return allLegalMoves.get(i);
    			}
    		}
    	}
    	//if we are close from the goal and the opponent can still play, prioritise a malus card
    	else if (nbOppMalus == 0 && distanceNuggetPath(myBoard, nuggetPos) < myBoard.length/2) {
    		for (int i=0; i<allLegalMoves.size(); i++) {
    			if (allLegalMoves.get(i).getCardPlayed() instanceof SaboteurMalus) {
    				return allLegalMoves.get(i);
    			}
    		}
    	}
    	//chose a tile with that creates a path
    	else {
    		for (int i=0; i<allLegalMoves.size(); i++) {
    			if (allLegalMoves.get(i).getCardPlayed() instanceof SaboteurTile) {
    				return allLegalMoves.get(i);
    			}
    		}
    	}
    	return null;
    }
    
    //TODO Anny
    /**
     * Expansion
     * 
     * @return the tree with an added node at the branch selected
     */
    
    //TODO Massy
    /**
     * Simulation
     * 
     * Utility value: 
     * 
     * 
     * @return an utility value from the simulation of the game
     */
    public BinarySearchTree createTree (Node root) {
    BinarySearchTree b = new BinarySearchTree();
	b.insert(3);b.insert(8);
	b.insert(1);b.insert(4);b.insert(6);b.insert(2);b.insert(10);b.insert(9);
	b.insert(20);b.insert(25);b.insert(15);b.insert(16);
	System.out.println("Original Tree : ");
	b.display(b.root);		
	System.out.println("");
	System.out.println("Check whether Node with value 4 exists : " + b.find(4));
	System.out.println("Delete Node with no children (2) : " + b.delete(2));		
	b.display(root);
	System.out.println("\n Delete Node with one child (4) : " + b.delete(4));		
	b.display(root);
	System.out.println("\n Delete Node with Two children (10) : " + b.delete(10));		
	b.display(root);
	return b;
    }
    //TODO Massy
    /**
     * Propagation
     * 
     * @return the updated tree
     */
    
    /**
     * Get the distance between the nugget/objectives and the closest path
     */
    private int distanceNuggetPath(int[][] myBoard, int[] nuggetPos) {
    	int smallestDistance = myBoard.length;
    	
    	//if we know where the nugget is, the distance should be between the closest path and the nugget
    	if (nuggetPos[0] != -1 && nuggetPos[1] != -1) {
    		for (int i=myBoard.length; i>0; i--) {
    			for (int j=0; j<myBoard.length; i++) {
    				//we want a 1 (open path) and that tile to have a 1 in the middle (mostly open path)
    				if (myBoard[i][j] == 1 && myBoard[(i/3)*3+1][(j/3)*3+1] == 1) {
    					int height = myBoard.length - i;
    					int length = nuggetPos[1] - j;
    					int distanceNuggPath = (int) Math.pow(Math.pow(height - (nuggetPos[0]*3), 2) + Math.pow(length - (nuggetPos[1]*3), 2), 0.5);
    					if (distanceNuggPath < smallestDistance) {
    						smallestDistance = distanceNuggPath;
    					}
    				}
    				if (smallestDistance > myBoard.length-i || smallestDistance > j) {
    			    	return smallestDistance;
    				}
    			}
    		}
    	} 
    	//if we don't know where the nugget is, the height should be between any of the objectives and its closest path
    	else {
    		for (int i=myBoard.length-6; i>0; i--) {
    			for (int j=0; j<myBoard.length; i++) {
    				if (myBoard[i][j] == 1) {
    					return myBoard.length - i;
    				}
    			}
    		}
    	}
    	return -1;
    }
    
    
}

class Node{
	int data;
	Node left;
	Node right;	
	public Node(int data){
		this.data = data;
		left = null;
		right = null;
	}
}

class BinarySearchTree {
	public static  Node root;
	public BinarySearchTree(){
		this.root = null;
	}
	
	public boolean find(int id){
		Node current = root;
		while(current!=null){
			if(current.data==id){
				return true;
			}else if(current.data>id){
				current = current.left;
			}else{
				current = current.right;
			}
		}
		return false;
	}
	public boolean delete(int id){
		Node parent = root;
		Node current = root;
		boolean isLeftChild = false;
		while(current.data!=id){
			parent = current;
			if(current.data>id){
				isLeftChild = true;
				current = current.left;
			}else{
				isLeftChild = false;
				current = current.right;
			}
			if(current ==null){
				return false;
			}
		}
		//if i am here that means we have found the node
		//Case 1: if node to be deleted has no children
		if(current.left==null && current.right==null){
			if(current==root){
				root = null;
			}
			if(isLeftChild ==true){
				parent.left = null;
			}else{
				parent.right = null;
			}
		}
		//Case 2 : if node to be deleted has only one child
		else if(current.right==null){
			if(current==root){
				root = current.left;
			}else if(isLeftChild){
				parent.left = current.left;
			}else{
				parent.right = current.left;
			}
		}
		else if(current.left==null){
			if(current==root){
				root = current.right;
			}else if(isLeftChild){
				parent.left = current.right;
			}else{
				parent.right = current.right;
			}
		}else if(current.left!=null && current.right!=null){
			
			//now we have found the minimum element in the right sub tree
			Node successor	 = getSuccessor(current);
			if(current==root){
				root = successor;
			}else if(isLeftChild){
				parent.left = successor;
			}else{
				parent.right = successor;
			}			
			successor.left = current.left;
		}		
		return true;		
	}
	
	public Node getSuccessor(Node deleleNode){
		Node successsor =null;
		Node successsorParent =null;
		Node current = deleleNode.right;
		while(current!=null){
			successsorParent = successsor;
			successsor = current;
			current = current.left;
		}
		//check if successor has the right child, it cannot have left child for sure
		// if it does have the right child, add it to the left of successorParent.
//		successsorParent
		if(successsor!=deleleNode.right){
			successsorParent.left = successsor.right;
			successsor.right = deleleNode.right;
		}
		return successsor;
	}
	public void insert(int id){
		Node newNode = new Node(id);
		if(root==null){
			root = newNode;
			return;
		}
		Node current = root;
		Node parent = null;
		while(true){
			parent = current;
			if(id<current.data){				
				current = current.left;
				if(current==null){
					parent.left = newNode;
					return;
				}
			}else{
				current = current.right;
				if(current==null){
					parent.right = newNode;
					return;
				}
			}
		}
	}
	public void display(Node root){
		if(root!=null){
			display(root.left);
			System.out.print(" " + root.data);
			display(root.right);
		}
	}
	public static void main(String arg[]){
		
	}
}


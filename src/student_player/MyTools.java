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
     * @return the move that makes the path that is closest to the goal even closer
     */
    public SaboteurMove selection(int[][] myBoard, int[] nuggetPos, ArrayList<SaboteurMove> allLegalMoves, int nbMyMalus, int nbOppMalus) {    	
    	
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
     * Get the distance between the nugget/objectives and the closest path.
     * Here, the closest path is assumed to be a feasible path.
     */
    public int distanceNuggetPath(int[][] objectivesPos, int[] nuggetPos, int objectivesFound, int[][] myBoard, SaboteurBoardState boardState) {
    	SaboteurTile[][] tileBoard = boardState.getHiddenBoard();
    	int shortestDistance = tileBoard.length;
    	
    	int nuggPosy = nuggetPos[0];
    	int nuggPosx = nuggetPos[1];
    	//if we know where the nugget is (or can deduce it), the distance should be between the closest path and the nugget
    	if ((nuggPosx != -1 && nuggPosy != -1) || objectivesFound == 1) {
    		if (objectivesFound == 1) {
    			nuggPosy = objectivesPos[2][0];
    			nuggPosx = objectivesPos[2][1];
    		}
    		for (int i=tileBoard.length-1; i<=0; i++) {
    			for (int j=0; j<tileBoard.length; j++) {
    				if (tileBoard[i][j] != null) {
    					//if we encouter an objective, we need to check if can add a tile from that objective first
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
				if (myBoard[i][j] != null) {
					return tileBoard.length;
				}
			}
    	}
    	
    	return -1;
    }
    
    
    /**
     * Calculate the upper bound of given action and state (UCT)
     */
    private int upperConfidence() {
    	
    }
    
    /**
     * Default policy: 
     * make a random move from a selected optimal set of cards
     */
    private SaboteurMove defaultPolicy() {
    	
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


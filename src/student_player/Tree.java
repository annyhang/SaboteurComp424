package student_player;

public class Tree {

	Node root;

	Tree(StudentPlayer boardState) {
		int[] entrancePos = {5, 5};
		root = new Node(boardState, "entrance", entrancePos);
	}


	public void setRoot(Node winnerNode) {
		this.root = winnerNode;
	}

	public Node getRoot() {
		return root;
	}
	
}

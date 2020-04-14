package student_player;

public class Tree {

    Node root;	//  The root is the entrance

    public Tree(StudentPlayer studentPlayer) {
        this.root = new Node(studentPlayer);
    }

    public Node getRoot() {
        return this.root;
    }

    public void addChild(Node parent, Node child) {
        parent.getChildren().add(child);
    }
	
	
}

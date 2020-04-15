package student_player;

public class Tree {

    Node root;	//  The root is the entrance

    public Tree(StudentBoardState studentBS) {
        this.root = new Node(studentBS);
        System.out.println("======making a tree========");
    }

    public Node getRoot() {
        return this.root;
    }
    
    //This shouldn't be used since we initialise the root in the constructor
    public Node setRoot (Node node) {
    	return this.root = node;
    }

    public void addChild(Node parent, Node child) {
        parent.getChildren().add(child);
    }
	
	
}

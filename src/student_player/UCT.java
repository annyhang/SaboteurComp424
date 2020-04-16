package student_player;

import java.util.Collections;
import java.util.Comparator;

public class UCT {
	//hu
	 /**
     * UCT to get the best node to expand the tree
     * iin our case for this particular game
     * wi/ni + c sqrt(ln t)/ni
     * in this case, wi, is the number of wins after the ith move
     * ni is the number of simulations after the ith move
     * c is the exploration parameter (sqrt2)why 
     * t is the total; number of simulations for the parent node
     */
	
	private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
    	if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestNodeWithUCT(Node node) {
        int parentVisit = node.getBoardState().getNodeVisit();
        return Collections.max(
        		node.getChildren(),
        		Comparator.comparing(c -> uctValue(parentVisit, c.getBoardState().getWinScore(), c.getBoardState().getNodeVisit())));
    }

}

package shortestpathfinder.algorithm;

import java.util.LinkedList;
import java.util.PriorityQueue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import shortestpathfinder.Direction;
import shortestpathfinder.GridValue;
import shortestpathfinder.ShortestPathFinder;

/**
 *
 * @author Kenneth Tran
 */
public class AStar extends PathAlgorithm {

    public enum Heuristic {
        EUCLIDEAN, MANHATTAN;
    }
    
    private PriorityQueue<AStarNode> openSet;
    
    private AStarNode[][] nodeGrid;
    
    private Heuristic heuristic;
    
    public AStar(ShortestPathFinder main) {
        super(main);
    }
    
    private class AStarNode implements Comparable {
        
        int r, c;
        double f = Integer.MAX_VALUE, g = Integer.MAX_VALUE;
        
        AStarNode parent;
        int dist;
        
        LinkedList<AStarNode> neighbours;
        
        public AStarNode(int r, int c) {
            this.neighbours = new LinkedList<>();
            this.r = r;
            this.c = c;
        }
        
        // Override this to let PriorityQueue compare f cost
        @Override
        public int compareTo(Object o) {
            return Double.compare(this.f, ((AStarNode) o).f);
        }
        
    }
    
    public Heuristic getHeuristic() {
        return this.heuristic;
    }
    
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }
    
    // Heuristic function that estimates the cost (distance) from Node 'n' to the goal node
    private double h(AStarNode n) {
        if (this.getHeuristic() == Heuristic.EUCLIDEAN)
            return Math.hypot(getMain().getEndC() - n.c, getMain().getEndR() - n.r);
        else
            return Math.abs(n.c - getMain().getEndC()) + Math.abs(n.r - getMain().getEndR());
    }
    
    @Override
    public void initialize(int delayMs) {
        super.initialize(delayMs);
        
        // Initialization of AStar
        
        openSet = new PriorityQueue<>();
        nodeGrid = new AStarNode[getMain().getGridLength()][getMain().getGridLength()];
        
        // init grid
        for (int i = 0; i < getMain().getGridLength(); i++) {
            for (int j = 0; j < getMain().getGridLength(); j++) {
                nodeGrid[i][j] = new AStarNode(i, j);
            }
        }
        
        // init neighbours
        for (int i = 0; i < getMain().getGridLength(); i++) {
            for (int j = 0; j < getMain().getGridLength(); j++) {
                for (Direction dir : getMain().getVectors()) {
                    int neighbourR = i + dir.getVectorR();
                    int neighbourC = j + dir.getVectorC();
                    
                    if (neighbourR < 0 || neighbourR >= getMain().getGridLength() || neighbourC < 0 || neighbourC >= getMain().getGridLength())
                        continue;
                    if (markerGrid[neighbourR][neighbourC] != GridValue.EMPTY && markerGrid[neighbourR][neighbourC] != GridValue.END) // Ignore obstacles
                        continue;
                    
                    nodeGrid[i][j].neighbours.add(nodeGrid[neighbourR][neighbourC]);
                }
            }
        }
        
        AStarNode start = nodeGrid[getMain().getStartR()][getMain().getStartC()];
        
        openSet.add(start);
        start.f = h(start);
        start.g = 0;
        
        getMain().getGridCanvas().draw();
    }

    @Override
    public EventHandler<ActionEvent> loopEventHandler() {
        return e -> {
            if (!openSet.isEmpty()) { // Visit node with lowest f cost
                AStarNode current = openSet.poll();
                
                if (current.r == getMain().getEndR() && current.c == getMain().getEndC()) { // Reached end
                    AStarNode crawl = current.parent;
                    int dist = 1;
                    
                    // Reconstruct path
                    while (crawl.parent != null) {
                        markerGrid[crawl.r][crawl.c] = GridValue.PATH;
                        crawl = crawl.parent;
                        dist++;
                    }

                    getMain().getGridCanvas().draw();
                    stop();

                    getMain().pathFinderBox.updateAlgLbl("Path: " + dist + " units");
                    return;
                }
                
                markerGrid[current.r][current.c] = GridValue.VISITED;
                
                for (AStarNode neighbour : current.neighbours) { // Examine neighbouring nodes
                    double tempG = current.g + 1; // Cost to travel is 1 unit

                    if (tempG < neighbour.g) { // If the distance to the neighbour from current is less than the pre-existing g(neighbour), then this path is shorter
                        neighbour.parent = current;
                        neighbour.g = tempG;
                        neighbour.f = neighbour.g + h(neighbour); // Calculate f(n) = g(n) + h(n)
                        
                        if (!openSet.contains(neighbour)) {
                            openSet.add(neighbour);
                            
                            if (markerGrid[neighbour.r][neighbour.c] != GridValue.END) // Don't set END to IN_QUEUE
                                markerGrid[neighbour.r][neighbour.c] = GridValue.IN_QUEUE;
                        }
                    }
                }
            } else { // No path found
                stop();
                getMain().pathFinderBox.updateAlgLbl("No path found");
            }
            
            getMain().getGridCanvas().draw();
        };
    }
    
}

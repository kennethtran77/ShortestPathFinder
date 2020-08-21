package shortestpathfinder.algorithm;

import java.util.LinkedList;
import java.util.Queue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import shortestpathfinder.Direction;
import shortestpathfinder.GridValue;
import shortestpathfinder.ShortestPathFinder;

/**
 *
 * @author Kenneth Tran
 */
public final class BFS extends PathAlgorithm {
    
    private Queue<BFSNode> queue;
    
    public BFS(ShortestPathFinder main) {
        super(main);
    }
    
    private class BFSNode {
        
        int r, c;
        
        BFSNode parent;
        
        public BFSNode(int r, int c, BFSNode parent) {
            this.r = r;
            this.c = c;
            
            this.parent = parent;
        }
        
    }
    
    // Initializes the BFS with given delay, copying the current state of the grid
    // Note: the BFS is not deinitialized once the animation finishes. It is deinitialized once
    //       you switch out of path finder mode/select another algorithm
    @Override
    public void initialize(int delayMs) {
        super.initialize(delayMs);
        
        // BFS Algorithm
        queue = new LinkedList<>();
        
        queue.add(new BFSNode(getMain().getStartR(), getMain().getStartC(), null));
    }
    
    @Override
    public final EventHandler<ActionEvent> loopEventHandler() {
        return e -> {
            if (!isRunning()) {
                stop();
                return;
            }
            
            if (!queue.isEmpty()) { // Visit point at head of queue
                BFSNode curr = queue.poll();
                
                markerGrid[curr.r][curr.c] = GridValue.VISITED;
                
                for (Direction dir : getMain().getVectors()) { // Loop through next moves
                    int nextR = curr.r + dir.getVectorR();
                    int nextC = curr.c + dir.getVectorC();
                    
                    if (nextR < 0 || nextR >= getMain().getGridLength() || nextC < 0 || nextC >= getMain().getGridLength())
                        continue;
                    
                    // If next point not visited, add it to queue
                    if (markerGrid[nextR][nextC] == GridValue.EMPTY || markerGrid[nextR][nextC] == GridValue.END) {
                        BFSNode nextPoint = new BFSNode(nextR, nextC, curr);
                        
                        queue.add(nextPoint);

                        if (markerGrid[nextR][nextC] == GridValue.END) { // Found path
                            BFSNode crawl = curr;
                            int dist = 1;
                            
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
                        
                        markerGrid[nextR][nextC] = GridValue.IN_QUEUE;
                    }
                }
            } else { // Did not find any path
                stop();
                getMain().pathFinderBox.updateAlgLbl("No path found");
            }
            
            getMain().getGridCanvas().draw();
        };
    }
    
}
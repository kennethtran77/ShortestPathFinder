package shortestpathfinder;

/**
 *
 * @author Kenneth Tran
 */
public enum Direction {
    
    UP_LEFT(-1, -1),
    UP(-1, 0),
    UP_RIGHT(-1, 1),
    RIGHT(0, 1),
    DOWN_RIGHT(1, 1),
    DOWN(1, 0),
    DOWN_LEFT(1, -1),
    LEFT(0, -1);
    
    private final int rVect, cVect;
    
    private Direction(int rVect, int cVect) {
        this.rVect = rVect;
        this.cVect = cVect;
    }
    
    public int getVectorR() {
        return this.rVect;
    }
    
    public int getVectorC() {
        return this.cVect;
    }

}
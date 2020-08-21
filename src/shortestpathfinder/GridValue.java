package shortestpathfinder;

import javafx.scene.paint.Color;

/**
 *
 * @author Kenneth Tran
 */
public enum GridValue {
    
    EMPTY(Color.WHITE),
    OBSTACLE(Color.web("#333333", 1)),
    START(Color.web("#f003fc", 1)),
    END(Color.web("#04ff00", 1)),
    VISITED(Color.web("#005c99", 1)),
    IN_QUEUE(Color.web("#0099ff", 1)),
    PATH(Color.web("#ffff00", 1));
    
    private final Color colour;
    
    public Color getColour() {
        return this.colour;
    }
    
    private GridValue(Color colour) {
        this.colour = colour;
    }
    
}
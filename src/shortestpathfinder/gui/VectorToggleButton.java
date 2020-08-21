package shortestpathfinder.gui;

import javafx.scene.control.ToggleButton;
import shortestpathfinder.Direction;
import shortestpathfinder.ShortestPathFinder;

/**
 *
 * @author Kenneth Tran
 */
public class VectorToggleButton extends ToggleButton {

    private final Direction direction;

    public VectorToggleButton(ShortestPathFinder main, Direction direction, String text) {
        if (main.vectors.contains(direction))
            throw new IllegalStateException("A vector button with this direction was already initialized");

        this.direction = direction;
        main.vectors.add(direction);
        this.setSelected(true);
        this.setText(text);

        this.setOnAction(e -> {
            if (this.isSelected()) {
                main.vectors.add(this.direction);
            } else {
                main.vectors.remove(this.direction);
            }
        });
    }

    public Direction getDirection() {
        return this.direction;
    }

}
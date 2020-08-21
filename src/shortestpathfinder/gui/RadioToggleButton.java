package shortestpathfinder.gui;

import javafx.scene.control.ToggleButton;

/**
 *
 * @author Kenneth Tran
 * ToggleButton subclass that requires one option to be selected at all times
 */
public class RadioToggleButton extends ToggleButton {

    @Override
    public void fire() {
        if (getToggleGroup() == null || !isSelected()) {
            super.fire();
        }
    }

}
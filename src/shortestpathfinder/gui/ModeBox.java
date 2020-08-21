package shortestpathfinder.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import shortestpathfinder.Mode;
import shortestpathfinder.ShortestPathFinder;

/**
 *
 * @author Kenneth Tran
 */
public class ModeBox {
    
    public VBox component;
    
    public Label lblModeBox;
    
    public ToggleGroup modeToggleGroup;
    public RadioButton rbtnEditor, rbtnPathFinder;
    
    public ModeBox(ShortestPathFinder main) {
        component = new VBox(5);
        component.setMinSize(200, 100);
        component.setPadding(new Insets(10, 10, 10, 10));
        
        lblModeBox = new Label("MODE");
        modeToggleGroup = new ToggleGroup();
        
        rbtnEditor = new RadioButton("Grid Editor");
        rbtnEditor.setToggleGroup(modeToggleGroup);
        rbtnEditor.setSelected(true);
        
        rbtnPathFinder = new RadioButton("Path Finder");
        rbtnPathFinder.setToggleGroup(modeToggleGroup);
        
        component.getChildren().addAll(lblModeBox, rbtnEditor, rbtnPathFinder);
        component.setPadding(new Insets(10, 10, 10, 10));
        component.getStyleClass().add("box");
        
        // Mode Toggle Group Event Listener
        modeToggleGroup.selectedToggleProperty().addListener(e -> {
            RadioButton selected = (RadioButton) modeToggleGroup.getSelectedToggle();
            
            if (selected == rbtnEditor) {
                main.menu.getChildren().remove(main.pathFinderBox.component);
                main.menu.getChildren().add(main.gridEditor.component);
                main.setCurrentMode(Mode.GRID_EDITOR);
                // Deinitialize the current algorithm to clear the grid of previous runs
                main.getCurrentAlgorithm().deinitialize();
            } else if (selected == rbtnPathFinder) {
                main.menu.getChildren().remove(main.gridEditor.component);
                main.menu.getChildren().add(main.pathFinderBox.component);
                main.setCurrentMode(Mode.PATH_FINDER);
            }
        });
        
    }
    
    public void setMenuDisabled(boolean disabled) {
        rbtnEditor.setDisable(disabled);
        rbtnPathFinder.setDisable(disabled);
    }
    
}
package shortestpathfinder.gui;

import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import shortestpathfinder.ShortestPathFinder;
import shortestpathfinder.Tool;

/**
 *
 * @author Kenneth Tran
 */
public class GridEditor {
    
    public VBox component;
    
    public GridEditor(ShortestPathFinder main) {
        component = new VBox(10);
        component.setMinWidth(205);
        component.setMaxWidth(205);
        component.setPadding(new Insets(10, 10, 10, 10));
        component.getStyleClass().add("box");
        
        Label lblSizeSlider = new Label("GRID LENGTH");
        
        // Slider
        Slider sizeSlider = new Slider(5, 50, 5);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setMajorTickUnit(15);
        sizeSlider.setSnapToTicks(true);
        
        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> sizeSlider.setValue(Math.round((double) newVal / 5) * 5));
        
        // Initialize button
        Button btnReinit = new Button("Reinitialize Grid");
        btnReinit.setMinSize(185, 40);
        btnReinit.setMaxSize(185, 40);
        
        btnReinit.setOnAction(e -> {
            int w = (int) sizeSlider.getValue();
            
            main.initializeGrid(w);
            main.getGridCanvas().draw();
        });
        
        // Generate obstacles button
        Button btnGenerateObstacles = new Button("Generate Obstacles");
        btnGenerateObstacles.setMinSize(185, 40);
        btnGenerateObstacles.setMaxSize(185, 40);
 
        btnGenerateObstacles.setOnAction(e -> {
            int w = (int) sizeSlider.getValue();
            
            Random random = new Random();
            
            main.initializeGrid(w);
            
            // 2/5 chance to generate obstacle at any given cell
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < w; j++) {
                    if (random.nextInt(5) <= 1) {
                        main.placeObstacle(i, j);
                    }
                }
            }
            
            main.getGridCanvas().draw();
        });
        
        // ToolBox
        FlowPane toolBox = new FlowPane();
        toolBox.setHgap(10);
        toolBox.setVgap(10);
        toolBox.setMaxWidth(185);
        toolBox.setPadding(new Insets(10, 10, 10, 10));
        toolBox.setId("toolbox");
        
        ToggleGroup toolboxToggleGroup = new ToggleGroup();
        
        ImageView icnDraw = new ImageView(new Image(getClass().getResourceAsStream("/icons/draw.png")));
        icnDraw.setFitHeight(20);
        icnDraw.setFitWidth(20);
        
        RadioToggleButton tbtnDraw = new RadioToggleButton();
        tbtnDraw.setGraphic(icnDraw);
        tbtnDraw.setMinSize(25, 25);
        tbtnDraw.setMaxSize(25, 25);
        tbtnDraw.setFont(Font.font(10));
        tbtnDraw.setToggleGroup(toolboxToggleGroup);
        tbtnDraw.setSelected(true);
        
        ImageView icnErase = new ImageView(new Image(getClass().getResourceAsStream("/icons/erase.png")));
        icnErase.setFitHeight(20);
        icnErase.setFitWidth(20);
        
        RadioToggleButton tbtnErase = new RadioToggleButton();
        tbtnErase.setGraphic(icnErase);
        tbtnErase.setMinSize(25, 25);
        tbtnErase.setMaxSize(25, 25);
        tbtnErase.setFont(Font.font(10));
        tbtnErase.setToggleGroup(toolboxToggleGroup);
        
        ImageView icnBucket = new ImageView(new Image(getClass().getResourceAsStream("/icons/bucket.png")));
        icnBucket.setFitHeight(20);
        icnBucket.setFitWidth(20);
        
        RadioToggleButton tbtnBucket = new RadioToggleButton();
        tbtnBucket.setGraphic(icnBucket);
        tbtnBucket.setMinSize(25, 25);
        tbtnBucket.setMaxSize(25, 25);
        tbtnBucket.setFont(Font.font(10));
        tbtnBucket.setToggleGroup(toolboxToggleGroup);
        
        ImageView icnStart = new ImageView(new Image(getClass().getResourceAsStream("/icons/start.png")));
        icnStart.setFitHeight(20);
        icnStart.setFitWidth(20);
        icnStart.setSmooth(true);
        
        RadioToggleButton tbtnStart = new RadioToggleButton();
        tbtnStart.setGraphic(icnStart);
        tbtnStart.setMinSize(25, 25);
        tbtnStart.setMaxSize(25, 25);
        tbtnStart.setFont(Font.font(10));
        tbtnStart.setToggleGroup(toolboxToggleGroup);
        
        ImageView icnEnd = new ImageView(new Image(getClass().getResourceAsStream("/icons/end.png")));
        icnEnd.setFitHeight(20);
        icnEnd.setFitWidth(20);
        icnEnd.setSmooth(true);
        
        RadioToggleButton tbtnEnd = new RadioToggleButton();
        tbtnEnd.setGraphic(icnEnd);
        tbtnEnd.setMinSize(25, 25);
        tbtnEnd.setMaxSize(25, 25);
        tbtnEnd.setFont(Font.font(10));
        tbtnEnd.setToggleGroup(toolboxToggleGroup);
        
        toolBox.getChildren().addAll(tbtnDraw, tbtnErase, tbtnBucket, tbtnStart, tbtnEnd);
        
        // Toolbox Toggle event handler
        toolboxToggleGroup.selectedToggleProperty().addListener(e -> {
            ToggleButton selected = (ToggleButton) toolboxToggleGroup.getSelectedToggle();
            
            if (selected == tbtnDraw)
                main.setCurrentTool(Tool.DRAW);
            else if (selected == tbtnErase)
                main.setCurrentTool(Tool.ERASE);
            else if (selected == tbtnBucket)
                main.setCurrentTool(Tool.BUCKET);
            else if (selected == tbtnStart)
                main.setCurrentTool(Tool.START);
            else if (selected == tbtnEnd)
                main.setCurrentTool(Tool.END);
        });
        
        component.getChildren().addAll(lblSizeSlider, sizeSlider, btnReinit, btnGenerateObstacles, toolBox);
    }
    
}
package shortestpathfinder.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import shortestpathfinder.Direction;
import shortestpathfinder.ShortestPathFinder;
import shortestpathfinder.algorithm.AStar;

/**
 *
 * @author Kenneth Tran
 */
public class PathFinderBox {
 
    private final ShortestPathFinder main;
    
    public VBox component;
    
    private Button btnPlayPause;
    private Button btnStop;
    
    private RadioButton rbtnEuclidean;
    private RadioButton rbtnManhattan;
    
    private RadioButton rbtnBfs;
    private RadioButton rbtnAStar;
    private TextField txtDelay;
    private GridPane vectorsBox;
    
    private final Label lblAlgInfo;
    
    public PathFinderBox(ShortestPathFinder main) {
        this.main = main;
        
        component = new VBox(5);
        component.setMinWidth(205);
        component.setMaxWidth(205);
        component.setPadding(new Insets(10, 10, 10, 10));
        component.getStyleClass().add("box");
        
        // Algorithm Selector
        Label lblAlgorithm = new Label("ALGORITHM");
        
        ToggleGroup algorithmToggleGroup = new ToggleGroup();
        
        rbtnBfs = new RadioButton("BFS");
        rbtnBfs.setToggleGroup(algorithmToggleGroup);
        rbtnBfs.setSelected(true);
        
        rbtnAStar = new RadioButton("A*");
        rbtnAStar.setToggleGroup(algorithmToggleGroup);
        
        // AStar heuristic
        VBox heuristicBox = new VBox(5);
        
        Label lblHeuristic = new Label("HEURISTIC");
        
        ToggleGroup heuristicToggleGroup = new ToggleGroup();
        
        rbtnEuclidean = new RadioButton("Euclidian");
        rbtnEuclidean.setToggleGroup(heuristicToggleGroup);
        rbtnEuclidean.setSelected(true);
        
        rbtnManhattan = new RadioButton("Manhattan");
        rbtnManhattan.setToggleGroup(heuristicToggleGroup);
        
        // Toggle heuristic event handler
        heuristicToggleGroup.selectedToggleProperty().addListener(e -> {
            RadioButton selected = (RadioButton) heuristicToggleGroup.getSelectedToggle();
            
            if (selected == rbtnEuclidean)
                main.AStarInstance.setHeuristic(AStar.Heuristic.EUCLIDEAN);
            else if (selected == rbtnManhattan)
                main.AStarInstance.setHeuristic(AStar.Heuristic.MANHATTAN);
            
            main.setCurrentAlgorithm(main.AStarInstance);
        });
        
        // Toggle algorithm event handler
        algorithmToggleGroup.selectedToggleProperty().addListener(e -> {
            RadioButton selected = (RadioButton) algorithmToggleGroup.getSelectedToggle();
            
            if (selected == rbtnBfs) {
                main.setCurrentAlgorithm(main.bfsInstance);
                
                heuristicBox.getChildren().remove(lblHeuristic);
                heuristicBox.getChildren().remove(rbtnEuclidean);
                heuristicBox.getChildren().remove(rbtnManhattan);
            } else if (selected == rbtnAStar) {
                main.setCurrentAlgorithm(main.AStarInstance);
                
                heuristicBox.getChildren().add(lblHeuristic);
                heuristicBox.getChildren().add(rbtnEuclidean);
                heuristicBox.getChildren().add(rbtnManhattan);
            }
        });
        
        // Vectors
        Label lblVectors = new Label("VECTORS");
        
        vectorsBox = new GridPane();
        vectorsBox.setVgap(10);
        vectorsBox.setHgap(10);
        vectorsBox.setPadding(new Insets(10, 10, 10, 10));
        
        VectorToggleButton tbtnUpLeft = new VectorToggleButton(main, Direction.UP_LEFT, "🡴");
        tbtnUpLeft.setFont(Font.font(8));
        tbtnUpLeft.setMinSize(25, 25);
        tbtnUpLeft.setMaxSize(25, 25);
        
        VectorToggleButton tbtnUp = new VectorToggleButton(main, Direction.UP, "🡱");
        tbtnUp.setFont(Font.font(8));
        tbtnUp.setMinSize(25, 25);
        tbtnUp.setMaxSize(25, 25);
        
        VectorToggleButton tbtnUpRight = new VectorToggleButton(main, Direction.UP_RIGHT, "🡵");
        tbtnUpRight.setFont(Font.font(8));
        tbtnUpRight.setMinSize(25, 25);
        tbtnUpRight.setMaxSize(25, 25);
        
        VectorToggleButton tbtnRight = new VectorToggleButton(main, Direction.RIGHT, "🡲");
        tbtnRight.setFont(Font.font(8));
        tbtnRight.setMinSize(25, 25);
        tbtnRight.setMaxSize(25, 25);
        
        VectorToggleButton tbtnDownRight = new VectorToggleButton(main, Direction.DOWN_RIGHT, "🡶");
        tbtnDownRight.setFont(Font.font(8));
        tbtnDownRight.setMinSize(25, 25);
        tbtnDownRight.setMaxSize(25, 25);
        
        VectorToggleButton tbtnDown = new VectorToggleButton(main, Direction.DOWN, "🡳");
        tbtnDown.setFont(Font.font(8));
        tbtnDown.setMinSize(25, 25);
        tbtnDown.setMaxSize(25, 25);
        
        VectorToggleButton tbtnDownLeft = new VectorToggleButton(main, Direction.DOWN_LEFT, "🡷");
        tbtnDownLeft.setFont(Font.font(8));
        tbtnDownLeft.setMinSize(25, 25);
        tbtnDownLeft.setMaxSize(25, 25);
        
        VectorToggleButton tbtnLeft = new VectorToggleButton(main, Direction.LEFT, "🡰");
        tbtnLeft.setFont(Font.font(8));
        tbtnLeft.setMinSize(25, 25);
        tbtnLeft.setMaxSize(25, 25);
        
        vectorsBox.add(tbtnUpLeft, 0, 0);
        vectorsBox.add(tbtnUp, 1, 0);
        vectorsBox.add(tbtnUpRight, 2, 0);
        vectorsBox.add(tbtnRight, 2, 1);
        vectorsBox.add(tbtnDownRight, 2, 2);
        vectorsBox.add(tbtnDown, 1, 2);
        vectorsBox.add(tbtnDownLeft, 0, 2);
        vectorsBox.add(tbtnLeft, 0, 1);
        
        // Animation delay
        Label lblDelay = new Label("ANIMATE DELAY(ms)");
        
        HBox delayBox = new HBox(10);
        
        txtDelay = new TextField("1");
        txtDelay.setMinSize(60, 40);
        txtDelay.setMaxSize(60, 40);
        
        btnPlayPause = new Button("▶");
        btnPlayPause.setMinSize(40, 40);
        btnPlayPause.setMaxSize(40, 40);
        btnPlayPause.getProperties().put("paused", true);
        
        btnStop = new Button("⏹");
        btnStop.setDisable(true);
        btnStop.setMinSize(40, 40);
        btnStop.setMaxSize(40, 40);
        
        // Play Pause Button Event
        btnPlayPause.setOnAction(e -> {
            if ((Boolean) btnPlayPause.getProperties().get("paused")) { // If pressed play
                if (!main.getCurrentAlgorithm().isInitialized() || !main.getCurrentAlgorithm().isRunning()) { // If algorithm wasn't initialized or is not running
                    if (main.getStartR() == -1 || main.getStartC() == -1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You did not set a start point.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    if (main.getEndR() == -1 || main.getEndC() == -1) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You did not set an end point.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    if (txtDelay.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You did not enter a delay interval.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    int delayMs;

                    try {
                        delayMs = Integer.parseInt(txtDelay.getText());
                    } catch (NumberFormatException exception) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You did not enter a proper integer.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    if (delayMs <= 0 || delayMs > 1000) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You can only enter a delay interval between 1 and 1000 ms.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    if (main.vectors.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.NONE, "You didn't select any vectors.", ButtonType.CLOSE);
                        alert.show();
                        return;
                    }

                    main.getCurrentAlgorithm().deinitialize();
                    main.getCurrentAlgorithm().initialize(delayMs);
                    main.getCurrentAlgorithm().start();
                } else if (main.getCurrentAlgorithm().isRunning()) { // Path algorithm is running; pressed unpause
                    main.getCurrentAlgorithm().resume();
                }

                btnPlayPause.setText("⏸");
                btnPlayPause.getProperties().put("paused", false);
                main.setMenuDisabled(true);
            } else { // If pressed pause
                main.getCurrentAlgorithm().pause();
                btnPlayPause.setText("▶");
                btnPlayPause.getProperties().put("paused", true);
            }
        });
        
        btnStop.setOnAction(e -> {
            main.getCurrentAlgorithm().deinitialize();
            main.setMenuDisabled(false);
            btnPlayPause.setText("▶");
            btnPlayPause.getProperties().put("paused", true);
        });
        
        lblAlgInfo = new Label();
        
        delayBox.getChildren().addAll(txtDelay, btnPlayPause, btnStop);
        
        component.getChildren().addAll(lblAlgorithm, rbtnBfs, rbtnAStar, heuristicBox, lblDelay, delayBox, lblVectors, vectorsBox, lblAlgInfo);
    }
    
    public void updateAlgLbl(String text) {
        if (text.isEmpty())
            this.lblAlgInfo.setVisible(false);
        else
            this.lblAlgInfo.setVisible(true);
        
        this.lblAlgInfo.setText(text);
    }
    
    // Disable the path finder box except for play/pause and stop button
    public void setMenuDisabled(boolean disabled) {
        btnStop.setDisable(!disabled);
        rbtnEuclidean.setDisable(disabled);
        rbtnManhattan.setDisable(disabled);
        rbtnBfs.setDisable(disabled);
        rbtnAStar.setDisable(disabled);
        txtDelay.setDisable(disabled);
        vectorsBox.setDisable(disabled);
        
        if (!disabled && !main.getCurrentAlgorithm().isRunning()) {
            btnPlayPause.setText("▶");
            btnPlayPause.getProperties().put("paused", true);
        }
    }
    
}
package shortestpathfinder;

import shortestpathfinder.algorithm.AStar;
import shortestpathfinder.algorithm.BFS;
import shortestpathfinder.algorithm.PathAlgorithm;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shortestpathfinder.gui.GridEditor;
import shortestpathfinder.gui.ModeBox;
import shortestpathfinder.gui.PathFinderBox;

/**
 *
 * @author Kenneth Tran
 */
public final class ShortestPathFinder extends Application {
    
    private GridValue[][] grid;
    
    // Keep track of the start and end positions
    private final int[] start = new int[] { -1, -1 };
    private final int[] end = new int[] { -1, -1 } ;
    
    private Tool currentTool = Tool.DRAW;
    private Mode mode = Mode.GRID_EDITOR;
    
    public final LinkedList<Direction> vectors = new LinkedList<>();
    
    public VBox menu;
    
    public ModeBox modeBox;
    public GridEditor gridEditor;
    public PathFinderBox pathFinderBox;

    private GridCanvas canvas;
    
    private PathAlgorithm currentAlgorithm;
    
    public BFS bfsInstance;
    public AStar AStarInstance;
    
    @Override
    public void start(Stage primaryStage) {
        initializeGrid(5);
        
        canvas = new GridCanvas(this);

        bfsInstance = new BFS(this);
        AStarInstance = new AStar(this);
        
        currentAlgorithm = bfsInstance;
        
        // Menu pane
        menu = new VBox(25);
        menu.setAlignment(Pos.CENTER_RIGHT);
        menu.setPadding(new Insets(50, 50, 50, 50));
        
        // Mode selector
        modeBox = new ModeBox(this);
        // Grid editor
        gridEditor = new GridEditor(this);
        // Path finder
        pathFinderBox = new PathFinderBox(this);

        menu.getChildren().add(modeBox.component);
        menu.getChildren().add(gridEditor.component);
        
        // Root pane
        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(canvas.getWrapperPane());
        
        // Scene    
        Scene scene = new Scene(root, 1050, 750);
        scene.getStylesheets().add("shortestpathfinder/styles.css");
        
        // Stage
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1075);
        primaryStage.setMinHeight(790);
        primaryStage.setTitle("Shortest Path Finder");
        primaryStage.show();
    }
    
    public void setMenuDisabled(boolean disabled) {
        modeBox.setMenuDisabled(disabled);
        pathFinderBox.setMenuDisabled(disabled);
    }
    
    public Tool getCurrentTool() {
        return this.currentTool;
    }
    
    public void setCurrentTool(Tool tool) {
        this.currentTool = tool;
    }
    
    public Mode getCurrentMode() {
        return this.mode;
    }
    
    public void setCurrentMode(Mode mode) {
        this.mode = mode;
    }
    
    public LinkedList<Direction> getVectors() {
        return this.vectors;
    }
    
    public int getStartR() {
        return this.start[0];
    }
    
    public int getStartC() {
        return this.start[1];
    }
    
    public int getEndR() {
        return this.end[0];
    }
    
    public int getEndC() {
        return this.end[1];
    }
    
    // Set to -1, -1 to remove
    public void setStart(int r, int c) {
        if (r < -1 || r >= grid.length)
            throw new IllegalArgumentException("r is out of bounds");
        if (c < -1 || c >= grid.length)
            throw new IllegalArgumentException("c is out of bounds");
        if (currentAlgorithm.isInitialized())
            throw new IllegalStateException("Cannot modify grid when path algorithm is initialized");
        
        // Replace end if it was previously at this position
        if (r == this.getEndR() && c == this.getEndC())
            this.setEnd(-1, -1);
        
        if (start[0] >= 0 && start[1] >= 0)
            this.grid[start[0]][start[1]] = GridValue.EMPTY; // Remove the old start position
        
        this.start[0] = r;
        this.start[1] = c;
        
        if (r >= 0 && c >= 0) // Place new start position if in bounds
            this.grid[r][c] = GridValue.START;
    }
    
    // Set to -1, -1 to remove
    public void setEnd(int r, int c) {
        if (r < -1 || r >= grid.length)
            throw new IllegalArgumentException("r is out of bounds");
        if (c < -1 || c >= grid.length)
            throw new IllegalArgumentException("c is out of bounds");
        if (currentAlgorithm.isInitialized())
            throw new IllegalStateException("Cannot modify grid when path algorithm is initialized");
        
        // Replace start if it was previously at this position
        if (r == this.getStartR() && c == this.getStartC())
            this.setStart(-1, -1);
        
        if (end[0] >= 0 && end[1] >= 0)
            this.grid[end[0]][end[1]] = GridValue.EMPTY; // Remove the old end position
        
        this.end[0] = r;
        this.end[1] = c;
        
        if (r >= 0 && c >= 0) // Place new end position if in bounds
            this.grid[r][c] = GridValue.END;
    }
    
    public void placeObstacle(int r, int c) {
        if (r < 0 || r >= grid.length)
            throw new IllegalArgumentException("r is out of bounds");
        if (c < 0 || c >= grid.length)
            throw new IllegalArgumentException("c is out of bounds");
        if (currentAlgorithm.isInitialized())
            throw new IllegalStateException("Cannot modify grid when path algorithm is initialized");
        
        // Replace start/end if it was previously at this position
        if (r == this.getStartR() && c == this.getStartC())
            this.setStart(-1, -1);
        if (r == this.getEndR() && c == this.getEndC())
            this.setEnd(-1, -1);
        
        this.grid[r][c] = GridValue.OBSTACLE;
    }
    
    public void erase(int r, int c) {
        if (r < 0 || r >= grid.length)
            throw new IllegalArgumentException("r is out of bounds");
        if (c < 0 || c >= grid.length)
            throw new IllegalArgumentException("c is out of bounds");
        if (currentAlgorithm.isInitialized())
            throw new IllegalStateException("Cannot modify grid when path algorithm is initialized");
        
        // Replace start/end if it was previously at this position
        if (r == this.getStartR() && c == this.getStartC())
            this.setStart(-1, -1);
        if (r == this.getEndR() && c == this.getEndC())
            this.setEnd(-1, -1);
        
        this.grid[r][c] = GridValue.EMPTY;
    }
    
    public GridValue getValueOnGrid(int r, int c) {
        if (r < 0 || r >= grid.length)
            throw new IllegalArgumentException("r is out of bounds");
        if (c < 0 || c >= grid.length)
            throw new IllegalArgumentException("c is out of bounds");
        
        return grid[r][c];
    }
    
    public int getGridLength() {
        return this.grid.length;
    }
    
    public GridValue[][] cloneGrid() {
        return Arrays.stream(grid).map(GridValue[]::clone).toArray(GridValue[][]::new);
    }
    
    public void initializeGrid(int w) {
        grid = new GridValue[w][w];
        
        start[0] = -1;
        start[1] = -1;
        end[0] = -1;
        end[1] = -1;
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                grid[i][j] = GridValue.EMPTY;
            }
        }
    }
    
    public void setCurrentAlgorithm(PathAlgorithm algorithm) {
        if (this.currentAlgorithm != null)
            this.currentAlgorithm.deinitialize();
        
        this.currentAlgorithm = algorithm;
    }
    
    public PathAlgorithm getCurrentAlgorithm() {
        return this.currentAlgorithm;
    }
    
    public GridCanvas getGridCanvas() {
        return this.canvas;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

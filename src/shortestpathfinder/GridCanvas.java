package shortestpathfinder;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

/**
 *
 * @author Kenneth Tran
 */
public final class GridCanvas {
    
    private final ShortestPathFinder main;
    
    private Canvas canvas;
    
    protected GridCanvas(ShortestPathFinder main) {
        this.main = main;
        this.canvas = new Canvas();
        
        // Canvas for main.grid
        Pane wrapperPane = new Pane();
        wrapperPane.setMaxSize(700, 700);

        wrapperPane.getChildren().add(canvas);
        wrapperPane.setStyle("-fx-background-color: white;");
        
        // Bind width/height property to wrapper pane
        canvas.widthProperty().bind(wrapperPane.widthProperty());
        canvas.heightProperty().bind(wrapperPane.heightProperty());
        
        // Redraw on resize
        canvas.widthProperty().addListener(e -> draw());
        canvas.heightProperty().addListener(e -> draw());
        
        // Events
        canvas.setOnMouseDragged(e -> {
            editGrid(e.getX(), e.getY(), canvas);
        });
        canvas.setOnMousePressed(e -> {
            editGrid(e.getX(), e.getY(), canvas);
        });
    }
    
    private void editGrid(double x, double y, Canvas canvas) {
        if (main.getCurrentMode() != Mode.GRID_EDITOR)
            return;
            
        int cellLength = (int) canvas.getWidth() / main.getGridLength();

        // Calculate the r and c from the mouse position
        int r = (int) y / cellLength;
        int c = (int) x / cellLength;

        if (r < 0 || r >= main.getGridLength() || c < 0 || c >= main.getGridLength())
            return;
        
        if (null != main.getCurrentTool()) switch (main.getCurrentTool()) {
            case DRAW:
                main.placeObstacle(r, c);
                break;
            case ERASE:
                main.erase(r, c);
                break;
            case BUCKET:
                floodFill(r, c);
                break;
            case START:
                main.setStart(r, c);
                break;
            case END:
                main.setEnd(r, c);
                break;
            default:
                break;
        }
        
        draw();
    }
    
    // Flood fill with obstacles
    private void floodFill(int r, int c) {
        if (r < 0 || r >= main.getGridLength() || c < 0 || c >= main.getGridLength() || main.getValueOnGrid(r, c) != GridValue.EMPTY)
            return;
        
        main.placeObstacle(r, c);
        
        floodFill(r + 1, c);
        floodFill(r - 1, c);
        floodFill(r, c + 1);
        floodFill(r, c - 1);
    }
    
    public void draw() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        
        double cellLength = height / main.getGridLength();
        
        for (int i = 0; i < main.getGridLength(); i++) {
            for (int j = 0; j < main.getGridLength(); j++) {
                switch (main.getValueOnGrid(i, j)) {
                    case EMPTY: // Empty cell
                        gc.setFill(main.getValueOnGrid(i, j).getColour());
                        gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        break;
                    case OBSTACLE: // Obstacle
                        gc.setFill(main.getValueOnGrid(i, j).getColour());
                        gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        break;
                    case START: // Start
                        gc.setFill(main.getValueOnGrid(i, j).getColour());
                        gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        break;
                    case END: // End
                        gc.setFill(main.getValueOnGrid(i, j).getColour());
                        gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                        break;
                    default:
                        break;
                }
                
                // If a path algorithm is initialized, colour the respective cells
                if (main.getCurrentAlgorithm() != null && main.getCurrentAlgorithm().isInitialized()) {
                    switch (main.getCurrentAlgorithm().getValueAtAlgorithmGrid(i, j)) {
                        case VISITED: // Visited
                            if (main.getStartR() == i && main.getStartC() == j)
                                continue;
                            
                            gc.setFill(main.getCurrentAlgorithm().getValueAtAlgorithmGrid(i, j).getColour());
                            gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            break;
                        case IN_QUEUE: // In queue
                            gc.setFill(main.getCurrentAlgorithm().getValueAtAlgorithmGrid(i, j).getColour());
                            gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            break;
                        case PATH: // Path
                            gc.setFill(main.getCurrentAlgorithm().getValueAtAlgorithmGrid(i, j).getColour());
                            gc.fillRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            gc.strokeRect(j * cellLength, i * cellLength, cellLength, cellLength);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        
        gc.stroke();
    }
    
    protected Pane getWrapperPane() {
        return (Pane) canvas.getParent();
    }
    
}
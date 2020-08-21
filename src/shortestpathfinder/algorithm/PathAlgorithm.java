package shortestpathfinder.algorithm;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import shortestpathfinder.GridValue;
import shortestpathfinder.ShortestPathFinder;

/**
 *
 * @author Kenneth Tran
 */
public abstract class PathAlgorithm {
    
    private final ShortestPathFinder main;
    
    protected GridValue[][] markerGrid;
    
    private boolean initialized, running;
    
    private Timeline timeline;
    
    public PathAlgorithm(ShortestPathFinder main) {
        this.main = main;
    }
    
    // Must implement EventHandler for the the ActionEvent that is fired during each iteration of the KeyFrame
    protected abstract EventHandler<ActionEvent> loopEventHandler();
    
    // Must call super when overriding
    // Initialize by creating a new marker grid and constructing timeline
    public void initialize(int delayMs) {
        if (initialized)
            throw new IllegalStateException(this.getClass().getName() + " is already initialized");
        if (main.getCurrentAlgorithm() != null && main.getCurrentAlgorithm() != this)
            throw new IllegalStateException("Another algorithm is already initialized");
        
        deinitialize();
        initialized = true;
        markerGrid = getMain().cloneGrid();
        
        timeline = new Timeline(new KeyFrame(Duration.millis(delayMs), loopEventHandler()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }
    
    // Pauses the event loop (running = true)
    public final void pause() {
        if (!initialized)
            throw new IllegalStateException(this.getClass().getName() + " has not been initialized yet");
        if (!running)
            throw new IllegalStateException(this.getClass().getName() + " is not currently running");
        
        main.pathFinderBox.updateAlgLbl("Paused");
        timeline.stop();
    }

    // Stops the event loop (running = false)
    public final void stop() {
        if (!initialized)
            throw new IllegalStateException(this.getClass().getName() + " has not been initialized yet");
        if (!running)
            throw new IllegalStateException(this.getClass().getName() + " is not currently running");
        
        running = false;
        
        getMain().setMenuDisabled(false);
        main.pathFinderBox.updateAlgLbl("");
        timeline.stop();
    }
    
    // Unpauses the event loop
    public final void resume() {
        if (!initialized)
            throw new IllegalStateException(this.getClass().getName() + " has not been initialized yet");
        if (!running)
            throw new IllegalStateException(this.getClass().getName() + " is not currently running");
        
        main.pathFinderBox.updateAlgLbl("Running...");
        timeline.play();
    }
    
    // Starts playing the event loop
    public final void start() {
        if (!initialized)
            throw new IllegalStateException(this.getClass().getName() + " has not been initialized yet");
        if (running)
            throw new IllegalStateException(this.getClass().getName() + " hasn't finished running yet");
        if (timeline == null)
            throw new IllegalStateException("No timeline was instanciated");
        
        running = true;
        main.pathFinderBox.updateAlgLbl("Running...");
        
        timeline.play();
    }

    // Deinitailize the current event loop: clear the marker grid and redraw the canvas
    public final void deinitialize() {
        if (timeline != null)
            timeline.stop();
        
        initialized = false;
        running = false;
        markerGrid = null;
        main.pathFinderBox.updateAlgLbl("");
        
        main.getGridCanvas().draw();
    }
    
    public final boolean isInitialized() {
        return initialized;
    }
    
    // If the event loop is "activated" (running = true when paused)
    public final boolean isRunning() {
        return running;
    }
    
    public final GridValue getValueAtAlgorithmGrid(int r, int c) {
        if (!initialized)
            throw new IllegalStateException(this.getClass().getName() + " has not been initialized yet");
        
        return markerGrid[r][c];
    }
    
    public final ShortestPathFinder getMain() {
        return this.main;
    }
    
}
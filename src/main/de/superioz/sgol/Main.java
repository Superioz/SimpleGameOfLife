package main.de.superioz.sgol;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.de.superioz.sgol.util.LabelManager;

import java.util.Random;

/**
 * Class created on 19.03.2015
 *
 * Game made with the idea of "Conway's Game of Life"
 * Inspired by hameister 'http://www.hameister.org'
 */
public class Main extends Application {

    public static Stage stage;
    public static final double FIELD_WIDTH = 520.0;
    public static final double FIELD_HEIGHT = 380.0;
    public static final double CELL_SIZE = 10.0;

    public static final String YOUNG_CELL_STYLE = "youngCell";
    public static final String ELDER_CELL_STYLE = "elderCell";
    public static final String OLD_CELL_STYLE = "oldCell";
    public static final String DEAD_CELL_STYLE = "deadCell";

    public static final int YOUNG_AGE = 20;
    public static final int ELDER_AGE = 40;
    public static final int OLD_AGE = 60;

    public final Timeline animation = new Timeline();
    public CellWorld cellWorld;
    public static Random random = new Random();

    @Override
    public void start(Stage s) throws Exception{
        // Stage
        s.centerOnScreen();
        stage = s;

        // Pane
        Pane p = new Pane();
        Scene sc = new Scene(p, FIELD_WIDTH, FIELD_HEIGHT);
        sc.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());

        // Cellworld
        cellWorld = new CellWorld(FIELD_WIDTH/CELL_SIZE, FIELD_HEIGHT/CELL_SIZE);

        //Init Animation
        this.initAnimation();

        // Put stackpanes (cells) into cellworld
        for(int x = 0; x < FIELD_WIDTH; x+=CELL_SIZE){
            for(int y = 0; y < FIELD_HEIGHT; y+=CELL_SIZE){
                Cell c = new Cell(
                    x, y, CELL_SIZE, CELL_SIZE, DEAD_CELL_STYLE
                );

                // Adding cell to world and pane
                p.getChildren().add(c);
                cellWorld.addCell(c);
            }
        }

        // Create population
        cellWorld.createPopulation(32.25);

        // Add Labels
        LabelManager.initLabels(p);
        LabelManager.setMenu(true);
        LabelManager.setIngameStats(false);
        LabelManager.setGamePausedLabel(false);

        // Events
        this.handleEvents(sc);

        // Last steps for stage
        s.setScene(sc);
        s.setTitle("GameOfLife");
        s.sizeToScene();
        s.setResizable(false);

        // Show
        s.show();
    }

    public static void main(String[] args){ launch(args); }

    /**
     * Start the next generation. Checks first all cells and their state
     * and then give them the specific color
     */
    private void nextDay(){
        cellWorld.nextDay();

        // Refresh the colors
        cellWorld.refreshCellColors();
    }

    /**
     * Init the animation for playing the game
     */
    public void initAnimation(){
        this.animation.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, event -> {
            nextDay();
        }), new KeyFrame(Duration.millis(100)));

        this.animation.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Adding events to given pane
     */
    public void handleEvents(Scene sc){
        sc.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event){
                KeyCode pressedKey = event.getCode();

                switch(pressedKey){
                    case SPACE:
                        // Start / Pause / Resume game
                        if(LabelManager.isMenuVisible()){
                            // Is in menu -> start game
                            LabelManager.setMenu(false);
                            LabelManager.setIngameStats(true);

                            animation.play();
                        }else if(animation.getStatus() == Animation.Status.RUNNING){
                            // Pause game
                            LabelManager.setGamePausedLabel(true);
                            LabelManager.setGameHeader(true);

                            animation.pause();
                        }else if(animation.getStatus() == Animation.Status.PAUSED){
                            // Resume game
                            // Pause game
                            LabelManager.setGamePausedLabel(false);
                            LabelManager.setGameHeader(false);

                            animation.play();
                        }
                        break;
                    case ESCAPE:
                        // Exit game -> go to menu
                        if(animation.getStatus() != Animation.Status.STOPPED){
                            LabelManager.setIngameStats(false);
                            LabelManager.setMenu(true);
                            LabelManager.setGamePausedLabel(false);
                            cellWorld.reset();
                            cellWorld.resetStats();

                            animation.stop();
                        }

                        break;
                    case G:
                        // Toggle Grid
                        if(animation.getStatus() != Animation.Status.STOPPED){
                            cellWorld.grid = !cellWorld.grid;
                            cellWorld.refreshGrid();
                        }

                        break;
                    case S:
                        // Toggle visibility of stats
                        if(animation.getStatus() != Animation.Status.STOPPED)
                            LabelManager.setIngameStats(!LabelManager.isIngameStatsVisible());

                        break;
                    default:
                        break;
                }
            }
        });
    }

}

package main.de.superioz.sgol.util;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import main.de.superioz.sgol.Main;

/**
 * Class created on 20.03.2015
 */
public class LabelManager {

    private static Label gamenameHeader;
    private static Label controls;
    private static Label pressToStart;

    private static Label dayWhat;
    private static Label yearWhat;
    private static Label population;

    private static Label gamePaused;


    public static void initLabels(Pane pane){
        gamenameHeader = new Label("Conway's Game of Life");
        gamenameHeader.setLayoutX(10);
        gamenameHeader.setLayoutY(10);
        gamenameHeader.getStyleClass().add("gamenameHeader");

        controls = new Label("Controls:" +
                "\n> Escape = Exit game, go to this menu" +
                "\n> Space = Pause/Resume game" +
                "\n> G = Toggles the grid in background" +
                "\n> S = Toggles the visibility of stats");
        controls.setLayoutX(gamenameHeader.getLayoutX());
        controls.setLayoutY(gamenameHeader.getLayoutY() * 6);
        controls.getStyleClass().add("controls");

        pressToStart = new Label("-> Press 'Space' to start simulation");
        pressToStart.setLayoutX(controls.getLayoutX());
        pressToStart.setLayoutY(controls.getLayoutY() * 5);
        pressToStart.getStyleClass().add("presstoStart");
        /*
        End of menu
         */

        dayWhat = new Label("Day: 0");
        dayWhat.setLayoutX(Main.FIELD_WIDTH - 80);
        dayWhat.setLayoutY(Main.FIELD_HEIGHT - 25);
        dayWhat.getStyleClass().add("stats");

        yearWhat = new Label("Year: 0");
        yearWhat.setLayoutX(dayWhat.getLayoutX() - 60);
        yearWhat.setLayoutY(dayWhat.getLayoutY());
        yearWhat.getStyleClass().add("stats");

        population = new Label("Population: 0");
        population.setLayoutX(yearWhat.getLayoutX() - 120);
        population.setLayoutY(dayWhat.getLayoutY());
        population.getStyleClass().add("stats");
        /*
        End of stats
         */

        gamePaused = new Label("Game paused. Press 'Space' to resume.");
        gamePaused.setLayoutX(gamenameHeader.getLayoutX());
        gamePaused.setLayoutY(gamenameHeader.getLayoutY() * 6);
        gamePaused.getStyleClass().add("gamePaused");

        // Add Children
        pane.getChildren().addAll(gamenameHeader, controls, pressToStart,
                dayWhat, yearWhat, population, gamePaused);
    }

    public static void setMenu(boolean b){
        gamenameHeader.setVisible(b);
        controls.setVisible(b);
        pressToStart.setVisible(b);
    }

    public static void setIngameStats(boolean b){
        dayWhat.setVisible(b);
        yearWhat.setVisible(b);
        population.setVisible(b);
    }

    public static boolean isIngameStatsVisible(){
        return dayWhat.isVisible() && yearWhat.isVisible() && population.isVisible();
    }

    public static boolean isMenuVisible(){
        return gamenameHeader.isVisible()
                && controls.isVisible()
                && pressToStart.isVisible();
    }

    public static void setGamePausedLabel(boolean b){
        gamePaused.setVisible(b);
    }

    public static void setGameHeader(boolean b){
        gamenameHeader.setVisible(b);
    }

    public static void setStatsLabel(String pop, String day, String year){
        dayWhat.setText("Day: " + day);
        yearWhat.setText("Year: " + year);
        population.setText("Population: " + pop);
    }

    public static int getDay(){
        String[] values = dayWhat.getText().split(":");
        String s = values[1].replaceAll(" ", "");

        return Integer.parseInt(s);
    }

    public static int getYear(){
        String[] values = yearWhat.getText().split(":");
        String s = values[1].replaceAll(" ", "");

        return Integer.parseInt(s);
    }

}

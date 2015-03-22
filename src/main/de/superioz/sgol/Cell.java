package main.de.superioz.sgol;

import javafx.scene.layout.StackPane;

/**
 * Class created on 19.03.2015
 */
public class Cell extends StackPane {

    private int x;
    private int y;

    public Cell(int layoutX, int layoutY, double prefHeight, double prefWidth, String styleClass){
        super();
        this.setLayoutX(layoutX);
        this.setLayoutY(layoutY);
        this.setPrefHeight(prefHeight);
        this.setPrefWidth(prefWidth);
        this.getStyleClass().add(styleClass);

        this.x = layoutX;
        this.y = layoutY;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

}

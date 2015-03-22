package main.de.superioz.sgol;

import main.de.superioz.sgol.util.LabelManager;

import java.util.*;

/**
 * Class created on 19.03.2015
 */
public class CellWorld {

    private HashMap<String, Cell> cellMap = new HashMap<>();
    public int[][] cells;
    private int cellsWidthSize;
    private int cellsHeightSize;

    public boolean grid = false;

    public CellWorld(double widthSize, double heightSize){
        this.cellsWidthSize = (int)widthSize;
        this.cellsHeightSize = (int)heightSize;

        cells = new int[cellsWidthSize][cellsHeightSize];
    }

    /**
     * Returns a cell with the given coords
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public boolean isAlive(int x, int y){
        return y >= 0 && x >= 0
                && y < Main.FIELD_HEIGHT && x < Main.FIELD_WIDTH
                && cells[x][y] >= 1;

    }

    public int getAliveStatus(int x, int y){
        if(y >= 0 && x >= 0
                && y < Main.FIELD_HEIGHT
                && x < Main.FIELD_WIDTH){
            return cells[x][y];
        }

        return 0;
    }

    /**
     * Creates random living and dead cells
     * The param at setAlive(..) is the density between 0 and 1
     */
    public void createPopulation(double percent){
        for(int x = 0; x < cellsWidthSize; x++) {
            for(int y = 0; y < cellsHeightSize; y++) {
                cells[x][y] = this.calcProbability(percent) ? 1 : 0;
            }
        }
    }

    /**
     * Turns into the next generation and kills / born cells
     * Copy the cellMap, reads from it and then apply
     */
    public void nextDay(){
        int[][] newCellWorld = new int[cellsWidthSize][cellsHeightSize];

        // Check Cells in Cell World
        for(int x = 0; x < cellsWidthSize; x++) {
            for(int y = 0; y < cellsHeightSize; y++) {
                newCellWorld[x][y] = getAliveStatus(x,y);
                checkCell(x, y, newCellWorld);
            }
        }

        // Apply Changes
        cells = newCellWorld;
        this.updateStats();
    }

    // Checks the rules from the game
    public void checkCell(int x, int y, int[][] newCellWorld){
        // Variables
        boolean isAlive = isAlive(x, y);
        int aliveStatus = newCellWorld[x][y];
        int aliveNeighbours = 0;

        // Loop through all neighbours
        for(boolean b : getCellNeighboursOf(x,y)){
            if(b) aliveNeighbours++;
        }

        /*
         If the cell is dead and 3 alive neighbours
          */
        if(!isAlive && aliveNeighbours==3){
            newCellWorld[x][y] = 1;
            return;
        }

        /*
         If the cell is living and less then 2 alive neighbours
          */
        if(isAlive && aliveNeighbours<2){
            newCellWorld[x][y] = 0;
            return;
        }

        /*
        If the cell is living and 3 or 2 alive neighbours
         */
        if(isAlive && (aliveNeighbours==3 || aliveNeighbours==2)){
            if(aliveStatus == Main.OLD_AGE)
                newCellWorld[x][y] = 0;
            else{
                newCellWorld[x][y]++;
            }

            return;
        }

        /*
        If the cell is living and more then 3 alive neighbours
         */
        if(isAlive && aliveNeighbours>3){
            newCellWorld[x][y] = 0;
        }
    }

    /**
     * Returns a list with all neighbours from the given cell
     */
    public List<Boolean> getCellNeighboursOf(int x, int y){
        List<Boolean> neighbours = new ArrayList<>();

        // Locations of neighbours
        int[] xRows = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] yRows = {1, 1, 1, 0, 0, -1, -1, -1};


        // Cells of the neighbours
        for(int i = 0; i < 8; i++) {
            if(x+xRows[i] >= 0 && y+yRows[i] >= 0
                    && x+xRows[i] < cellsWidthSize && y+yRows[i] < cellsHeightSize) {
                neighbours.add(isAlive(x + xRows[i], y + yRows[i]));
            }
        }

        // Return it
        return neighbours;
    }

    public Collection<Cell> getCells(){
        return cellMap.values();
    }

    public HashMap<String, Cell> getCellMap(){
        return cellMap;
    }

    /**
     * Refresh the colors of all cells
     */
    public void refreshCellColors(){
        int cellSize = (int) Main.CELL_SIZE;

        for(int x = 0; x < cellsWidthSize; x++){
            for(int y = 0; y < cellsHeightSize; y++){
                Cell cell = cellMap.get(x*cellSize+" "+y*cellSize);
                cell.getStyleClass().clear();

                // Checks if its alive and change color if needed
                this.addCellStyleclasses(cell);
            }
        }
    }

    public void addCellStyleclasses(Cell c){
        String styleClass = "";
        int cellSize = (int) Main.CELL_SIZE;
        int aliveStatus = getAliveStatus(c.getX()/cellSize, c.getY()/cellSize);

        if(aliveStatus == 0)
            styleClass = Main.DEAD_CELL_STYLE;
        else if(aliveStatus <= Main.YOUNG_AGE)
            styleClass = Main.YOUNG_CELL_STYLE;
        else if(aliveStatus <= Main.ELDER_AGE && aliveStatus > Main.YOUNG_AGE)
            styleClass = Main.ELDER_CELL_STYLE;
        else if(aliveStatus <= Main.OLD_AGE && aliveStatus > Main.ELDER_AGE){
            styleClass = Main.OLD_CELL_STYLE;
        }

        if(grid)
            c.getStyleClass().add("grid");

        c.getStyleClass().add(styleClass);
    }

    public void reset(){
        int cellSize = (int) Main.CELL_SIZE;

        for(int x = 0; x < cellsWidthSize; x++){
            for(int y = 0; y < cellsHeightSize; y++){
                boolean isAlive = isAlive(x,y);
                Cell cell = cellMap.get(x*cellSize+" "+y*cellSize);
                cell.getStyleClass().clear();

                // Checks if its alive and change color if needed
                cell.getStyleClass().add(Main.DEAD_CELL_STYLE);

                this.createPopulation(32.25);
            }
        }
    }

    public boolean calcProbability(double percent){
        double range = (100 - 1) + 1;
        double rNum = (Math.random() * range) + 1;

        return rNum <= percent;
    }

    public void refreshGrid(){
        int cellSize = (int) Main.CELL_SIZE;

        for(int x = 0; x < cellsWidthSize; x++){
            for(int y = 0; y < cellsHeightSize; y++){
                Cell cell = cellMap.get(x*cellSize+" "+y*cellSize);
                cell.getStyleClass().clear();
                addCellStyleclasses(cell);

                if(grid)
                    cell.getStyleClass().add("grid");
            }
        }
    }

    public void updateStats(){
        int day = LabelManager.getDay();
        int year = LabelManager.getYear();
        int population = getLivingCells();

        if(day == 180){
            year++;
            day = 0;

            // New cells
            if(population >= 25 && population < 100)
                celebrateNewYear(getRandomPercent(25));
            else if(population > 100)
                celebrateNewYear(getRandomPercent(50));

        }

        LabelManager.setStatsLabel(population+"", (day+1)+"", year+"");
    }

    public void resetStats(){
        LabelManager.setStatsLabel("0", "0", "0");
    }

    public int getLivingCells(){
        int count = 0;

        for(int x = 0; x < cellsWidthSize; x++){
            for(int y = 0; y < cellsHeightSize; y++){
                if(isAlive(x,y))
                    count++;
            }
        }

        return count;
    }

    /**
     * Creates new cells when new year incoming (celebrating new year)
     * @param density How much (in percent) cells come to celebrate
     */
    public void celebrateNewYear(int density){
        for(int x = 0; x < cellsWidthSize; x++){
            for(int y = 0; y < cellsHeightSize; y++){
                if(!isAlive(x,y)){
                    if(calcProbability(density)){
                        cells[x][y] = getRandomAge();
                    }
                }
            }
        }
    }

    public int getRandomAge(){
        int range = (60 - 1) + 1;
        return (int) ((Math.random() * range) + 1);
    }

    public int getRandomPercent(int max){
        int range = (max - 1) + 1;
        return (int) ((Math.random() * range) + 1);
    }

    /**
     * Adds a cell to the cellmap
     * @param cell The cell
     */
    public void addCell(Cell cell){
        cellMap.put(cell.getX() + " " + cell.getY(), cell);
    }

}

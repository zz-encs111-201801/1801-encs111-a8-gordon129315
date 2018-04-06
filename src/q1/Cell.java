package q1;

import java.util.LinkedList;

public class Cell {
    int value;
    public static final int GROUND = 1;
    public static final int TREE = 2;

    int mark;
    public final static  int markNotVisited = 0;
    public final static  int markWaiting = 1;
    public final static  int markVisited = 2;

    int status;
    public static final int NOT_START_OR_END_POINT = 0;
    public static final int START_POINT = 1;
    public static final int END_POINT = 2;

    Cell from;
    Point point;

    public Cell(Point point, int value) {
        this.point = point;
        this.value = value;
    }

    LinkedList<Cell> adjCells = new LinkedList<>();

    public void addAdjCell(Cell adjCell, boolean inverse) {
        adjCells.add(adjCell);
        if (inverse) {
            adjCell.addAdjCell(this, false);
        }
    }

    @Override
    public String toString() {
        return point.x + "," + point.y;
    }
}

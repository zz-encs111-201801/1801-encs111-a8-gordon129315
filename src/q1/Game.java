package q1;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game {
    Map<Point, Cell> cells = new HashMap<>();

    public Cell getCell(Point point) {
        return cells.get(point);
    }

    int height;
    int length;

    public static Game newGame(int[][] data) {
        Game game = new Game();
        game.height = data.length;
        game.length = data[0].length;
        for (int i = 0; i < game.height; i++) {         //Point : y
            for (int j = 0; j < game.length; j++) {     //Point : x
                if (data[i][j] == 1) {
                    Cell cell = new Cell(new Point(j, i), Cell.TREE);
                    game.cells.put(new Point(j, i), cell);
                }
                else if (data[i][j] == 0) {
                    Cell cell = new Cell(new Point(j, i), Cell.GROUND);
                    game.cells.put(new Point(j, i), cell);
                    //连接上面的结点
                    Cell topCell = game.getCell(new Point(j - 1, i));
                    if (topCell != null && topCell.value == Cell.GROUND) {
                        cell.addAdjCell(topCell, true);
                    }
                    //连接左边的结点
                    Cell leftCell = game.getCell(new Point(j, i - 1));
                    if (leftCell != null && leftCell.value == Cell.GROUND) {
                        cell.addAdjCell(leftCell, true);
                    }
                }
            }
        }
        game.initCells();
        return game;
    }

    //initialize cells
    public void initCells() {
        Collection<Cell> cells = this.cells.values();
        for (Cell cell : cells) {
            if (cell.value == Cell.GROUND) {
                cell.mark = Cell.markNotVisited;
                cell.status = Cell.NOT_START_OR_END_POINT;
                cell.from = null;
            }
        }
    }

    public void print() {
        System.out.print("  ");
        for (int i = 0; i < length; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < height; i++) {         //Point : y
            System.out.print(i + " ");
            for (int j = 0; j < length; j++) {     //Point : x
                Cell cell = getCell(new Point(j, i));
                if (cell.value == Cell.TREE){
                    System.out.print("T ");
                }
                else if (cell.value == Cell.GROUND) {
                    if (cell.status == Cell.START_POINT) {
                        System.out.print("x ");
                    }
                    else if (cell.status == Cell.END_POINT) {
                        System.out.print("v ");
                    }
                    else {
                        if (cell.mark == Cell.markVisited) {
                            System.out.print("o ");
                        }
                        else {
                            System.out.print("_ ");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    //isReachable(from: Point, to: Point) -> boolean
    //检测 两个点 是否可通过 平移抵达
    public boolean isReachable(Point from, Point to) {
        if (getCell(from).value == Cell.TREE || getCell(to).value == Cell.TREE) {
            return false;
        }
        else if (from.x != to.x && from.y != to.y) {         //横纵坐标都不一样，一定false
            return false;
        }
        else if (from.x == to.x && from.y == to.y) {         //起始点同一坐标，一定true
            return true;
        }
        else if (from.x != to.x && from.y == to.y) {         //横坐标不同，纵坐标相同，左右平移
            int min_x = Math.min(from.x, to.x);
            int max_x = Math.max(from.x, to.x);
            for (int j = min_x; j <= max_x; j++) {
                Cell cell = getCell(new Point(j, from.y));
                if (cell.value == Cell.TREE) {
                    return false;
                }
            }
            return true;
        }
        else if (from.x == to.x && from.y != to.y) {         //横坐标相同，纵坐标不同，上下平移
            int min_y = Math.min(from.y, to.y);
            int max_y = Math.max(from.y, to.y);
            for (int i = min_y; i <= max_y; i++) {
                Cell cell = getCell(new Point(from.x, i));
                if (cell.value == Cell.TREE) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    //setRange(origin: Point, range: int)
    //从 origin 开始，标记周围距离它 range 范围的格，
    public void setRange(Point origin, int range) {
        Cell o_cell = getCell(origin);
        o_cell.status = Cell.START_POINT;
        dfs(o_cell,0, range);
    }

    //dfs 深度遍历到range步就返回，不再继续走下去
    private void dfs(Cell origin, int step, int range) {
        origin.mark = Cell.markVisited;
        if (step == range) {
            return;
        }
        for (Cell cell : origin.adjCells) {
            if (cell.mark == Cell.markNotVisited) {
                dfs(cell, step + 1, range);
            }
        }
    }

    //setPath(from: Point, to: Point)
    //找到 从 from 开始 到 to 的 一条最短路径（可能有多条，选中一条就好）
    public void setPath(Point from, Point to) {
        Cell fromCell = getCell(from);
        Cell toCell = getCell(to);
        bfs(fromCell, toCell);                      //广度遍历，一直到找到终点结点为止
        LinkedList<Point> path = new LinkedList<>();
        recordPath(path, fromCell, toCell);         //记录path路径经过的结点的坐标
        initCells();                                //清除所有visited的状态
        for (Point point : path) {
            getCell(point).mark = Cell.markVisited;     //再把路径经过的点设为Visited
        }
        fromCell.status = Cell.START_POINT;
        toCell.status = Cell.END_POINT;

    }

    //setConnected(origin: Point)
    //从 origin 开始，标记所有它能抵达的点
    public void setConnected(Point origin) {
        Cell o_cell = getCell(origin);
        o_cell.status = Cell.START_POINT;
        bfs(o_cell);
    }

    //广度遍历 从from开始的连通组件
    private void bfs(Cell from) {
        bfs(from, null);
    }

    //广度遍历 从from遍历到to即返回
    //若to为null，则遍历从from开始的连通组件
    private void bfs(Cell from, Cell to) {
        LinkedList<Cell> waiting = new LinkedList<>();

        waiting.addLast(from);
        from.mark = Cell.markWaiting;

        while (!waiting.isEmpty()) {
            Cell cell = waiting.removeFirst();
            cell.mark = Cell.markVisited;

            if (cell == to) {
                return;
            }

            for (Cell adjCell : cell.adjCells) {
                if (adjCell.mark == Cell.markNotVisited) {
                    waiting.addLast(adjCell);
                    adjCell.mark = Cell.markWaiting;
                    adjCell.from = cell;     //记录走过路径的指针
                }
            }

        }
    }

    //记录path路径经过的结点的坐标
    private void recordPath(LinkedList<Point> path, Cell fromCell, Cell toCell) {
        Cell cell = toCell;
        while (cell != fromCell) {
            path.add(cell.point);
            cell = cell.from;
        }
    }
}

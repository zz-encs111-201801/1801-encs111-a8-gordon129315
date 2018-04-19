package q1;

public class Driver {
    public static void main(String[] args) {
        new Driver().run();
    }

    public void run() {
        int[][] data = {{0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 1, 1, 0, 0},
                        {0, 0, 0, 0, 1, 0, 0},
                        {1, 1, 0, 0, 1, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0}};

        Game game = Game.newGame(data);
        game.print();
        System.out.println();

        //isReachable
        System.out.println(game.isReachable(new Point(2, 2), new Point(2, 0)));

        System.out.println();
        game.initCells();

        //setRange
        game.setRange(new Point(5, 1), 3);
        game.print();

        System.out.println();
        game.initCells();

        //setPath
        game.setPath(new Point(3, 3), new Point(4, 1));
        game.print();

        System.out.println();
        game.initCells();

        //setConnected
        game.setConnected(new Point(0, 5));
        game.print();

        System.out.println();
        game.initCells();



    }
}

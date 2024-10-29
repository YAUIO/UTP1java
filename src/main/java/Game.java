import java.util.Arrays;

public class Game extends Thread {
    private final BoardMark[][] boardMarks;
    private final int x;
    private final int y;
    private final GUI gui;

    public enum BoardMark {
        Empty,
        Cross,
        Circle,
        WonCross,
        WonCircle
    }

    public Game(int x, int y, int size) {
        boardMarks = new BoardMark[size][size];
        initialize(boardMarks.length);
        gui = new GUI(boardMarks.length, x, y, this);
        this.x = x;
        this.y = y;

        this.start();
    }

    protected native int[][] fetchInternal(int xp, int yp);

    private native void initialize(int size);

    private void updateBoardState(int xp, int yp) {
        int[][] board = fetchInternal(xp, yp);

        int row = 0;
        int col = 0;

        while (row < board.length) {
            col = 0;
            while (col < board[row].length) {
                boardMarks[row][col] = BoardMark.values()[board[row][col]];
                col++;
            }
            row++;
        }

    }

    private int[] getPlate() {
        int xplate = gui.xpk + 1;
        int yplate = gui.ypk + 1;

        if (gui.cX != -1){
            int xp = x / gui.boardSize;
            int yp = y / gui.boardSize;

            xplate = gui.cX / xp + 1;
            yplate = gui.cY / yp + 1;

            gui.cX = -1;
            gui.cY = -1;
        }

        return new int[]{xplate - 1, yplate - 1};
    }

    public void run() {
        try {

            int[] field = {-1, -1};
            while (true) {
                updateBoardState(field[0], field[1]);
                gui.repaintJPanels();
                if (boardMarks[0][0] == BoardMark.WonCross || boardMarks[0][0] == BoardMark.WonCircle){
                    synchronized (this) {
                        this.wait(400);
                    }
                    System.out.println(boardMarks[0][0]);
                    break;
                }
                synchronized (this) {
                    wait();
                }
                field = getPlate();
            }
            
        } catch (Exception e) {
            System.out.println("Exception in game thread: " + e.getMessage());
        }
    }

    ;

    public BoardMark[][] getBoard() {
        return boardMarks;
    }
}

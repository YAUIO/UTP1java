import javax.swing.*;
import java.util.Arrays;

public class Game extends Thread {
    public int[] lastStep;
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

    public static BoardMark toBoardMark(int val){
        return BoardMark.values()[val];
    }

    public Game(){
        GUI.getPreGUI();
        gui = null;
        y = 0;
        x = 0;
    }

    public Game(int x, int y, int size) {
        lastStep = null;
        initialize(size);
        gui = new GUI(size, x, y, this);
        this.x = x;
        this.y = y;

        this.start();
    }

    protected native int[] fetchInternal(int xp, int yp);

    private native void initialize(int size);

    private void updateBoardState(int xp, int yp) {
        lastStep = fetchInternal(xp, yp);
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
                if (toBoardMark(lastStep[0]) == BoardMark.WonCross || toBoardMark(lastStep[0]) == BoardMark.WonCircle){
                    synchronized (this) {
                        this.wait(400);
                    }
                    System.out.println(toBoardMark(lastStep[0]));
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
}

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
        WonCircle,
        Draw
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

    public Game(boolean gui){ //only for testing
        if (gui){
            GUI.getPreGUI();
        }

        this.gui = null;
        y = 0;
        x = 0;
    }

    public Game(int x, int y, int size) {
        lastStep = new int[]{0, 0, 0};
        initialize(size);
        gui = new GUI(size, x, y, this);
        this.x = x;
        this.y = y;

        this.start();
    }

    protected native int getValue(int y, int x);

    protected native int getSize(); //only for testing purposes

    protected native void setValue(int y, int x, int value); //only for testing purposes

    protected native void initialize(int size);

    protected native int[] fetchInternal(int xp, int yp);

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
                if (toBoardMark(lastStep[0]) == BoardMark.WonCross || toBoardMark(lastStep[0]) == BoardMark.WonCircle || toBoardMark(lastStep[0]) == BoardMark.Draw){
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
            
        } catch (InterruptedException e) {
            gui.interrupt();
            System.out.println("Thread interrupted");
        } catch (Exception e) {
            System.out.println("Exception in game thread: " + e.getMessage());
        }

        gui.interrupt();
    }

}

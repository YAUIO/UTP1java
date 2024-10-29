import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI extends Thread {
    private final BoardElement[][] guiBoard;
    private JFrame frame;
    private final int x;
    private final int y;

    private boolean init;
    private boolean initPanel;

    private final Game game;

    final int boardSize;

    int cX;
    int cY;

    int xpk;
    int ypk;

    GUI(int size, int x, int y, Game game) {
        cX = -1;
        cY = -1;

        xpk = 0;
        ypk = 0;

        boardSize = size;
        guiBoard = new BoardElement[boardSize][boardSize];

        this.x = x;
        this.y = y;

        this.game = game;

        this.init = true;
        this.initPanel = true;

        this.start();
    }

    private JFrame getJFrame() {
        JFrame window = new JFrame("HDPR");
        window.setSize(x, y);
        window.setPreferredSize(new Dimension(1280, 720));
        window.setLayout(new GridLayout(boardSize, boardSize));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        window.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        if (ypk>0) ypk--; guiBoard[ypk+1][xpk].eSelected();
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (ypk<boardSize-1) ypk++; guiBoard[ypk-1][xpk].eSelected();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (xpk<boardSize-1) xpk++; guiBoard[ypk][xpk-1].eSelected();
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (xpk>0) xpk--; guiBoard[ypk][xpk+1].eSelected();
                    }
                    case KeyEvent.VK_ENTER -> {
                        guiBoard[ypk][xpk].eSelected();
                        synchronized (game) {
                            game.notify();
                        }
                    }
                    default -> {return;}
                }

                guiBoard[ypk][xpk].selected();

                frame.pack();
                frame.setVisible(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        window.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cX = e.getX();
                cY = e.getY();

                synchronized (game) {
                    game.notify();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        return window;
    }

    public void repaintJPanels() {

        if (init) {
            return;
        }

        int r = 0;
        int c;

        for (BoardElement[] row : guiBoard) {
            c = 0;
            for (BoardElement panel : row) {

                if(initPanel){
                    panel = new BoardElement(x / boardSize, y / boardSize, false, false, false);
                } else {
                    if (game.lastStep[0]!=0 && game.lastStep[1]==r && game.lastStep[2]==c){
                        switch (Game.toBoardMark(game.lastStep[0])){
                            case Game.BoardMark.Cross:
                                if (!panel.isCrossed) {
                                    frame.remove(panel);
                                    panel = new BoardElement(x / boardSize, y / boardSize, true, true, false);
                                }
                                break;
                            case Game.BoardMark.Circle :
                                if (!panel.isCrossed) {
                                    frame.remove(panel);
                                    panel = new BoardElement(x / boardSize, y / boardSize, true, false, false);
                                }
                                break;
                        }
                    }
                }

                panel.setVisible(true);
                guiBoard[r][c] = panel;
                frame.add(panel);
                c++;
            }
            r++;
        }
        frame.pack();
        frame.setVisible(true);
        initPanel = false;
    }

    @Override
    public void run() {
        frame = getJFrame();
        init = false;
        repaintJPanels();

        while (game.isAlive()) {

        }

        frame.dispose();
    }
}

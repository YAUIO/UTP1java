import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public static void getPreGUI() {
        JFrame window = new JFrame("HDPR");
        window.setSize(400, 400);
        window.setPreferredSize(new Dimension(400, 400));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);

        JPanel buttons = new JPanel(new GridLayout(3, 0, 100, 10));

        Button f = new Button("8");
        Button s = new Button("10");
        Button t = new Button("12");

        f.addActionListener(e -> {
            if (e.getActionCommand().equals("8")) {
                window.dispose();
                new Game(1280, 720, 8);
            }
        });

        s.addActionListener(e -> {
            if (e.getActionCommand().equals("10")) {
                window.dispose();
                new Game(1280, 720, 10);
            }
        });

        t.addActionListener((e -> {
            if (e.getActionCommand().equals("12")) {
                window.dispose();
                new Game(1280, 720, 12);
            }
        }));

        buttons.add(f);
        buttons.add(s);
        buttons.add(t);

        window.add(buttons, BorderLayout.CENTER);

        window.pack();
        window.setVisible(true);
    }

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
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        if (ypk > 0) ypk--;
                        guiBoard[ypk + 1][xpk].eSelected();
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (ypk < boardSize - 1) ypk++;
                        guiBoard[ypk - 1][xpk].eSelected();
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (xpk < boardSize - 1) xpk++;
                        guiBoard[ypk][xpk - 1].eSelected();
                    }
                    case KeyEvent.VK_LEFT -> {
                        if (xpk > 0) xpk--;
                        guiBoard[ypk][xpk + 1].eSelected();
                    }
                    case KeyEvent.VK_ENTER -> {
                        guiBoard[ypk][xpk].eSelected();
                        synchronized (game) {
                            game.notify();
                        }
                    }
                    default -> {
                        return;
                    }
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

                guiBoard[ypk][xpk].eSelected();

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

        if (initPanel) {
            int r = 0;
            int c;
            for (BoardElement[] row : guiBoard) {
                c = 0;
                for (BoardElement panel : row) {
                    panel = new BoardElement(x / boardSize, y / boardSize, c, r, false, game);
                    panel.setVisible(true);
                    frame.add(panel);
                    guiBoard[r][c] = panel;
                    c++;
                }
                r++;
            }
        } else {
            BoardElement panel = guiBoard[game.lastStep[1]][game.lastStep[2]];
            frame.remove(panel);
            panel = new BoardElement(x / boardSize, y / boardSize, game.lastStep[2], game.lastStep[1], false, game);
            guiBoard[game.lastStep[1]][game.lastStep[2]] = panel;
            for (BoardElement[] row : guiBoard) {
                for (BoardElement panelIter : row) {
                    frame.remove(panelIter);
                    frame.add(panelIter);
                }
            }
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

        while (game.isAlive());

        frame.dispose();
    }
}

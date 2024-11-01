import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @BeforeAll
    public static void systemSet() {
        System.setProperty("java.library.path", "-Djava.library.path=/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/");

        System.load("/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/libUTP1.so");
    }

    @BeforeEach
    public void waitE() throws InterruptedException {
        synchronized (Thread.currentThread()){
            Thread.currentThread().wait(1000);
        }
    }

    public static void printBoard(int size, Game game) {
        for (int a = 0; a < size; a++) {
            for (int b = 0; b < size; b++) {
                System.out.print(game.getValue(a, b) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Test
    public void testInitialize() {


        for (int size = 8; size <= 12; size += 2) {

            Game game = new Game(false);

            game.initialize(size);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    assertEquals(0, game.getValue(x, y));
                }
            }

            assertEquals(size, game.getSize());

            try {
                game.interrupt();
                game.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testRow() {
        for (Game.BoardMark element = Game.BoardMark.Cross, win = Game.BoardMark.WonCross; element.ordinal() <= Game.BoardMark.Circle.ordinal(); element = Game.BoardMark.values()[element.ordinal() + 1], win = Game.BoardMark.values()[win.ordinal() + 1]) {
            for (int size = 8; size <= 12; size += 2) {
                for (int x = 0; x < size - 4; x++) {
                    for (int y = 0; y < size; y++) {
                        Game game = new Game(false);
                        game.initialize(size);

                        for (int c = 0; c < 4; c++) {
                            game.setValue(y, x + c, element.ordinal());
                        }


                        if (element == Game.BoardMark.Circle) {
                            game.fetchInternal(size - x - 1, size - y - 1);
                        }

                        Assertions.assertEquals(win.ordinal(), game.fetchInternal(x + 4, y)[0]);

                        try {
                            game.interrupt();
                            game.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testColumn() {
        for (Game.BoardMark element = Game.BoardMark.Cross, win = Game.BoardMark.WonCross; element.ordinal() <= Game.BoardMark.Circle.ordinal(); element = Game.BoardMark.values()[element.ordinal() + 1], win = Game.BoardMark.values()[win.ordinal() + 1]) {
            for (int size = 8; size <= 12; size += 2) {
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size - 4; y++) {
                        Game game = new Game(false);
                        game.initialize(size);

                        for (int c = 0; c < 4; c++) {
                            game.setValue(y + c, x, element.ordinal());
                        }

                        if (element == Game.BoardMark.Circle) {
                            game.fetchInternal(size - x - 1, size - y - 1);
                        }

                        Assertions.assertEquals(win.ordinal(), game.fetchInternal(x, y + 4)[0]);

                        try {
                            game.interrupt();
                            game.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testDiag() {
        for (Game.BoardMark element = Game.BoardMark.Cross, win = Game.BoardMark.WonCross; element.ordinal() <= Game.BoardMark.Circle.ordinal(); element = Game.BoardMark.values()[element.ordinal() + 1], win = Game.BoardMark.values()[win.ordinal() + 1]) {
            for (int size = 8; size <= 12; size += 2) {
                for (int x = 0; x < size - 4; x++) {
                    for (int y = 0; y < size - 4; y++) {
                        Game game = new Game(false);
                        game.initialize(size);

                        for (int c = 0; c < 4; c++) {
                            game.setValue(y + c, x + c, element.ordinal());
                        }

                        if (element == Game.BoardMark.Circle) {
                            if (x != 0 && y != 0) {
                                game.fetchInternal(0, 0);
                            } else {
                                game.fetchInternal(size - 1, size - 1);
                            }
                        }

                        Assertions.assertEquals(win.ordinal(), game.fetchInternal(x + 4, y + 4)[0]);

                        try {
                            game.interrupt();
                            game.join();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void checkMouseInput() throws AWTException {
        final int screenX = (1920 - 1280) / 2;
        final int screenY = (1080 - 720) / 2;

        Robot robot = new Robot();
        int downMask = MouseEvent.BUTTON1_DOWN_MASK;
        for (int size = 8; size <= 12; size += 2) {
            int sqSizeX = 1280 / size;
            int sqSizeY = 720 / size;

            int oX = 0;
            int oY = 0;

            for (; oX < 20; oX++) {
                if (robot.getPixelColor(oX + screenX, 10 + screenY) == Color.BLACK) {
                    break;
                }
            }

            for (; oY < 20; oY++) {
                if (robot.getPixelColor(10 + screenX, oY + screenY) == Color.BLACK) {
                    break;
                }
            }


            int lastX = 0;
            int lastY = 0;
            for (int move = 0; move < size * size; move++) {
                Game game = new Game(1280, 720, size);
                try {
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(100);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int y = lastY; y < size; y++) {
                    if (game.lastStep[0] < 3) {
                        for (int x = lastX; x < size; x++) {
                            lastX = 0;
                            if (game.lastStep[0] < 3) {
                                robot.mouseMove(x * sqSizeX + screenX + oX, y * sqSizeY + screenY + oY);
                                robot.mousePress(downMask);
                                robot.mouseRelease(downMask);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Assertions.assertEquals(Game.BoardMark.values()[move % 2 + 1].ordinal(), game.getValue(y, x));
                                move++;
                            } else {
                                lastX = x;
                                lastY = y;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                try {
                    game.interrupt();
                    game.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void checkMouseInputDoubleClick() throws AWTException {
        final int screenX = (1920 - 1280) / 2;
        final int screenY = (1080 - 720) / 2;

        Robot robot = new Robot();
        int downMask = MouseEvent.BUTTON1_DOWN_MASK;
        for (int size = 8; size <= 12; size += 2) {
            int sqSizeX = 1280 / size;
            int sqSizeY = 720 / size;

            int oX = 0;
            int oY = 0;

            for (; oX < 20; oX++) {
                if (robot.getPixelColor(oX + screenX, 10 + screenY) == Color.BLACK) {
                    break;
                }
            }

            for (; oY < 20; oY++) {
                if (robot.getPixelColor(10 + screenX, oY + screenY) == Color.BLACK) {
                    break;
                }
            }


            int lastX = 0;
            int lastY = 0;
            for (int move = 0; move < size * size; move++) {
                Game game = new Game(1280, 720, size);
                try {
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(100);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                for (int y = lastY; y < size; y++) {
                    if (game.lastStep[0] < 3) {
                        for (int x = lastX; x < size; x++) {
                            lastX = 0;
                            if (game.lastStep[0] < 3) {
                                robot.mouseMove(x * sqSizeX + screenX + oX, y * sqSizeY + screenY + oY);
                                robot.mousePress(downMask);
                                robot.mouseRelease(downMask);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                robot.mousePress(downMask);
                                robot.mouseRelease(downMask);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Assertions.assertEquals(Game.BoardMark.values()[move % 2 + 1].ordinal(), game.getValue(y, x));
                                move++;
                            } else {
                                lastX = x;
                                lastY = y;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                try {
                    game.interrupt();
                    game.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void checkKeyboardInput() throws AWTException {
        Robot robot = new Robot();
        int right = KeyEvent.VK_RIGHT;
        int left = KeyEvent.VK_LEFT;
        int down = KeyEvent.VK_DOWN;
        int enter = KeyEvent.VK_ENTER;

        for (int size = 8; size <= 12; size += 2) {
            int lastX = 0;
            int lastY = 0;
            for (int move = 0; move < size * size; move++) {
                Game game = new Game(1280, 720, size);
                try {
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(200);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (lastX != 0 || lastY != 0){
                    for (int c = 0; c < lastX; c++) {
                        robot.keyPress(right);
                        robot.keyRelease(right);
                        try {
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait(20);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    for (int c = 0; c < lastY; c++) {
                        robot.keyPress(down);
                        robot.keyRelease(down);
                        try {
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait(20);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                for (int y = lastY; y < size; y++) {
                    if (game.lastStep[0] < 3) {
                        for (int x = lastX; x < size; x++) {
                            lastX = 0;
                            if (game.lastStep[0] < 3) {
                                robot.keyPress(enter);
                                robot.keyRelease(enter);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Assertions.assertEquals(Game.BoardMark.values()[move % 2 + 1].ordinal(), game.getValue(y, x));
                                move++;
                                robot.keyPress(right);
                                robot.keyRelease(right);
                            } else {
                                lastX = x;
                                lastY = y;
                                break;
                            }
                        }
                        if (lastX == 0){
                            robot.keyPress(down);
                            robot.keyRelease(down);

                            for (int c = 0; c < size; c++) {
                                robot.keyPress(left);
                                robot.keyRelease(left);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }

                try {
                    game.interrupt();
                    game.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    public void checkKeyboardInputDoubleClick() throws AWTException {
        Robot robot = new Robot();
        int right = KeyEvent.VK_RIGHT;
        int left = KeyEvent.VK_LEFT;
        int down = KeyEvent.VK_DOWN;
        int enter = KeyEvent.VK_ENTER;

        for (int size = 8; size <= 12; size += 2) {
            int lastX = 0;
            int lastY = 0;
            for (int move = 0; move < size * size; move++) {
                Game game = new Game(1280, 720, size);
                try {
                    synchronized (Thread.currentThread()) {
                        Thread.currentThread().wait(100);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (lastX != 0 || lastY != 0){
                    for (int c = 0; c < lastX; c++) {
                        robot.keyPress(right);
                        robot.keyRelease(right);
                        try {
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait(20);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    for (int c = 0; c < lastY; c++) {
                        robot.keyPress(down);
                        robot.keyRelease(down);
                        try {
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait(20);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                for (int y = lastY; y < size; y++) {
                    if (game.lastStep[0] < 3) {
                        for (int x = lastX; x < size; x++) {
                            lastX = 0;
                            if (game.lastStep[0] < 3) {
                                robot.keyPress(enter);
                                robot.keyRelease(enter);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                robot.keyPress(enter);
                                robot.keyRelease(enter);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                Assertions.assertEquals(Game.BoardMark.values()[move % 2 + 1].ordinal(), game.getValue(y, x));
                                move++;
                                robot.keyPress(right);
                                robot.keyRelease(right);
                            } else {
                                lastX = x;
                                lastY = y;
                                break;
                            }
                        }
                        if (lastX == 0){
                            robot.keyPress(down);
                            robot.keyRelease(down);

                            for (int c = 0; c < size; c++) {
                                robot.keyPress(left);
                                robot.keyRelease(left);
                                try {
                                    synchronized (Thread.currentThread()) {
                                        Thread.currentThread().wait(20);
                                    }
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }

                try {
                    game.interrupt();
                    game.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
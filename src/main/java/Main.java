import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.setProperty("java.library.path","-Djava.library.path=/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/");

        System.load("/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/libUTP1.so");

        Game game = new Game(1280,720, 10);
    }
}
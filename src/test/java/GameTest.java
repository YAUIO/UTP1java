import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest{

    @BeforeAll
    public static void systemSet(){
        System.setProperty("java.library.path","-Djava.library.path=/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/");

        System.load("/home/yudek/Documents/CLionProjects/UTP1/cmake-build-debug/libUTP1.so");
    }

    @Test
    public void testFetch(){

    }
}
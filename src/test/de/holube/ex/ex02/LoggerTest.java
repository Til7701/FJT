package de.holube.ex.ex02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the non-static part of the {@link Logger} class.
 *
 * @author Tilman Holube
 */
class LoggerTest {

    @BeforeEach
    void setUp() {
        Logger.getLogger().clear();
    }

    @Test
    void getMessagesTest() {
        assertNotNull(Logger.getLogger().getMessages());
        assertEquals(Logger.getLogger().getCapacity(), Logger.getLogger().getMessages().length);
        assertNull(Logger.getLogger().getMessages()[0]);
        assertNull(Logger.getLogger().getMessages()[1]);

        Logger.getLogger().log("getMessagesTest");

        assertEquals(Logger.getLogger().getCapacity(), Logger.getLogger().getMessages().length);
        assertTrue(Logger.getLogger().getMessages()[0].endsWith("getMessagesTest"));
        assertNull(Logger.getLogger().getMessages()[1]);
    }

    @Test
    void logTest() throws InterruptedException {
        LogThread t1 = new LogThread("logTest");
        LogThread t2 = new LogThread("LogTest42");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Logger.getLogger().printSynchronized();

        assertEquals(LogThread.N,
                Arrays.stream(Logger.getLogger().getMessages()).filter(m -> m != null && m.endsWith("logTest")).count()
        );
        assertEquals(LogThread.N,
                Arrays.stream(Logger.getLogger().getMessages()).filter(m -> m != null && m.endsWith("LogTest42")).count()
        );
    }

    @Test
    void logTestNull() throws InterruptedException {
        Logger.getLogger().log(null);

        Logger.getLogger().printSynchronized();

        assertEquals(1,
                Arrays.stream(Logger.getLogger().getMessages()).filter(m -> m != null && m.endsWith("null")).count()
        );
    }

    @Test
    void clearTest() {
        Logger.getLogger().log("clearTest");
        Logger.getLogger().clear();
        Logger.getLogger().printAndContinue();
        assertNull(Logger.getLogger().getMessages()[0]);
    }

    @Test
    void getCapacityTest() throws InterruptedException {
        assertEquals(Logger.getLogger().getCapacity(), Logger.getLogger().getMessages().length);

        LogThread t1 = new LogThread("getCapacityTest");

        t1.start();
        t1.join();

        assertEquals(Logger.getLogger().getCapacity(), Logger.getLogger().getMessages().length);
    }

    @Test
    void printAndContinueTest() throws InterruptedException {
        LogThreadNot t1 = new LogThreadNot("printAndContinueTest");
        LogThreadNot t2 = new LogThreadNot("printAndContinueTest2");

        t1.start();
        t2.start();

        Thread.sleep(20);
        Logger.getLogger().printAndContinue();

        t1.join();
        t2.join();

        /*
        This could be printed:
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        BEGIN LOG PRINT
        2022-05-07T13:39:56.422189900: printAndContinueTest
        2022-05-07T13:39:56.429172: printAndContinueTest2
        2022-05-07T13:39:56.431167400: printAndContinueTest
        2022-05-07T13:39:56.432164: printAndContinueTest2
        2022-05-07T13:39:56.433162200: printAndContinueTest
        2022-05-07T13:39:56.434157800: printAndContinueTest2
        logging printAndContinueTest
        2022-05-07T13:39:56.435155400: printAndContinueTest
        2022-05-07T13:39:56.436153: printAndContinueTest2
        2022-05-07T13:39:56.437150400: printAndContinueTest
        2022-05-07T13:39:56.438147500: printAndContinueTest2
        END LOG PRINT
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest
        logging printAndContinueTest2
        logging printAndContinueTest2
         */
    }

    @Test
    void printSynchronizedTest() throws InterruptedException {
        LogThreadNot t1 = new LogThreadNot("printSynchronizedTest");
        LogThreadNot t2 = new LogThreadNot("printSynchronizedTest2");

        t1.start();
        t2.start();

        Thread.sleep(20);
        Logger.getLogger().printSynchronized();

        t1.join();
        t2.join();

        /*
        Now there should be no lines saying logging while the printSynchronized is running
         */
    }

    static class LogThread extends Thread {
        public static final int N = 10;

        private final String message;

        public LogThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                Logger.getLogger().log(message);
            }
        }
    }

    static class LogThreadNot extends Thread {
        public static final int N = 10;

        private final String message;

        public LogThreadNot(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                Logger.getLogger().log(message);
                System.out.printf("logging %s \n", message);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt();
                }
            }
        }
    }
}

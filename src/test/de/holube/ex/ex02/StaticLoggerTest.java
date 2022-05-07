package de.holube.ex.ex02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the static part of the {@link Logger} class.
 *
 * @author Tilman Holube
 */
class StaticLoggerTest {

    @Test
    void getLoggerTest() throws InterruptedException {
        class GetLoggerThread extends Thread {
            private Logger logger;

            @Override
            public void run() {
                logger = Logger.getLogger();
            }

            public Logger getLogger() {
                return logger;
            }
        }

        GetLoggerThread t1 = new GetLoggerThread();
        GetLoggerThread t2 = new GetLoggerThread();

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        Logger logger1 = t1.getLogger();
        Logger logger2 = t2.getLogger();

        assertEquals(logger1, logger2);
    }

}

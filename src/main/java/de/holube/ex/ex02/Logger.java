package de.holube.ex.ex02;

/**
 * Thread-safe logger.
 *
 * @author DIBO, Tilman Holube
 */
public class Logger {

    private volatile static Logger logger = null;

    private String[] messages = null;
    private int index = 0;

    private Logger(int size) {
        messages = new String[size];
    }

    /**
     * Returns the singleton instance of the logger. If the instance does not exist, it is created with an initial
     * capacity of 2.
     *
     * @return the logger
     */
    public static Logger getLogger() {
        Logger tmpLogger = logger;
        if (tmpLogger == null) {
            synchronized (Logger.class) {
                if (logger == null) {
                    logger = new Logger(2);
                    return logger;
                }
            }
        }
        return tmpLogger;
    }

    /**
     * Getter for the messages array. This is only for testing purposes.
     *
     * @return the messages array
     */
    synchronized String[] getMessages() {
        return messages;
    }

    /**
     * Adds a message to the logger.
     *
     * @param message the message to add
     */
    public synchronized void log(String message) {
        checkCapacity();
        messages[index++] = java.time.LocalDateTime.now() + ": " + message;
    }

    /**
     * Clears the logger.
     */
    public synchronized void clear() {
        index = 0;
        messages = new String[2];
    }

    /**
     * Returns the current capacity of the logger.
     *
     * @return the capacity
     */
    public synchronized int getCapacity() {
        return messages.length;
    }

    /**
     * This method prints the messages in the logger to the console. It copies the messages to a new array and then
     * prints the array. If there is a new message added to the logger while the method is printing to the console, the
     * new message will not be printed.
     */
    public void printAndContinue() {
        String[] messagesToPrint;
        int indexToPrint;

        synchronized (this) {
            indexToPrint = index;
            messagesToPrint = new String[indexToPrint];
            System.arraycopy(messages, 0, messagesToPrint, 0, indexToPrint);
        }

        System.out.println("BEGIN LOG PRINT");
        for (int i = 0; i < indexToPrint; i++) {
            System.out.println(messagesToPrint[i]);
        }
        System.out.println("END LOG PRINT");
    }

    /**
     * This method prints the messages in the logger to the console. If there is a new message added to the logger while
     * the method is running, the new message will not be printed. The Thread adding the message has to wait until this
     * method is finished. Then the message will be added to the logger.
     */
    public synchronized void printSynchronized() {
        System.out.println("BEGIN LOG PRINT");
        for (int i = 0; i < index; i++) {
            System.out.println(messages[i]);
        }
        System.out.println("END LOG PRINT");
    }

    /**
     * This method checks, if the capacity of the logger is reached. If it is, the capacity is doubled. This is
     * synchronized to make sure, that further usages outside the log method are supported. It technically does not
     * need to be synchronized.
     */
    private synchronized void checkCapacity() {
        if (index == messages.length) {
            String[] newMessages = new String[messages.length * 2];
            System.arraycopy(messages, 0, newMessages, 0, messages.length);
            messages = newMessages;
        }
    }

}


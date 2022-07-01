package de.holube.ex.ex09;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.*;

class Alice extends Thread {

    private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    private final CryptoAG cryptoAG;

    public Alice(CryptoAG cryptoAG) {
        this.cryptoAG = cryptoAG;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            try {
                String message = messages.take();
                System.out.println("Received: " + message);
                System.out.println(cryptoAG.decrypt(message));
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public void send(String message) {
        try {
            messages.put(message);
        } catch (InterruptedException e) {
        }
    }

}

class Bob extends Thread {

    private final Alice alice;

    private final CryptoAG cryptoAG;

    public Bob(String name, Alice alice, CryptoAG cryptoAG) {
        super(name);
        this.alice = alice;
        this.cryptoAG = cryptoAG;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            String message = "Greetings from " + getName() + "!";
            String encrypted = cryptoAG.encrypt(message);
            System.out.println("Sending: " + encrypted);
            alice.send(encrypted);
            try {
                Thread.sleep(ThreadLocalRandom.current().nextLong(1000));
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}

class CryptoAG {

    private static final String ALGORITHM = "RSA";
    private final KeyPair pair;

    private final ExecutorService executorService;

    public CryptoAG() throws NoSuchAlgorithmException {
        executorService = Executors.newFixedThreadPool(5);

        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
        generator.initialize(2048);
        pair = generator.generateKeyPair();
    }

    public String encrypt(String string) {
        return getResultUninterruptibly(() -> encryptImpl(string));
    }

    public String decrypt(String string) {
        return getResultUninterruptibly(() -> decryptImpl(string));
    }

    private String getResultUninterruptibly(Callable<String> callable) {
        while (true) {
            try {
                return executorService.submit(callable).get();
            } catch (InterruptedException e) {
                // ignore
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String encryptImpl(String string) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
        byte[] cipherText = cipher.doFinal(string.getBytes());
        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decryptImpl(String string) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(string));
        return new String(plainText);
    }

}

public class BobAlice {

    public static void main(String[] args) throws Exception {
        CryptoAG cryptoAG = new CryptoAG();
        Alice alice = new Alice(cryptoAG);
        alice.start();
        for (int i = 1; i < 100; i++) {
            Thread bob = new Bob("Bob-" + i, alice, cryptoAG);
            bob.start();
            Thread.sleep(ThreadLocalRandom.current().nextLong(100));
        }

    }

}
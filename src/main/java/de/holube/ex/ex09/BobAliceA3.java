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

class AliceA3 extends Thread {

    private final BlockingQueue<String> messages = new LinkedBlockingQueue<>();

    private final CryptoAG cryptoAG;

    public AliceA3(CryptoAG cryptoAG) {
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

class BobA3 extends Thread {

    private final AliceA3 alice;

    private final CryptoAG cryptoAG;

    public BobA3(String name, AliceA3 alice, CryptoAG cryptoAG) {
        super(name);
        this.alice = alice;
        this.cryptoAG = cryptoAG;
    }

    @Override
    public void run() {
        while (!interrupted()) {
            String message = "Greetings from " + getName() + "!";
            alice.send(cryptoAG.encrypt(message));
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
        Future<String> future = executorService.submit(callable);
        while (true) {
            try {
                return future.get();
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

public class BobAliceA3 {

    public static void main(String[] args) throws Exception {
        CryptoAG cryptoAG = new CryptoAG();
        AliceA3 alice = new AliceA3(cryptoAG);
        alice.start();
        for (int i = 1; i < 100; i++) {
            Thread bob = new BobA3("Bob-" + i, alice, cryptoAG);
            bob.start();
            Thread.sleep(ThreadLocalRandom.current().nextLong(100));
        }

    }

}
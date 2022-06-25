package de.holube.ex.ex08;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class SchereSteinPapier {

    enum HandZeichen {
        SCHERE, STEIN, PAPIER;

        private Thread spieler;

        static HandZeichen random() {
            HandZeichen zeichen = values()[ThreadLocalRandom.current().nextInt(3)];
            zeichen.spieler = Thread.currentThread();
            return zeichen;
        }

        int schlaegt(HandZeichen other) { // analog zu compare beim Comparator
            if (this == other) {
                return 0;
            }
            return (this == HandZeichen.STEIN && other == HandZeichen.SCHERE
                    || this == HandZeichen.PAPIER && other == HandZeichen.STEIN
                    || this == HandZeichen.SCHERE && other == HandZeichen.PAPIER) ? 1 : -1;
        }

        Thread getSpieler() {
            return spieler;
        }
    }

    public static void main(String[] args) {
        Queue<HandZeichen> handZeichenQueue = new ConcurrentLinkedQueue<>();
        Phaser phaser = new Phaser(3);

        Runnable spieler = () -> {
            while (true) {
                phaser.arriveAndAwaitAdvance();
                handZeichenQueue.add(HandZeichen.random());
                phaser.arriveAndAwaitAdvance();
            }
        };
        Runnable schiedsrichter = () -> {
            while (true) {
                phaser.arriveAndAwaitAdvance();
                System.out.println("Schnick, Schnack, Schnuck");
                phaser.arriveAndAwaitAdvance();
                HandZeichen handZeichen1 = handZeichenQueue.poll();
                HandZeichen handZeichen2 = handZeichenQueue.poll();
                switch (handZeichen1.schlaegt(handZeichen2)) {
                    case 1:
                        System.out.printf("%s gewinnt mit %s, %s verliert mit %s%n", handZeichen1.getSpieler().getName(),
                                handZeichen1, handZeichen2.getSpieler().getName(), handZeichen2);
                        break;
                    case -1:
                        System.out.printf("%s gewinnt mit %s, %s verliert mit %s%n", handZeichen2.getSpieler().getName(),
                                handZeichen2, handZeichen1.getSpieler().getName(), handZeichen1);
                        break;
                    default:
                        System.out.printf("Unentschieden, beide Spieler haben %s gewaehlt%n", handZeichen1);
                        break;
                }
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000));
                } catch (InterruptedException e) {
                }
            }
        };

        Thread spieler1 = new Thread(spieler);
        spieler1.setName("Spieler 1");
        Thread spieler2 = new Thread(spieler);
        spieler2.setName("Spieler 2");
        Thread referee = new Thread(schiedsrichter);
        spieler1.start();
        spieler2.start();
        referee.start();
    }
}

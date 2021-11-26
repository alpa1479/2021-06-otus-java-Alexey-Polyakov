package ru.otus.hw15;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CounterDemo {

    private static final Logger logger = LoggerFactory.getLogger(CounterDemo.class);

    private final Counter counter = new Counter(0);
    private ThreadPriority lastPriority = ThreadPriority.SECOND;

    public static void main(String[] args) {
        CounterDemo demo = new CounterDemo();
        new Thread(() -> demo.calculate(ThreadPriority.FIRST)).start();
        new Thread(() -> demo.calculate(ThreadPriority.SECOND)).start();
    }

    private synchronized void calculate(ThreadPriority priority) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastPriority == priority) {
                    wait();
                }
                lastPriority = priority;
                int value = counter.nextValue();
                logger.info("value = {}", value);
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

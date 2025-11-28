package task2;

public class ProducerConsumerApp {

    private static final int BUFFER_SIZE_1 = 10;
    private static final int BUFFER_SIZE_2 = 5;
    private static final int NUM_PRODUCERS = 5;
    private static final int NUM_CONSUMER_TRANSLATORS = 2;
    private static final int MESSAGES_TO_READ = 100;

    private static final CircularBuffer buffer1 = new CircularBuffer(BUFFER_SIZE_1);
    private static final CircularBuffer buffer2 = new CircularBuffer(BUFFER_SIZE_2);

    private static class Producer extends Thread {
        private final int threadId;
        private int messageCount = 0;

        public Producer(int threadId) {
            this.threadId = threadId;
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    messageCount++;
                    String message = String.format("Thread No %d generated message %d", threadId, messageCount);
                    buffer1.put(message);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static class Translator extends Thread {
        private final int threadId;

        public Translator(int threadId) {
            this.threadId = threadId;
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String consumedMessage = buffer1.get();
                    String translatedMessage = String.format("Thread No %d translated message: [%s]", threadId, consumedMessage);
                    buffer2.put(translatedMessage);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Producer threads...");
        for (int i = 1; i <= NUM_PRODUCERS; i++) {
            new Producer(i).start();
        }

        System.out.println("Starting Translator threads...");
        for (int i = 1; i <= NUM_CONSUMER_TRANSLATORS; i++) {
            new Translator(i).start();
        }

        System.out.println("\nMain thread starting to read " + MESSAGES_TO_READ + " messages from Buffer 2:");
        for (int i = 1; i <= MESSAGES_TO_READ; i++) {
            String message = buffer2.get();
            System.out.printf("READ MESSAGE %d: %s%n", i, message);
        }

        System.out.println("\nSuccessfully read all " + MESSAGES_TO_READ + " messages.");
        System.out.println("Program finished. Daemon threads will now terminate.");
    }
}
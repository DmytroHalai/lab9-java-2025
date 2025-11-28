package task2;

public class CircularBuffer {
    private final String[] buffer;
    private final int capacity;
    private volatile int head = 0;
    private volatile int tail = 0;
    private volatile int count = 0;

    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new String[capacity];
    }

    public synchronized void put(String item) throws InterruptedException {
        while (count == capacity) {
            wait();
        }

        buffer[tail] = item;
        tail = (tail + 1) % capacity;
        count++;

        notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        String item = buffer[head];
        head = (head + 1) % capacity;
        count--;

        notifyAll();

        return item;
    }
}
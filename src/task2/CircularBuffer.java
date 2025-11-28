package task2;

public class CircularBuffer {
    private final String[] buffer;
    private final int capacity;
    private int head = 0;
    private int tail = 0;

    public CircularBuffer(int capacity) {
        this.capacity = capacity + 1;
        this.buffer = new String[this.capacity];
    }

    private boolean isEmpty() {
        return head == tail;
    }

    private boolean isFull() {
        return (tail + 1) % capacity == head;
    }

    public synchronized void put(String item) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        buffer[tail] = item;
        tail = (tail + 1) % capacity;
        notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }
        String item = buffer[head];
        head = (head + 1) % capacity;
        notifyAll();
        return item;
    }
}

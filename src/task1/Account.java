package task1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final int id;
    private volatile int balance;
    private final Lock lock = new ReentrantLock();

    public Account(int id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void withdraw(int amount) {
        if (balance - amount < 0) {
            throw new IllegalArgumentException("Insufficient funds on account " + id);
        }
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}
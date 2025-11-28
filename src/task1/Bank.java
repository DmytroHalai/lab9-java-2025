package task1;

public class Bank {

    public void transfer(Account from, Account to, int amount) {
        if (from == to) {
            return;
        }

        Account firstLock;
        Account secondLock;

        if (from.getId() < to.getId()) {
            firstLock = from;
            secondLock = to;
        } else {
            firstLock = to;
            secondLock = from;
        }

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (from.getBalance() >= amount) {
                    from.withdraw(amount);
                    to.deposit(amount);
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}
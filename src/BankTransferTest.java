import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTransferTest {

    private static final int NUM_ACCOUNTS = 1000;
    private static final int NUM_THREADS = 400;
    private static final int NUM_TRANSFERS_PER_THREAD = 300;
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        List<Account> accounts = createAccounts();

        long initialTotalBalance = calculateTotalBalance(accounts);
        System.out.println("Initial Total Balance: " + initialTotalBalance);

        runTransfers(bank, accounts);

        long finalTotalBalance = calculateTotalBalance(accounts);
        System.out.println("Final Total Balance:   " + finalTotalBalance);

        if (initialTotalBalance == finalTotalBalance) {
            System.out.println("\nTest Passed: Total balance remained consistent.");
        } else {
            System.err.println("\nTest Failed: Balances do not match. Concurrency issue detected!");
        }
    }

    private static List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        int baseAmount = 1000;
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            int initialBalance = baseAmount + RANDOM.nextInt(baseAmount);
            accounts.add(new Account(i, initialBalance));
        }
        return accounts;
    }

    private static long calculateTotalBalance(List<Account> accounts) {
        return accounts.stream().mapToLong(Account::getBalance).sum();
    }

    private static void runTransfers(Bank bank, List<Account> accounts) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        int numTransfers = NUM_THREADS * NUM_TRANSFERS_PER_THREAD;
        AtomicInteger transferCount = new AtomicInteger(0);

        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < NUM_TRANSFERS_PER_THREAD; j++) {
                    Account from = accounts.get(RANDOM.nextInt(NUM_ACCOUNTS));
                    Account to = accounts.get(RANDOM.nextInt(NUM_ACCOUNTS));
                    int amount = 1 + RANDOM.nextInt(50);

                    bank.transfer(from, to, amount);
                    transferCount.incrementAndGet();
                }
            });
        }

        executor.shutdown();
        System.out.println("Waiting for " + numTransfers + " transfers to complete...");
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.err.println("Some tasks did not finish.");
        }
        System.out.println("Transfers completed: " + transferCount.get());
    }
}
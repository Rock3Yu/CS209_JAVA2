package practice.lab7;

import java.util.concurrent.locks.ReentrantLock;

public class AccountLock extends Account{

    final private ReentrantLock lock = new ReentrantLock();
    private double balance;

    /**
     * @param money
     */
    public void deposit(double money) {
        lock.lock();
        try {
            double newBalance = balance + money;
            try {
                Thread.sleep(10);   // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
            lock.unlock();
        } catch (Exception e) {
            lock.unlock();
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }
}
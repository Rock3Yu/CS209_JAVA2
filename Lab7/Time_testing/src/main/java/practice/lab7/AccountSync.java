package practice.lab7;

public class AccountSync extends Account {

    private double balance;

    /**
     * @param money
     */

    public synchronized void deposit(double money) {
        try {
            double newBalance = balance + money;
            try {
                Thread.sleep(10);   // Simulating this service takes some processing time
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            balance = newBalance;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getBalance() {
        return balance;
    }
}
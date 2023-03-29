package practice.lab7;

public class DepositThread implements Runnable {

    final private Account account;
    final private double money;


    public DepositThread(Account account, double money) {
        this.account = account;
        this.money = money;
    }

    @Override
    public void run() {
        account.deposit(money);
    }
}

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
  private ConcurrentHashMap<String, Account> accounts;
  private final Random random = new Random();

  public synchronized boolean isFraud()
      throws InterruptedException {
    Thread.sleep(1000);
    return random.nextBoolean();
  }

  public void transfer(String fromAccountNum, String toAccountNum, long amount) {
    Account fromAccount = accounts.get(fromAccountNum);
    Account toAccount = accounts.get(toAccountNum);
    Account lock1;
    Account lock2;
    if (fromAccountNum.hashCode() < toAccountNum.hashCode()) {
      lock1 = fromAccount;
      lock2 = toAccount;
    } else {
      lock1 = toAccount;
      lock2 = fromAccount;
    }
    synchronized (lock1) {
      synchronized (lock2) {
        if (!fromAccount.isLocked() && !toAccount.isLocked()) {
          fromAccount.setMoney(fromAccount.getMoney() - amount);
          toAccount.setMoney(toAccount.getMoney() + amount);
          if (amount > 50000) {
            boolean isFraud;
            try {
              isFraud = isFraud();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              return;
            }
            if (isFraud) {
              fromAccount.setLocked(true);
              toAccount.setLocked(true);
            }
          }
        }
      }
    }
  }

  public long getBalance(String accountNum) {
    return accounts.get(accountNum).getMoney();
  }

  public synchronized void setAccounts(Map<String, Account> accounts) {
    this.accounts = new ConcurrentHashMap<>(accounts);
  }
}

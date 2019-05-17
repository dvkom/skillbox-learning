import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BankTest {

  @Test
  public void concurrent_transfer_will_not_change_accounts_balance() {
    Bank bank = createBankWithAccounts();
    List<Long> expected = Stream
        .generate(() -> 500_000L)
        .limit(10)
        .collect(Collectors.toList());
    // Результат симметричных переводов = 0, если нет race condition
    ExecutorService executor = Executors.newFixedThreadPool(100);
    for (int i = 0; i < 100_000; i++) {
      for (int j = 1; j <= 10; j++) {
        final int f = j;
        executor.execute(() ->
            bank.transfer(String.valueOf(f), String.valueOf(11 - f), 2_000L));
      }
    }
    executor.shutdown();

    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    List<Long> actual = IntStream.rangeClosed(1, 10)
        .boxed()
        .map(String::valueOf)
        .map(bank::getBalance)
        .collect(Collectors.toList());

    assertThat(actual, is(expected));
  }

  @Test
  public void will_not_be_transferred_between_locked_accounts() {
    Bank bank = createBankWithLockedAccounts();
    bank.transfer(String.valueOf(1), String.valueOf(2), 60_000L);
    bank.transfer(String.valueOf(2), String.valueOf(1), 20_000L);
    assertThat(
        new Long[]{bank.getBalance("1"), bank.getBalance("2")},
        is(new Long[]{500_000L, 500_000L})
    );
  }

  @Test
  public void accounts_will_be_locked() {
    Bank bank = new Bank();
    HashMap<String, Account> accounts = new HashMap<>();
    accounts.put("1", new Account(500_000L));
    accounts.put("2", new Account(500_000L));
    bank.setAccounts(accounts);

    ExecutorService executor = Executors.newFixedThreadPool(100);
    for (int i = 0; i < 100; i++) {
      executor.execute(() ->
          bank.transfer("1", "2", 60_000L));
      executor.execute(() ->
          bank.transfer("2", "1", 60_000L));
    }
    executor.shutdown();

    try {
      if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException ex) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    long lockedCount = accounts.values().stream().filter(Account::isLocked).count();
    assertTrue(lockedCount > 1);
  }

  private Bank createBankWithAccounts() {
    Bank bank = new Bank();
    HashMap<String, Account> accounts = IntStream.rangeClosed(1, 10)
        .boxed()
        .map(String::valueOf)
        .collect(Collectors.toMap(Function.identity(), s -> new Account(500_000L),
            (e1, e2) -> e1, HashMap::new));
    bank.setAccounts(accounts);
    return bank;
  }

  private Bank createBankWithLockedAccounts() {
    Bank bank = new Bank();
    HashMap<String, Account> accounts = new HashMap<>();
    accounts.put("1", new Account(500_000L));
    accounts.put("2", new Account(500_000L));
    accounts.get("1").setLocked(true);
    accounts.get("2").setLocked(true);
    bank.setAccounts(accounts);
    return bank;
  }
}

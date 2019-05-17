public class Account {
  private long money;
  private boolean isLocked;

  public Account(long money) {
    this.money = money;
  }

  public long getMoney() {
    return money;
  }

  public void setMoney(long money) {
    this.money = money;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }
}

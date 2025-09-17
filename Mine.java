public class Mina {
  // Resources
  private int stone;
  private int iron;
  private int gold;

  private int workers = 0;

  public Mina() {
    stone = (int) Math.random() * 300;
    iron = (int) Math.random() * 250;
    gold = (int) Math.random() * 100;
  }

  public int send_workers(int amount) {
    if ((workers + amount) > 100) return -1;
    workers += amount;
    return workers;
  }
}

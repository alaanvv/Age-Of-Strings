package modelo;

import java.util.ArrayList;
import persistencia.BancoDeDados;

public class Empire extends Entidade {
  private int population = 30;
  private int food = 100;
  private int workers = 0;
  private int iron = 100;
  private int gold = 100;
  private int wood = 50;
  private String name;

  private ArrayList<Army> armies = new ArrayList<>();
  private ArrayList<Farm> farms = new ArrayList<>();
  private ArrayList<Mine> mines = new ArrayList<>();
  private Lumber lumber;

  private BancoDeDados db;

  public Empire(BancoDeDados db, String name) {
    super(db.nextEmpire());
    this.name = name;
    this.db = db;
    lumber = new Lumber(super.getId(), db);
    db.getLumbers().insert(lumber);
  }

  public ArrayList<Army> getArmies() { return armies; }
  public ArrayList<Farm> getFarms() { return farms; }
  public ArrayList<Mine> getMines() { return mines; }
  public Lumber getLumber() { return lumber; }
  public String getName() { return name; }
  public int getPopulation() { return population; }
  public int getWorkers() { return workers; }
  public int getFood() { return food; }
  public int getIron() { return iron; }
  public int getGold() { return gold; }

  public void setPopulation(int population) { this.population = population; }
  public void setWorkers(int workers) { this.workers = workers; }
  public void setName(String name) { this.name = name; }
  public void setFood(int food) { this.food = food; }
  public void setIron(int iron) { this.iron = iron; }
  public void setGold(int gold) { this.gold = gold; }

  public void addPopulation(int amount) { population += amount; }

  public boolean hasFarm() { return !farms.isEmpty(); }
  public boolean hasMine() { return !mines.isEmpty(); }
  public boolean hasArmy() { return !armies.isEmpty(); }

  // ---

  public void destroy() {
    lumber.destroy();
    for (Army a : armies) a.destroy();
    for (Farm f : farms) f.destroy();
    for (Mine m : mines) m.destroy();
    db.getEmpires().remove(super.getId());
  }

  public boolean buildHouse() {
    if (wood < 5 || gold < 5) return false;
    wood -= 5;
    gold -= 5;

    population += 3;
    return true;
  }

  public boolean buildFarm() {
    if (wood < 5 || gold < 2) return false;
    wood -= 5;
    gold -= 2;

    Farm farm = new Farm(super.getId(), db);
    db.getFarms().insert(farm);
    farms.add(farm);
    return true;
  }

  public boolean buildMine() {
    if (wood < 15 || gold < 5) return false;
    wood -= 15;
    gold -= 5;

    Mine mine = new Mine(super.getId(), db);
    db.getMines().insert(mine);
    mines.add(mine);
    return true;
  }

  public boolean createArmy() {
    if (gold < 20 || iron < 50) return false;
    iron -= 50;
    gold -= 20;

    Army army = new Army(super.getId(), db);
    db.getArmies().insert(army);
    armies.add(army);
    return true;
  }

  // ---

  public int sendWorkersToLumber(int amount) {
    amount = lumber.sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int sendWorkersToFarm(int amount, int id) {
    amount = ((Farm) (db.getFarms().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int sendWorkersToMine(int amount, int id) {
    amount = ((Mine) (db.getMines().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int sendWorkersToArmy(int amount, int id) {
    amount = ((Army) (db.getArmies().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  // ---

  public int takeWorkersFromLumber(int amount) {
    int taken = ((Lumber) (db.getLumbers().findById(super.getId()))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int takeWorkersFromFarm(int amount, int id) {
    int taken = ((Farm) (db.getFarms().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int takeWorkersFromMine(int amount, int id) {
    int taken = ((Mine) (db.getMines().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int takeWorkersFromArmy(int amount, int id) {
    int taken = ((Army) (db.getArmies().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  // ---

  public String runTurn() {
    StringBuilder output = new StringBuilder();

    // Extraindo recursos
    wood += lumber.extractWood();

    for (Farm farm : farms)
      food += farm.extractFood();

    for (Mine mine : mines) {
      iron += mine.extractIron();
      gold += mine.extractGold();
    }

    // Consumindo pontos de comida
    int consumed = population + workers;
    food -= consumed;
    if (food < 0) {
      int dead = -food;
      float perc = (float) dead / (population + workers);
      output.append(String.format("! Imperio #%d: %d pessoas morreram de fome.\n", super.getId(), dead));
      food = 0;

      int removed = lumber.takeWorkers((int) Math.ceil(lumber.getWorkers() * perc));
      workers -= removed;
      dead -= removed;

      for (Army army : armies) {
        if (dead <= 0) break;
        removed = army.takeWorkers((int) Math.ceil(army.getWorkers() * perc));
        workers -= removed;
        dead -= removed;
      }

      for (Mine mine : mines) {
        if (dead <= 0) break;
        removed = mine.takeWorkers((int) Math.ceil(mine.getWorkers() * perc));
        workers -= removed;
        dead -= removed;
      }

      for (Farm farm : farms) {
        if (dead <= 0) break;
        removed = farm.takeWorkers((int) Math.ceil(farm.getWorkers() * perc));
        workers -= removed;
        dead -= removed;
      }

      if (dead > 0)
        population -= Math.min(population, dead);

      if (population < 0) population = 0;
      if (workers < 0) workers = 0;
    }

    return output.toString();
  }

  // ---

  @Override
  public String toString() {
    return String.format("Imperio %s #%d | Populacao: %d; Trabalhadores: %d; Comida: %d; Madeira: %d; Ferro: %d; Ouro: %d", name, super.getId(), population, workers, food, wood, iron, gold);
  }
}

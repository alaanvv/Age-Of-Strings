package modelo;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JOptionPane;

/**
 * Core aggregate representing a player's realm.
 * Holds resources, population, worker allocations, and owned structures
 * (farms, mines, lumber) plus armies. Provides turn resolution logic and
 * helper methods to build or manage dependents.
 */
public class Empire extends Entidade {
  private int population = 30;
  private int food = 100;
  private int workers = 0;
  private int iron = 100;
  private int gold = 100;
  private int wood = 50;
  private String name;
  private int battleCount = 0;

  private final Map<Integer, Army> armies = new TreeMap<>();
  private final Map<Integer, Farm> farms = new TreeMap<>();
  private final Map<Integer, Mine> mines = new TreeMap<>();
  private final Lumber lumber;

  public Empire(String name, int id) {
    super(id);
    this.name = name;
    lumber = new Lumber(id, this);
  }

  public Map<Integer, Army> getArmies() { return armies; }
  public Map<Integer, Farm> getFarms() { return farms; }
  public Map<Integer, Mine> getMines() { return mines; }
  public Lumber getLumber() { return lumber; }
  public String getName() { return name; }
  public int getPopulation() { return population; }
  public int getWorkers() { return workers; }
  public int getFood() { return food; }
  public int getIron() { return iron; }
  public int getGold() { return gold; }
  public int getWood() { return wood; }
  public int getBattleCount() { return battleCount; }

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

  public boolean buildHouse() {
    if (wood < 5 || gold < 5) return false;
    wood -= 5;
    gold -= 5;

    population += 3;
    return true;
  }

  public Farm buildFarm(int farmId) {
    if (wood < 5 || gold < 2) return null;
    wood -= 5;
    gold -= 2;

    Farm f = new Farm(farmId, this);
    farms.put(farmId, f);

    return f;
  }

  public Mine buildMine(int id) {
    if (wood < 15 || gold < 5) return null;
    wood -= 15;
    gold -= 5;

    Mine mine = new Mine(id, this);
    mines.put(id, mine);
    return mine;
  }

  public Army createArmy(int id) {
    if (gold < 20 || iron < 50) return null;
    iron -= 50;
    gold -= 20;

    Army army = new Army(this, id);
    armies.put(id, army);
    return army;
  }

  // ---

  public int sendWorkers(int amount, WorkpostInterface workpost){
    int sent = workpost.sendWorkers(Math.min(amount, population));
    population -= sent;
    workers += sent;
    return sent;
  }

  public int sendWorkersToLumber(int amount) {
    return sendWorkers(amount, lumber);
  }

  public int sendWorkersToFarm(int amount, int id) {
    return sendWorkers(amount, farms.get(id));
  }

  public int sendWorkersToMine(int amount, int id) {
    return sendWorkers(amount, mines.get(id));
  }

  public int sendWorkersToArmy(int amount, int id) {
    return sendWorkers(amount, armies.get(id));
  }

  // ---

  public int takeWorkers(int amount, WorkpostInterface workpost){
    int taken = workpost.takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int takeWorkersFromLumber(int amount) {
    return takeWorkers(amount, lumber);
  }

  public int takeWorkersFromFarm(int amount, int id) {
    return takeWorkers(amount, farms.get(id));
  }

  public int takeWorkersFromMine(int amount, int id) {
    return takeWorkers(amount, mines.get(id));
  }

  public int takeWorkersFromArmy(int amount, int id) {
    return takeWorkers(amount, armies.get(id));
  }

  // ---

  /** This functions serves to determine how much gold is being invested into an army
   * @param armyId The army that will have its funds changed.
   * @param amount The new hiring cost of the army.
   * @return If the change was possible.
   */
  boolean changeArmyHiringFunds(int armyId, int amount){
    Army army = armies.get(armyId);
    int previousHiringCost = army.getHiringCost();
    int previousHiringLevel = army.getHiringLevel();
    int difference = army.changeHiringFunds(amount);
    if(this.gold + difference < 0){
      army.changeHiringFunds(previousHiringCost);
      return false;
    }
    this.gold += difference;
    return army.getHiringLevel() != previousHiringLevel;
  }

  public String runTurn() {
    StringBuilder output = new StringBuilder();

    // Extraindo recursos
    wood += lumber.extractWood();

    for (Farm farm : farms.values())
      food += farm.extractFood();

    for (Mine mine : mines.values()) {
      iron += mine.extractIron();
      gold += mine.extractGold();
    }

    armies.forEach((key, army) -> army.timeUpdateArmy(this));

    // Consumindo pontos de comida
    int consumedFood = population + workers;
    food -= consumedFood;
    if (food < 0) {
      int dead = -food;
      float deathRatio = (float) dead / (population + workers);
      output.append(String.format("! Imperio #%d: %d pessoas morreram de fome.\n", super.getId(), dead));
      JOptionPane.showMessageDialog(null, String.format("! Imperio #%d: %d pessoas morreram de fome.\n", super.getId(), dead));
      food = 0;

      int removed = lumber.takeWorkers((int) Math.ceil(lumber.getWorkers() * deathRatio));
      workers -= removed;
      dead -= removed;

      for (Army army : armies.values()) {
        if (dead <= 0) break;
        removed = army.takeWorkers((int) Math.ceil(army.getWorkers() * deathRatio));
        workers -= removed;
        dead -= removed;
      }

      for (Mine mine : mines.values()) {
        if (dead <= 0) break;
        removed = mine.takeWorkers((int) Math.ceil(mine.getWorkers() * deathRatio));
        workers -= removed;
        dead -= removed;
      }

      for (Farm farm : farms.values()) {
        if (dead <= 0) break;
        removed = farm.takeWorkers((int) Math.ceil(farm.getWorkers() * deathRatio));
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

  public String statusReport() {
    return "=== Empire Status ===\n"
        + "Name: " + name + "\n"
        + "ID: " + super.getId() + "\n"
        + "Population: " + population + "\n"
        + "Workers: " + workers + "\n"
        + "Food: " + food + "\n"
        + "Wood: " + wood + "\n"
        + "Iron: " + iron + "\n"
        + "Gold: " + gold + "\n"
        + "Farms: " + farms.size() + "\n"
        + "Mines: " + mines.size() + "\n"
        + "Armies: " + armies.size() + "\n";
  }

  // ---

  @Override
  public String toString() {
    return String.format("Imperio %s #%d | Populacao: %d; Trabalhadores: %d; Comida: %d; Madeira: %d; Ferro: %d; Ouro: %d", 
           name, super.getId(), population, workers, food, wood, iron, gold);
  }
}

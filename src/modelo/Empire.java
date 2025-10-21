package modelo;

import java.util.ArrayList;
import persistencia.BancoDeDados;

/**
 * Represents an Empire in the game.
 * An Empire manages its population, resources (food, wood, iron, gold),
 * buildings (Farms, Mines, Lumber), and military units (Armies). It is the central
 * entity controlled by a player.
 */
public class Empire extends Entidade {
  /** The non-working population of the empire. */
  private int population = 30;
  /** The empire's food supply, consumed by the population each turn. */
  private int food = 100;
  /** The number of citizens currently assigned to a workplace. */
  private int workers = 0;
  /** The empire's iron supply, used for military and construction. */
  private int iron = 100;
  /** The empire's gold supply, used for construction and army maintenance. */
  private int gold = 100;
  /** The empire's wood supply, used for construction. */
  private int wood = 50;
  /** The name of the empire. */
  private String name;

  /** A list of all armies belonging to the empire. */
  private ArrayList<Army> armies = new ArrayList<>();
  /** A list of all farms belonging to the empire. */
  private ArrayList<Farm> farms = new ArrayList<>();
  /** A list of all mines belonging to the empire. */
  private ArrayList<Mine> mines = new ArrayList<>();
  /** The lumber mill of the empire for wood production. */
  private Lumber lumber;

  /** Reference to the database for persistence operations. */
  private BancoDeDados db;

  /**
   * Constructs a new Empire with a given name.
   * Initializes its resources and creates a default lumber mill.
   *
   * @param db The database instance for persistence.
   * @param name The name for the new empire.
   */
  public Empire(BancoDeDados db, String name) {
    super(db.nextEmpire());
    this.name = name;
    this.db = db;
    lumber = new Lumber(super.getId(), db);
    db.getLumbers().insert(lumber);
  }

  /** @return The list of the empire's armies. */
  public ArrayList<Army> getArmies() { return armies; }
  /** @return The list of the empire's farms. */
  public ArrayList<Farm> getFarms() { return farms; }
  /** @return The list of the empire's mines. */
  public ArrayList<Mine> getMines() { return mines; }
  /** @return The empire's lumber mill. */
  public Lumber getLumber() { return lumber; }
  /** @return The name of the empire. */
  public String getName() { return name; }
  /** @return The current non-working population. */
  public int getPopulation() { return population; }
  /** @return The current number of workers. */
  public int getWorkers() { return workers; }
  /** @return The current amount of food. */
  public int getFood() { return food; }
  /** @return The current amount of iron. */
  public int getIron() { return iron; }
  /** @return The current amount of gold. */
  public int getGold() { return gold; }

  /** @param population The new total non-working population. */
  public void setPopulation(int population) { this.population = population; }
  /** @param workers The new total number of workers. */
  public void setWorkers(int workers) { this.workers = workers; }
  /** @param name The new name for the empire. */
  public void setName(String name) { this.name = name; }
  /** @param food The new total amount of food. */
  public void setFood(int food) { this.food = food; }
  /** @param iron The new total amount of iron. */
  public void setIron(int iron) { this.iron = iron; }
  /** @param gold The new total amount of gold. */
  public void setGold(int gold) { this.gold = gold; }

  /**
   * Increases the empire's population by a given amount.
   * @param amount The number of people to add to the population.
   */
  public void addPopulation(int amount) { population += amount; }

  /** @return true if the empire has at least one farm, false otherwise. */
  public boolean hasFarm() { return !farms.isEmpty(); }
  /** @return true if the empire has at least one mine, false otherwise. */
  public boolean hasMine() { return !mines.isEmpty(); }
  /** @return true if the empire has at least one army, false otherwise. */
  public boolean hasArmy() { return !armies.isEmpty(); }

  // ---

  /**
   * Destroys the empire and all its associated entities (armies, farms, etc.)
   * from the database.
   */
  public void destroy() {
    lumber.destroy();
    for (Army a : armies) a.destroy();
    for (Farm f : farms) f.destroy();
    for (Mine m : mines) m.destroy();
    db.getEmpires().remove(super.getId());
  }

  /**
   * Builds a new house, increasing the population.
   * Costs 5 wood and 5 gold.
   * @return true if the house was built, false if resources were insufficient.
   */
  public boolean buildHouse() {
    if (wood < 5 || gold < 5) return false;
    wood -= 5;
    gold -= 5;

    population += 3;
    return true;
  }

  /**
   * Builds a new farm for food production.
   * Costs 5 wood and 2 gold.
   * @return true if the farm was built, false if resources were insufficient.
   */
  public boolean buildFarm() {
    if (wood < 5 || gold < 2) return false;
    wood -= 5;
    gold -= 2;

    Farm farm = new Farm(super.getId(), db);
    db.getFarms().insert(farm);
    farms.add(farm);
    return true;
  }

  /**
   * Builds a new mine for iron and gold production.
   * Costs 15 wood and 5 gold.
   * @return true if the mine was built, false if resources were insufficient.
   */
  public boolean buildMine() {
    if (wood < 15 || gold < 5) return false;
    wood -= 15;
    gold -= 5;

    Mine mine = new Mine(super.getId(), db);
    db.getMines().insert(mine);
    mines.add(mine);
    return true;
  }

  /**
   * Creates a new army.
   * Costs 20 gold and 50 iron.
   * @return true if the army was created, false if resources were insufficient.
   */
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

  /**
   * Sends workers from the general population to the lumber mill.
   * @param amount The desired number of workers to send.
   * @return The actual number of workers sent.
   */
  public int sendWorkersToLumber(int amount) {
    amount = lumber.sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  /**
   * Sends workers from the general population to a specific farm.
   * @param amount The desired number of workers to send.
   * @param id The ID of the target farm.
   * @return The actual number of workers sent.
   */
  public int sendWorkersToFarm(int amount, int id) {
    amount = ((Farm) (db.getFarms().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  /**
   * Sends workers from the general population to a specific mine.
   * @param amount The desired number of workers to send.
   * @param id The ID of the target mine.
   * @return The actual number of workers sent.
   */
  public int sendWorkersToMine(int amount, int id) {
    amount = ((Mine) (db.getMines().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  /**
   * Sends workers (recruits) from the general population to a specific army.
   * @param amount The desired number of workers to send.
   * @param id The ID of the target army.
   * @return The actual number of workers sent.
   */
  public int sendWorkersToArmy(int amount, int id) {
    amount = ((Army) (db.getArmies().findById(id))).sendWorkers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  // ---

  /**
   * Takes workers from the lumber mill and returns them to the general population.
   * @param amount The desired number of workers to take.
   * @return The actual number of workers taken.
   */
  public int takeWorkersFromLumber(int amount) {
    int taken = lumber.takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  /**
   * Takes workers from a specific farm and returns them to the general population.
   * @param amount The desired number of workers to take.
   * @param id The ID of the target farm.
   * @return The actual number of workers taken.
   */
  public int takeWorkersFromFarm(int amount, int id) {
    int taken = ((Farm) (db.getFarms().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  /**
   * Takes workers from a specific mine and returns them to the general population.
   * @param amount The desired number of workers to take.
   * @param id The ID of the target mine.
   * @return The actual number of workers taken.
   */
  public int takeWorkersFromMine(int amount, int id) {
    int taken = ((Mine) (db.getMines().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  /**
   * Takes soldiers from a specific army and returns them to the general population.
   * @param amount The desired number of soldiers to return.
   * @param id The ID of the target army.
   * @return The actual number of workers taken.
   */
  public int takeWorkersFromArmy(int amount, int id) {
    int taken = ((Army) (db.getArmies().findById(id))).takeWorkers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  // ---

  /**
   * Simulates the passing of one turn for the empire. This includes resource
   * extraction and food consumption. If food is insufficient, starvation occurs,
   * and a portion of the population and workers may die.
   *
   * @return A string containing a log of events that occurred during the turn, such as starvation.
   */
  public String runTurn() {
    StringBuilder output = new StringBuilder();

    // Resource extraction
    wood += lumber.extractWood();

    for (Farm farm : farms)
      food += farm.extractFood();

    for (Mine mine : mines) {
      iron += mine.extractIron();
      gold += mine.extractGold();
    }

    // Food consumption
    int consumed = population + workers;
    food -= consumed;
    if (food < 0) {
      int dead = -food;
      float perc = (float) dead / (population + workers);
      output.append(String.format("! Imperio #%d: %d pessoas morreram de fome.\n", super.getId(), dead));
      food = 0;

      // Remove workers proportionally from all workplaces due to starvation
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
      
      // Remove remaining dead from the idle population
      if (dead > 0)
        population -= Math.min(population, dead);

      if (population < 0) population = 0;
      if (workers < 0) workers = 0;
    }

    return output.toString();
  }

  // ---

  /**
   * Returns a string representation of the empire's current status.
   * @return A formatted string with the empire's name, ID, and resource counts.
   */
  @Override
  public String toString() {
    return String.format("Imperio %s #%d | Populacao: %d; Trabalhadores: %d; Comida: %d; Madeira: %d; Ferro: %d; Ouro: %d", name, super.getId(), population, workers, food, wood, iron, gold);
  }
}
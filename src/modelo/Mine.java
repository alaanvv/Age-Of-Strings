package modelo;

import persistencia.BancoDeDados;

/**
 * Represents a Mine building within an Empire.
 * A mine is used to produce iron and gold. It has a finite amount of
 * resources that are depleted over time. Production is determined by the
 * number of workers assigned to it, up to a maximum of 20.
 */
public class Mine extends Entidade {
  /** The amount of iron remaining in the mine. */
  private int iron;
  /** The amount of gold remaining in the mine. */
  private int gold;
  /** The current number of workers assigned to this mine. */
  private int workers = 0;
  /** The ID of the Empire this mine belongs to. */
  private int empireId;

  /** Reference to the database for persistence operations. */
  private BancoDeDados db;

  // ---

  /**
   * Constructs a new Mine for a given empire.
   * Initializes the mine with a random, finite amount of iron and gold.
   *
   * @param empireId The ID of the empire that owns this mine.
   * @param db The database instance.
   */
  public Mine(int empireId, BancoDeDados db) {
    super(db.nextMine());
    this.empireId = empireId;
    this.db = db;
    iron = (int)(Math.random() * 250);
    gold = (int)(Math.random() * 100);
  }

  /**
   * Gets the current number of workers at the mine.
   * @return The number of workers.
   */
  public int getWorkers() {
    return workers;
  }

  /**
   * Destroys the mine, removing it from the database and returning its
   * workers to the parent empire's population.
   */
  public void destroy() {
    db.getMines().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
  }

  /**
   * Assigns a number of workers to the mine, up to a maximum of 20.
   * @param amount The number of workers to send.
   * @return The actual number of workers that were added to the mine.
   */
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.min(20, workers + amount);
    return workers - previousWorkers;
  }

  /**
   * Removes a number of workers from the mine.
   * The number of workers cannot go below 0.
   * @param amount The number of workers to remove.
   * @return The actual number of workers that were removed from the mine.
   */
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  /**
   * Calculates the amount of iron produced in one turn.
   * Production is based on the number of workers and is limited by the
   * amount of iron remaining in the mine.
   * @return The amount of iron extracted.
   */
  public int extractIron() {
    double efficiency = Math.log(workers + 1);
    int collectedIron = Math.min(iron, (int)(Math.random() * efficiency * 3));
    iron -= collectedIron;
    return collectedIron;
  }

  /**
   * Calculates the amount of gold produced in one turn.
   * Production is based on the number of workers and is limited by the
   * amount of gold remaining in the mine.
   * @return The amount of gold extracted.
   */
  public int extractGold() {
    double efficiency = Math.log(workers + 1);
    int collectedGold = Math.min(gold, (int)(Math.random() * efficiency * 2));
    gold -= collectedGold;
    return collectedGold;
  }

  /**
   * Gets the ID of the empire that owns this mine.
   * @return The empire's ID.
   */
  public int getEmpireId() {
    return empireId;
  }

  // ---

  /**
   * Returns a string representation of the Mine.
   * @return A string showing the mine's ID, worker count, and remaining resources.
   */
  @Override
  public String toString() {
    return String.format("Mina #%d | Trabalhadores: %d/20; Ferro: %d; Ouro: %d", super.getId(), workers, iron, gold);
  }
}
package modelo;

import persistencia.BancoDeDados;

/**
 * Represents a Farm building within an Empire.
 * A farm is used to produce food, and its production is determined by the
 * number of workers assigned to it. Each farm has a maximum capacity of 10 workers.
 */
public class Farm extends Entidade {
  /** The current number of workers assigned to this farm. */
  private int workers = 0;
  /** The ID of the Empire this farm belongs to. */
  private int empireId;

  /** Reference to the database for persistence operations. */
  private BancoDeDados db;

  // ---

  /**
   * Constructs a new Farm for a given empire.
   *
   * @param empireId The ID of the empire that owns this farm.
   * @param db The database instance.
   */
  public Farm(int empireId, BancoDeDados db) {
    super(db.nextFarm());
    this.empireId = empireId;
    this.db = db;
  }

  /**
   * Gets the current number of workers at the farm.
   * @return The number of workers.
   */
  public int getWorkers() {
    return workers;
  }

  /**
   * Destroys the farm, removing it from the database and returning its
   * workers to the parent empire's population.
   */
  public void destroy() {
    db.getFarms().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
  }

  /**
   * Assigns a number of workers to the farm, up to a maximum of 10.
   * @param amount The number of workers to send.
   * @return The actual number of workers that were added to the farm.
   */
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.min(10, workers + amount);
    return workers - previousWorkers;
  }

  /**
   * Removes a number of workers from the farm.
   * The number of workers cannot go below 0.
   * @param amount The number of workers to remove.
   * @return The actual number of workers that were removed from the farm.
   */
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  /**
   * Calculates the amount of food produced in one turn.
   * Production is based on the number of workers, with logarithmic efficiency.
   * @return The amount of food extracted.
   */
  public int extractFood() {
    double efficiency = Math.log(workers + 1);
    return (int)(Math.random() * efficiency * 20);
  }

  /**
   * Gets the ID of the empire that owns this farm.
   * @return The empire's ID.
   */
  public int getEmpireId() {
    return empireId;
  }

  // ---

  /**
   * Returns a string representation of the Farm.
   * @return A string showing the farm's ID and current worker count.
   */
  @Override
  public String toString() {
    return String.format("Fazenda #%d | Trabalhadores: %d/10", super.getId(), workers);
  }
}
package modelo;

import persistencia.BancoDeDados;

/**
 * Represents a Lumber mill or logging camp within an Empire.
 * A lumber mill is used to produce wood, and its production is determined by the
 * number of workers assigned to it. Unlike farms or mines, there is no
 * explicit worker capacity.
 */
public class Lumber extends Entidade {
  /** The current number of workers assigned to this lumber mill. */
  private int workers = 0;
  /** The ID of the Empire this lumber mill belongs to. */
  private int empireId;

  /** Reference to the database for persistence operations. */
  private BancoDeDados db;

  // ---

  /**
   * Constructs a new Lumber mill for a given empire.
   *
   * @param empireId The ID of the empire that owns this lumber mill.
   * @param db The database instance.
   */
  public Lumber(int empireId, BancoDeDados db) {
    super(db.nextLumber());
    this.empireId = empireId;
    this.db = db;
  }

  /**
   * Gets the current number of workers at the lumber mill.
   * @return The number of workers.
   */
  public int getWorkers() {
    return workers;
  }

  /**
   * Destroys the lumber mill, removing it from the database and returning its
   * workers to the parent empire's population.
   */
  public void destroy() {
    db.getLumbers().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
  }

  /**
   * Assigns a number of workers to the lumber mill.
   * @param amount The number of workers to send.
   * @return The actual number of workers that were added.
   */
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = workers + amount;
    return workers - previousWorkers;
  }

  /**
   * Removes a number of workers from the lumber mill.
   * The number of workers cannot go below 0.
   * @param amount The number of workers to remove.
   * @return The actual number of workers that were removed.
   */
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  /**
   * Calculates the amount of wood produced in one turn.
   * Production is based on the number of workers, with logarithmic efficiency.
   * @return The amount of wood extracted.
   */
  public int extractWood() {
    double efficiency = Math.log(workers + 1);
    return (int)(Math.random() * efficiency * 10);
  }

  /**
   * Gets the ID of the empire that owns this lumber mill.
   * @return The empire's ID.
   */
  public int getEmpireId() {
    return empireId;
  }

  // ---

  /**
   * Returns a string representation of the Lumber mill.
   * @return A string showing the lumber mill's ID and current worker count.
   */
  @Override
  public String toString() {
    return String.format("Campo de lenhadores #%d | Trabalhadores: %d", super.getId(), workers);
  }
}
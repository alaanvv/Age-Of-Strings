package modelo;

/**
 * Wood-harvesting workpost for an {@link Empire}.
 * Maintains worker count, can be torn down returning population, and produces
 * wood each turn based on worker-driven efficiency.
 */
public class Lumber extends Entidade implements WorkpostInterface{
  private int workers = 0;
  private final Empire empire;


  // ---

  public Lumber(int id, Empire empire) {
    super(id);
    this.empire = empire;
  }

  public int getWorkers() {
    return workers;
  }

  public void destroy() {
    empire.addPopulation(workers);
  }

  // Retorna quantos trabalhadores entraram
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = workers + amount;
    return workers - previousWorkers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  // Extrai madeira equivalente a um turno
  public int extractWood() {
    double efficiency = Math.log(workers + 1);
    return (int)(Math.random() * efficiency * 10);
  }

  public int getEmpireId() {
    return empire.getId();
  }

  // ---

  @Override
  public String toString() {
    return String.format("Campo de lenhadores #%d | Trabalhadores: %d", super.getId(), workers);
  }
}

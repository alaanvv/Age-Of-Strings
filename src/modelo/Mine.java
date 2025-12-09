package modelo;

/**
 * Mineral-producing workpost for an {@link Empire}.
 * Tracks finite iron/gold reserves, enforces worker cap, and extracts
 * resources each turn with diminishing returns.
 */
public class Mine extends Entidade implements WorkpostInterface{
  private int iron;
  private int gold;
  private final int initialIron;
  private final int initialGold;
  private int workers = 0;
  private final Empire empire;


  // ---

  public Mine(int id, Empire empire) {
    super(id);
    this.empire = empire;
    initialIron = (int)(Math.random() * 250);
    initialGold = (int)(Math.random() * 100);
    iron = initialIron;
    gold = initialGold;
  }

  public int getWorkers() {
    return workers;
  }

  public int getRemainingIron() { return iron; }
  public int getRemainingGold() { return gold; }
  public int getInitialIron() { return initialIron; }
  public int getInitialGold() { return initialGold; }
  public int getExtractedIron() { return initialIron - iron; }
  public int getExtractedGold() { return initialGold - gold; }

  // Retorna quantos trabalhadores entraram
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.min(20, workers + amount);
    return workers - previousWorkers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  // Extrai ferro equivalente a um turno
  public int extractIron() {
    double efficiency = Math.log(workers + 1);
    int collectedIron = Math.min(iron, (int)(Math.random() * efficiency * 3));
    iron -= collectedIron;
    return collectedIron;
  }

  // Extrai ouro equivalente a um turno
  public int extractGold() {
    double efficiency = Math.log(workers + 1);
    int collectedGold = Math.min(gold, (int)(Math.random() * efficiency * 2));
    gold -= collectedGold;
    return collectedGold;
  }

  public int getEmpireId() {
    return empire.getId();
  }

  // ---

  @Override
  public String toString() {
    return String.format("Mina #%d | Trabalhadores: %d/20; Ferro: %d; Ouro: %d", super.getId(), workers, iron, gold);
  }
}

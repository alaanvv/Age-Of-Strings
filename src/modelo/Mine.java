package modelo;

import persistencia.BancoDeDados;

public class Mine extends Entidade {
  private int iron;
  private int gold;
  private int workers = 0;
  private int empireId;

  private BancoDeDados db;

  // ---

  public Mine(int empireId, BancoDeDados db) {
    super(db.getMines().getSize());
    this.empireId = empireId;
    this.db = db;
    iron = (int)(Math.random() * 250);
    gold = (int)(Math.random() * 100);
  }

  public int getWorkers() {
    return workers;
  }

  public void destroy() {
    db.getMines().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
  }

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
    return empireId;
  }

  // ---

  @Override
  public String toString() {
    return String.format("Mina #%d | Trabalhadores: %d/10; Ferro: %d; Ouro: %d", super.getId(), workers, iron, gold);
  }
}

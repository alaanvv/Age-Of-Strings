package modelo;

import persistencia.BancoDeDados;

public class Farm extends Entidade {
  private int workers = 0;
  private int empireId;

  private BancoDeDados db;

  // ---

  public Farm(int empireId, BancoDeDados db) {
    super(db.nextFarm());
    this.empireId = empireId;
    this.db = db;
  }

  public int getWorkers() {
    return workers;
  }

  public void destroy() {
    db.getFarms().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
  }

  // Retorna quantos trabalhadores entraram
  public int sendWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.min(10, workers + amount);
    return workers - previousWorkers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int takeWorkers(int amount) {
    int previousWorkers = workers;
    workers = Math.max(0, workers - amount);
    return previousWorkers - workers;
  }

  // Extrai comida equivalente a um turno
  public int extractFood() {
    double efficiency = Math.log(workers + 1);
    return (int)(Math.random() * efficiency * 20);
  }

  public int getEmpireId() {
    return empireId;
  }

  // ---

  @Override
  public String toString() {
    return String.format("Fazenda #%d | Trabalhadores: %d/10", super.getId(), workers);
  }
}

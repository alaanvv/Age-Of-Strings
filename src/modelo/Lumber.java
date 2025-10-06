package modelo;

import persistencia.BancoDeDados;

public class Lumber extends Entidade {
  private int workers = 0;
  private int empireId;

  private BancoDeDados db;

  // ---

  public Lumber(int empireId, BancoDeDados db) {
    super(db.getLumbers().getSize());
    this.empireId = empireId;
    this.db = db;
  }

  public int getWorkers() {
    return workers;
  }

  public void destroy() {
    db.getLumbers().remove(super.getId());
    ((Empire) db.getEmpires().findById(getEmpireId())).addPopulation(workers);
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
    return empireId;
  }

  // ---

  @Override
  public String toString() {
    return String.format("Campo de lenhadores #%d | Trabalhadores: %d", super.getId(), workers);
  }
}

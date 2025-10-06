package modelo;

import persistencia.BancoDeDados;

public class Lumber extends Entidade {
  private int workers = 0;
  private int empire_id;

  private BancoDeDados db;

  // ---

  public Lumber(int empire_id, BancoDeDados db) {
    super(db.getLumber().getSize());
    this.empire_id = empire_id;
    this.db = db;
  }

  public int getWorkers() { return workers; }

  public void destroy() {
    db.getLumber().remover(super.get_id());
  }

  // Retorna quantos trabalhadores entraram
  public int send_workers(int amount) {
    int _workers = workers;
    workers = workers + amount;
    return workers - _workers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int take_workers(int amount) {
    int _workers = workers;
    workers = Math.max(0, workers - amount);
    return _workers - workers;
  }

  // Extrai madeira equivalente a um turno
  public int extract_wood() {
    double efficiency = Math.log(workers + 1);
    return (int) (Math.random() * efficiency * 10);
  }

  public int get_empire_id() {
    return empire_id;
  }

  // ---

  @Override
  public String toString() {
    return String.format("Campo de lenhadores #%d | Trabalhadores: %d", super.get_id(), workers);
  }
}

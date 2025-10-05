package modelo;

import persistencia.BancoDeDados;

public class Mine extends Entidade{
  private int iron;
  private int gold;
  private int workers = 0;
  private int empire_id;

  public Mine(int empire_id, BancoDeDados banco) {
    super(banco.getMine().getSize());
    iron = (int) (Math.random() * 250);
    gold = (int) (Math.random() * 100);
    this.empire_id = empire_id;
  }

  // Retorna quantos trabalhadores entraram
  public int send_workers(int amount) {
    int _workers = workers;
    workers = Math.min(20, workers + amount);
    return workers - _workers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int take_workers(int amount) {
    int _workers = workers;
    workers = Math.max(0, workers - amount);
    return _workers - workers;
  }

  // Extrai ferro equivalente a um turno
  public int extract_iron() {
    double efficiency = Math.log(workers + 1); 
    int collected_iron = Math.min(iron, (int) (Math.random() * efficiency * 3));
    iron -= collected_iron;
    return collected_iron;
  }

  // Extrai ouro equivalente a um turno
  public int extract_gold() {
    double efficiency = Math.log(workers + 1); 
    int collected_gold = Math.min(gold, (int) (Math.random() * efficiency * 2));
    gold -= collected_gold;
    return collected_gold;
  }

  public int getEmpire_id() {
        return empire_id;
    }

  public int getWorkers() {
      return workers;
  }

  // ---

  public String toString() {
    return "{" + super.toString() + " | " + String.format("Mina #%d| Trabalhadores: %d/20 | Ferro: %d | Ouro: %d}", super.get_id(), workers, iron, gold);
  }
}

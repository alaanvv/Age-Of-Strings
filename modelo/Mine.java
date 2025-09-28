package modelo;
public class Mine {
  private int id;
  private int iron;
  private int gold;
  private int workers = 0;

  public Mine(int id) {
    this.id = id;
    iron = (int) (Math.random() * 250);
    gold = (int) (Math.random() * 100);
  }

  // ---

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

  // ---

  public String toString() {
    return String.format("Mina #%d\nTrabalhadores: %d/20\nFerro: %d\nOuro: %d", id, workers, iron, gold);
  }
}

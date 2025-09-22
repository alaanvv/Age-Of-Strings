public class Farm {
  private int id;
  private int workers = 0;

  public Farm(int id) {
    this.id = id;
  }

  // ---

  // Retorna quantos trabalhadores entraram
  public int send_workers(int amount) {
    int _workers = workers;
    workers = Math.min(10, workers + amount);
    return workers - _workers;
  }

  // Retorna quantos trabalhadores foram retirados
  public int take_workers(int amount) {
    int _workers = workers;
    workers = Math.max(0, workers - amount);
    return _workers - workers;
  }

  // Extrai comida equivalente a um turno
  public int extract_food() {
    double efficiency = Math.log(workers + 1); 
    return (int) (Math.random() * efficiency * 20);
  }

  // ---

  public String toString() {
    return String.format("Fazenda #%d\nTrabalhadores: %d/10", id, workers);
  }
}

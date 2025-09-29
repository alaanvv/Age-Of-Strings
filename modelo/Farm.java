package modelo;

public class Farm extends Entidade{
  private int workers = 0;
  private int idx;

  
  // ---
  
  public Farm(int idx) {
    super();
    this.idx = idx;
  }

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
  @Override
  public String toString() {
    return "{" + super.toString() + " | " + String.format("Fazenda #%d | Trabalhadores: %d/10}", idx, workers);
  }
}

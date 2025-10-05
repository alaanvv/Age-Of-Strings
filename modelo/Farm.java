package modelo;

import persistency.BancoDeDados;

public class Farm extends Entidade{
  private int workers = 0;
  private int empire_id;

  // ---
  
  public Farm(int empire_id, BancoDeDados banco) {
    super(banco.getFarm().getSize());
    this.empire_id = empire_id;
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

  public int getEmpire_id() {
        return empire_id;
    }

    public int getWorkers() {
        return workers;
    }

  // ---
  @Override
  public String toString() {
    return "{" + super.toString() + " | " + String.format("Fazenda #%d | Trabalhadores: %d/10}", super.get_id(), workers);
  }
}

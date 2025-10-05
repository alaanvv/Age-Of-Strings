package modelo;

import persistencia.BancoDeDados;

public class LumberCamp extends Entidade{
  private int workers = 0;
  private int empire_id;

  // ---

  public LumberCamp(int empire_id, BancoDeDados banco){
    super(banco.getLumberCamp().getSize());
    this.empire_id = empire_id;
  }

  public int send_workers(int amount) {
    workers += amount;
    return amount;
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

  public int getWorkers() {
      return workers;
  }

  public int getEmpire_id() {
      return empire_id;
  }

  // ---

  public String toString() {
    return "{" + super.toString() + " | " + String.format("Campo de Lenhadores | Trabalhadores: %d}", workers);
  }
}

package modelo;
public class LumberCamp extends Entidade{
  private int workers = 0;

  // ---

  public LumberCamp(){
    super();
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

  // ---

  public String toString() {
    return "{" + super.toString() + " | " + String.format("Campo de Lenhadores | Trabalhadores: %d}", workers);
  }
}

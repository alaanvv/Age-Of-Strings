package modelo;

public class Farm extends Entidade implements Workpost {
  private int workers = 0;
  private Empire empire;


  // ---

  public Farm(int id, Empire empire) {
    super(id);
    this.empire = empire;
  }

  public int getWorkers() {
    return workers;
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

  public int getEmpireId(){
    return empire.getId();
  }

  // ---

  @Override
  public String toString() {
    return String.format("Fazenda #%d | Trabalhadores: %d/10", super.getId(), workers);
  }
}

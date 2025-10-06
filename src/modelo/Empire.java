package modelo;

import persistencia.BancoDeDados;
import java.util.ArrayList;

public class Empire extends Entidade {
  private int population = 3;
  private int food = 10;
  private int workers = 0;
  private int iron = 10;
  private int gold = 10;
  private int wood = 5;

  private ArrayList<Army> armies = new ArrayList<>();
  private ArrayList<Farm> farms = new ArrayList<>();
  private ArrayList<Mine> mines = new ArrayList<>();
  private Lumber lumber;

  private BancoDeDados db;

  public Empire(BancoDeDados db) {
    super(db.getEmpire().getSize());
    this.db = db;
    lumber = new Lumber(super.get_id(), db);
  }

  public ArrayList<Army> getArmies() { return armies; }
  public ArrayList<Farm> getFarms() { return farms; }
  public ArrayList<Mine> getMines() { return mines; }
  public Lumber getLumber() { return lumber; }
  public int getPopulation() { return population; }
  public int getWorkers() { return workers; }
  public int getFood() { return food; }
  public int getIron() { return iron; }
  public int getGold() { return gold; }

  public void setPopulation(int population) { this.population = population; }
  public void setWorkers(int workers) { this.workers = workers; }
  public void setFood(int food) { this.food = food; }
  public void setIron(int iron) { this.iron = iron; }
  public void setGold(int gold) { this.gold = gold; }

  // ---

  public boolean build_house() {
    if (wood < 5 || gold < 5) return false;
    wood -= 5;
    gold -= 5;

    population += 3;
    return true;
  }

  public boolean build_farm() {
    if (wood < 5 || gold < 2) return false;
    wood -= 5;
    gold -= 2;

    Farm farm = new Farm(super.get_id(), db);
    db.getFarm().inserir(farm);
    farms.add(farm);
    return true;
  }

  public boolean build_mine() {
    if (wood < 15 || gold < 5) return false;
    wood -= 15;
    gold -= 5;

    Mine mine = new Mine(super.get_id(), db);
    db.getMine().inserir(mine);
    mines.add(mine);
    return true;
  }

  public boolean create_army() {
    if (gold < 20 || iron < 50) return false;
    iron -= 50;
    gold -= 20;

    Army army = new Army(super.get_id(), db);
    db.getArmy().inserir(army);
    armies.add(army);
    return true;
  }

  // ---

  public int send_workers_to_lumber(int amount) {
    amount = lumber.send_workers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int send_workers_to_farm(int amount, int id) {
    amount = ((Farm) (db.getFarm().buscarId(id))).send_workers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int send_workers_to_mine(int amount, int id) {
    amount = ((Mine) (db.getMine().buscarId(id))).send_workers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  public int send_workers_to_army(int amount, int id) {
    amount = ((Mine) (db.getArmy().buscarId(id))).send_workers(Math.min(amount, population));
    population -= amount;
    workers += amount;
    return amount;
  }

  // ---

  public int take_workers_from_lumber(int amount) {
    int taken = ((Lumber) (db.getLumber().buscarId(super.get_id()))).take_workers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int take_workers_from_farm(int amount, int id) {
    int taken = ((Farm) (db.getFarm().buscarId(id))).take_workers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int take_workers_from_mine(int amount, int id) {
    int taken = ((Mine) (db.getMine().buscarId(id))).take_workers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  public int take_workers_from_army(int amount, int id) {
    int taken = ((Army) (db.getArmy().buscarId(id))).take_workers(amount);
    population += taken;
    workers -= taken;
    return taken;
  }

  // ---

  public void run_turn() {
    // Extraindo recursos
 
    wood += lumber.extract_wood();

    for (Farm farm : farms)
      food += farm.extract_food();

    for (Mine mine : mines) {
      iron += mine.extract_iron();
      gold += mine.extract_gold();
    }

    // Consumindo pontos de comida

    int consumed = population + workers;
    food -= consumed;
    if (food >= 0) return;

    int dead = -food;
    float perc = (float) dead / (population + workers);
    System.out.println(String.format("! Imperio #%d: %d pessoas morreram de fome.", super.get_id(), -dead));
    food = 0;

    int removed = lumber.take_workers((int) Math.ceil(lumber.getWorkers() * perc));
    workers -= removed;
    dead -= removed;

    for (Army army : armies) {
      if (dead <= 0) break;
      removed = army.take_workers((int) Math.ceil(army.getWorkers() * perc));
      workers -= removed;
      dead -= removed;
    }

    for (Mine mine : mines) {
      if (dead <= 0) break;
      removed = mine.take_workers((int) Math.ceil(mine.getWorkers() * perc));
      workers -= removed;
      dead -= removed;
    }

    for (Farm farm : farms) {
      if (dead <= 0) break;
      removed = farm.take_workers((int) Math.ceil(farm.getWorkers() * perc));
      workers -= removed;
      dead -= removed;
    }

    if (dead > 0)
      population -= Math.min(population, dead);

    if (population < 0) population = 0;
    if (workers < 0) workers = 0;
  }

  // ---

  @Override
  public String toString() {
    return String.format("Imperio #%d | Populacao: %d; Trabalhadores: %d; Comida: %d; Madeira: %d; Ferro: %d; Ouro: %d", super.get_id(), population, workers, food, wood, iron, gold);
  }
}

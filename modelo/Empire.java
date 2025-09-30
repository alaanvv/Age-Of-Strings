package modelo;
import java.util.ArrayList;

public class Empire extends Entidade{
  private int idx;
  private int food = 10;
  private int wood = 50;
  private int iron = 0;
  private int gold = 10;
  private int population = 3;
  private int workers = 0;

  private LumberCamp lumber_camp = new LumberCamp();
  private ArrayList<Farm> farms  = new ArrayList<>();
  private ArrayList<Mine> mines  = new ArrayList<>();
  private ArrayList<Army> armies = new ArrayList<>();
  

  // ---
  
  public Empire(int idx){
    super();
    this.idx = idx;
  }

  public int get_population() {
    return population;
  }

  void set_population(int amt){
    population = amt;
  }

  int get_workers(){
    return workers;
  }

  void set_workers(int amt){
    workers = amt;
  }

  int get_iron(){
    return iron;
  }

  void set_iron(int val){
    iron = val;
  }

  int get_gold(){
    return gold;
  }

  void set_gold(int val){
    gold = val;
  }

  int get_food(){
    return food;
  }

  void set_food(int val){
    food = val;
  }

  public int get_farm_count() {
    return farms.size();
  }

  public int get_mine_count() {
    return mines.size();
  }

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
    
    farms.add(new Farm(farms.size()));
    return true;
    
  }
  
  public boolean build_mine() {
    if (wood < 15 || gold < 5) return false;
    wood -= 15;
    gold -= 5;

    mines.add(new Mine(mines.size()));
    return true;
  }

  public boolean create_army(){
    if(gold < 20 || food < 1 || iron < 50) return false;
    gold -= 20;
    food -= food;
    iron -= iron;

    armies.add(new Army(this.idx, armies.size()));
    return true;
  }

  // ---
  
  public int send_workers_to_lumber_camp(int amount) {
    amount = Math.min(amount, population);
    amount = lumber_camp.send_workers(amount);
    population -= amount;
    workers += amount;
    return amount;
  }
  
  public int send_workers_to_farm(int amount, int id) {
    amount = Math.min(amount, population);
    amount = farms.get(id).send_workers(amount);
    population -= amount;
    workers += amount;
    return amount;
  }
  
  public void send_workers_to_mine(int amount, int id) {
    amount = Math.min(amount, population);

    workers += amount;
    population -= amount;
    mines.get(id).send_workers(amount);
  }

  public boolean send_workers_to_army(int amount, int idx){
    return armies.get(idx).allocate_work(this, amount);
  }

  // ---

  public int take_workers_from_lumber_camp(int amount) {
    int taken = lumber_camp.take_workers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  public int take_workers_from_farm(int amount, int id) {
    int taken = farms.get(id).take_workers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  public int take_workers_from_mine(int amount, int id) {
    int taken = mines.get(id).take_workers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  // ---

  public void view_lumber_camp() {
    System.out.println(lumber_camp);
  }

  public void view_farms() {
    if (farms.size() == 0) System.out.println("Nenhuma fazenda construida.\nPara construir uma fazenda, use `build farm`");
    farms.forEach(System.out::println);
  }

  public void view_mines() {
    if (mines.size() == 0) System.out.println("Nenhuma mina construida.\nPara construir uma mina, use `build mine`");
    mines.forEach(System.out::println);
  }

  @Override
  public String toString() {
    return 
      "{" + super.toString() + " | " + 
      String.format("Império | População: %d | Trabalhadores: %d | Comida: %d | Madeira: %d | Ferro: %d | Ouro: %d",
      population, workers, food, wood, iron, gold);
  }
}

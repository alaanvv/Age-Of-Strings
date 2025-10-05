package modelo;
import java.util.ArrayList;
import persistencia.BancoDeDados;

public class Empire extends Entidade{
  private int food = 1000;
  private int wood = 50;
  private int iron = 100;
  private int gold = 100;
  private int population = 3;
  private int workers = 0;
  private BancoDeDados banco;

  private LumberCamp lumber_camp;
  private ArrayList<Farm> farms  = new ArrayList<>();
  private ArrayList<Mine> mines  = new ArrayList<>();
  private ArrayList<Army> armies = new ArrayList<>();
  
  
  public Empire(BancoDeDados banco){
    super(banco.getEmpire().getSize());
    this.banco = banco;
    lumber_camp = new LumberCamp(super.get_id(), banco);
  }

  public int get_population() {
    return population;
  }

  void set_population(int amt){
    population = amt;
  }

  public int getFood() {
      return food;
  }

  public void setFood(int food) {
      this.food = food;
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
    return farms.size()-1;
  }

  public int get_mine_count() {
    return mines.size()-1;
  }

    public LumberCamp getLumber_camp() {
        return lumber_camp;
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
    
    Farm new_farm = new Farm(super.get_id(), banco);

    farms.add(new_farm);
    banco.getFarm().inserir(new_farm);
    return true;
    
  }
  
  public boolean build_mine() {
    if (wood < 15 || gold < 5) return false;
    wood -= 15;
    gold -= 5;

    Mine new_mine = new Mine(super.get_id(), banco);

    mines.add(new_mine);
    banco.getMine().inserir(new_mine);
    return true;
  }

  public boolean create_army(){
    if(gold < 20 || food < 1 || iron < 50) return false;
    gold -= 20;
    food -= 1;
    iron -= 50;

    Army new_army = new Army(super.get_id(), banco);

    armies.add(new_army);
    banco.getArmy().inserir(new_army);
    return true;
  }

  // ---
  
  public int send_workers_to_lumber_camp(int amount) {
    amount = Math.min(amount, population);
    amount = ((LumberCamp)(banco.getLumberCamp().buscarId(super.get_id()))).send_workers(amount);
    population -= amount;
    workers += amount;
    return amount;
  }
  
  public int send_workers_to_farm(int amount, int id) {
    amount = Math.min(amount, population);
    amount = ((Farm)(banco.getFarm().buscarId(id))).send_workers(amount);
    population -= amount;
    workers += amount;
    return amount;
  }
  
  public int send_workers_to_mine(int amount, int id) {
    amount = Math.min(amount, population);

    workers += amount;
    population -= amount;
    ((Mine)(banco.getMine().buscarId(id))).send_workers(amount);
    return amount;
  }

  public boolean send_workers_to_army(int amount, int id){
    return ((Army)(banco.getArmy().buscarId(id))).allocate_work(this, amount);
  }

  // ---

  public int take_workers_from_army(int amount, int id){
    int taken = ((Army)(banco.getArmy().buscarId(id))).take_soldiers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  public int take_workers_from_lumber_camp(int amount) {
    int taken = ((LumberCamp)(banco.getLumberCamp().buscarId(super.get_id()))).take_workers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  public int take_workers_from_farm(int amount, int id) {
    int taken = ((Farm)(banco.getFarm().buscarId(id))).take_workers(amount);
    workers -= taken;
    population += taken;
    return taken;
  }

  public int take_workers_from_mine(int amount, int id) {
    int taken = ((Mine)(banco.getMine().buscarId(id))).take_workers(amount);
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
      String.format("Imperio %d| Populacao: %d | Trabalhadores: %d | Comida: %d | Madeira: %d | Ferro: %d | Ouro: %d",
      super.get_id(), population, workers, food, wood, iron, gold);
  }
}

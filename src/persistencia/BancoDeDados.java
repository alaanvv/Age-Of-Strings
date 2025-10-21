package persistencia;

import java.util.ArrayList;

import javax.lang.model.UnknownEntityException;

import modelo.Entidade;
import modelo.Farm;
import modelo.Lumber;
import modelo.Mine;
import modelo.Army;
import modelo.Battle;
import modelo.Empire;

/**
 * Acts as an in-memory database to manage and persist all major game entities.
 * <p>
 * This class centralizes access to collections of battles, empires, and various
 * resource-producing structures like lumbers, farms, and mines. It also handles
 * the generation of unique IDs for new entities.
 */
public class BancoDeDados {
  private Persistente battles;
  private Persistente empires;
  private Persistente lumbers;
  private Persistente armies;
  private Persistente farms;
  private Persistente mines;

  /**
   * Counter to generate a unique ID for the next Battle entity.
   * It's incremented each time a new battle is created.
   */
  private int cBattles = 1;
  /**
   * Counter to generate a unique ID for the next Empire entity.
   */
  private int cEmpires = 1;
  /**
   * Counter to generate a unique ID for the next Lumber entity.
   */
  private int cLumbers = 1;
  /**
   * Counter to generate a unique ID for the next Army entity.
   */
  private int cArmies = 1;
  /**
   * Counter to generate a unique ID for the next Farm entity.
   */
  private int cFarms = 1;
  /**
   * Counter to generate a unique ID for the next Mine entity.
   */
  private int cMines = 1;

  public BancoDeDados() {
    this.battles = new Persistente();
    this.empires = new Persistente();
    this.lumbers = new Persistente();
    this.armies = new Persistente();
    this.farms = new Persistente();
    this.mines = new Persistente();
  }
  
  /** Create an empire and insert into the database
   * @return new empire id
  */
  public int createEmpire(String name){
    Empire e = new Empire(name, this.nextEmpire());
    empires.insert(e);
    e.getLumber().setId(this.nextLumber());
    lumbers.insert(e.getLumber());
    
    return e.getId();
  }

  /** Create a battle and insert into the database
   * @return the battle id
  */
  public int createBattle(int attackerId, int defenderId){
    Army attackerArmy = (Army)armies.findById(attackerId);
    Army defenderArmy = (Army)armies.findById(defenderId);
    Battle b = new Battle(attackerArmy, defenderArmy, this.nextBattle());
    battles.insert(b);
    return b.getId();
  }

  /**
   * Create a army of a given empire
   * @param empireId The empire that will own the army.
   * @return the army id if it was sucessfully created, or -1 if creation failed (due to lack of resources).
    */
  public int createArmy(int empireId){
    Empire empire = (Empire)empires.findById(empireId);
    Army army = empire.createArmy(this.nextArmy());
    
    if(army == null) return 0;
    
    armies.insert(army);
    return army.getId();
  }

  public int createFarm(int empireId){
    Empire empire = (Empire)empires.findById(empireId);
    Farm farm = empire.buildFarm(empireId);

    if(farm == null) return 0;

    farms.insert(farm);
    return farm.getId();
  }

  public int createMine(int empireId){
    Empire empire = (Empire)empires.findById(empireId);
    Mine mine = empire.buildMine(this.nextMine());

    if(mine == null) return 0;

    mines.insert(mine);
    return mine.getId();
  }

  /** Destroy any type of Entity, removing of its respective persistency.*/
  public void destroyEntity(Entidade entity){
    switch(entity){
        case Battle b -> battles.remove(b.getId());
        case Empire e -> throw new RuntimeException("Objeto tipo Empire deve ser destruído em sua função apropriada (destroyEmpire).");
        case Lumber l -> lumbers.remove(l.getId());
        case Army a -> armies.remove(a.getId());
        case Farm f -> farms.remove(f.getId());
        case Mine m -> mines.remove(m.getId());
        default -> {throw new RuntimeException("Um tipo de entidade inesperada foi encontrada em destroyEntity: " + entity.getClass().getName());}
      }
  }

  /** Destroy a list of Entidade, removing of its respective persistency */
  public void destroyEntities(ArrayList<Entidade> entitieList){
    for(int i = entitieList.size()-1; i >= 0; i--){
      destroyEntity(entitieList.get(i));
    }
  }

  /** Correctly remove an Empire, removing all its dependent entities and finally destroying the empire*/
  public void destroyEmpire(Empire empire){

    //Adds all empire dependent entities to an ArrayList and burn'em all in destroyEntities
    ArrayList<Entidade> entities = new ArrayList<>();
    
    entities.addAll(empire.getArmies());
    entities.addAll(empire.getFarms());
    entities.addAll(empire.getMines());
    entities.add(empire.getLumber());

    destroyEntities(entities);
    empires.remove(empire.getId());
  }


  /**
   * Generates and returns the next available unique ID for a Battle.
   * This method uses a post-increment operation, ensuring each call returns
   * a sequential and unique identifier (0, 1, 2, ...).
   * @return The next unique integer ID for a battle.
   */
  public int nextBattle() { return cBattles++; }

  /**
   * Generates and returns the next available unique ID for an Empire.
   * @return The next unique integer ID for an empire.
   */
  public int nextEmpire() { return cEmpires++; }

  /**
   * Generates and returns the next available unique ID for a Lumber mill.
   * @return The next unique integer ID for a lumber mill.
   */
  public int nextLumber() { return cLumbers++; }

  /**
   * Generates and returns the next available unique ID for an Army.
   * @return The next unique integer ID for an army.
   */
  public int nextArmy() { return cArmies++; }

  /**
   * Generates and returns the next available unique ID for a Farm.
   * @return The next unique integer ID for a farm.
   */
  public int nextFarm() { return cFarms++; }

  /**
   * Generates and returns the next available unique ID for a Mine.
   * @return The next unique integer ID for a mine.
   */
  public int nextMine() { return cMines++; }

  public Persistente getLumbers() {
    return lumbers;
  }

  public Persistente getBattles() {
    return battles;
  }

  public Persistente getEmpires() {
    return empires;
  }

  public Persistente getArmies() {
    return armies;
  }

  public Persistente getFarms() {
    return farms;
  }

  public Persistente getMines() {
    return mines;
  }

  public void setBattles(Persistente battles) {
    this.battles = battles;
  }

  public void setEmpires(Persistente empires) {
    this.empires = empires;
  }

  public void setArmies(Persistente armies) {
    this.armies = armies;
  }

  public void setLumbers(Persistente lumbers) {
    this.lumbers = lumbers;
  }

  public void setFarms(Persistente farms) {
    this.farms = farms;
  }

  public void setMines(Persistente mines) {
    this.mines = mines;
  }

  public boolean hasLumber() {
    return !lumbers.getEntidades().isEmpty();
  }

  public boolean hasBattle() {
    return !battles.getEntidades().isEmpty();
  }

  public boolean hasEmpire() {
    return !empires.getEntidades().isEmpty();
  }

  public boolean hasFarm() {
    return !farms.getEntidades().isEmpty();
  }

  public boolean hasMine() {
    return !mines.getEntidades().isEmpty();
  }

  public boolean hasArmy() {
    return !armies.getEntidades().isEmpty();
  }

  public int sizeLumbers() {
    return lumbers.getSize();
  }

  public int sizeBattles() {
    return battles.getSize();
  }

  public int sizeEmpires() {
    return empires.getSize();
  }

  public int sizeFarms() {
    return farms.getSize();
  }

  public int sizeMines() {
    return mines.getSize();
  }

  public int sizeArmies() {
    return armies.getSize();
  }
}
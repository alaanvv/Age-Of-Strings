package persistencia;

import java.util.ArrayList;

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
  private final Persistente<Battle> battles;
  private final Persistente<Empire> empires;
  private final Persistente<Lumber> lumbers;
  private final Persistente<Army> armies;
  private final Persistente<Farm> farms;
  private final Persistente<Mine> mines;

  private int battleIdSequence = 1;
  private int empireIdSequence = 1;
  private int lumberIdSequence = 1;
  private int armyIdSequence = 1;
  private int farmIdSequence = 1;
  private int mineIdSequence = 1;

  public BancoDeDados() {
    this.battles = new Persistente<Battle>();
    this.empires = new Persistente<Empire>();
    this.lumbers = new Persistente<Lumber>();
    this.armies = new Persistente<Army>();
    this.farms = new Persistente<Farm>();
    this.mines = new Persistente<Mine>();
  }
  
  /** Create an empire and insert into the database
   * @return new empire id
  */
  public int createEmpire(String name){
    Empire empire = new Empire(name, this.nextEmpire());
    empires.insert(empire);
    empire.getLumber().setId(this.nextLumber());
    lumbers.insert(empire.getLumber());
    
    return empire.getId();
  }

  /** Create a battle and insert into the database
   * @return the battle id
  */
  public int createBattle(int attackerId, int defenderId) throws InexistentIdException{
    Battle battle;
    Army attackerArmy = null;
    Army defenderArmy = null;

    try{
      attackerArmy = (Army)armies.findById(attackerId);
      defenderArmy = (Army)armies.findById(defenderId);
      battle = new Battle(attackerArmy, defenderArmy, this.nextBattle());
      attackerArmy.setInBattle(true);
      defenderArmy.setInBattle(true);
      attackerArmy.setCurrentBattle(battle);
      defenderArmy.setCurrentBattle(battle);
      
    } catch(InexistentIdException e){
      if (attackerArmy == null){
        throw new InexistentIdException("Id do exército atacante não existe");
      }else{
        throw new InexistentIdException("Id do exército defensor não existe");
      }
    }
    
    battles.insert(battle);
    return battle.getId();
  }

  /**
   * Create a army of a given empire
   * @param empireId The empire that will own the army.
   * @return the army id if it was sucessfully created, or -1 if creation failed (due to lack of resources).
    */
  public int createArmy(int empireId) throws InexistentIdException{
    Empire empire = (Empire)empires.findById(empireId);
    Army army = empire.createArmy(this.nextArmy());
    
    if(army == null) return 0;
    
    armies.insert(army);
    return army.getId();
  }

  public int createFarm(int empireId) throws InexistentIdException{
    Empire empire = (Empire)empires.findById(empireId);
    Farm farm = empire.buildFarm(this.nextFarm());

    if(farm == null) return 0;

    farms.insert(farm);
    return farm.getId();
  }

  public int createMine(int empireId) throws InexistentIdException{
    Empire empire = (Empire)empires.findById(empireId);
    Mine mine = empire.buildMine(this.nextMine());

    if(mine == null) return 0;

    mines.insert(mine);
    return mine.getId();
  }

  /** Destroy any type of Entity, removing of its respective persistency.*/
  public void destroyEntity(Entidade entity) throws RuntimeException{
    if(entity instanceof Battle){
      battles.remove(entity.getId());
    } else if(entity instanceof Empire){
      throw new RuntimeException("Objeto tipo Empire deve ser destruído em sua função apropriada (destroyEmpire).");
    } else if(entity instanceof Lumber){
      lumbers.remove(entity.getId());
    } else if(entity instanceof Army){
      armies.remove(entity.getId());
        try {
          Empire owner = empires.findById(((Army) entity).getEmpireId());
          owner.getArmies().remove(entity.getId());
        } catch (InexistentIdException ignored) {}
    } else if(entity instanceof Farm){
      farms.remove(entity.getId());
    } else if(entity instanceof Mine){
      mines.remove(entity.getId());
    } else {
      throw new RuntimeException("Um tipo de entidade inesperada foi recebida em destroyEntity: " + entity.getClass().getName());
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
    
    entities.addAll(empire.getArmies().values());
    entities.addAll(empire.getFarms().values());
    entities.addAll(empire.getMines().values());
    entities.add(empire.getLumber());

    destroyEntities(entities);
    empires.remove(empire.getId());
  }


  public int nextBattle() { return battleIdSequence++; }

  public int nextEmpire() { return empireIdSequence++; }

  public int nextLumber() { return lumberIdSequence++; }

  public int nextArmy() { return armyIdSequence++; }

  public int nextFarm() { return farmIdSequence++; }

  public int nextMine() { return mineIdSequence++; }

  public Persistente<Lumber> getLumbers() {
    return lumbers;
  }

  public Persistente<Battle> getBattles() {
    return battles;
  }

  public Persistente<Empire> getEmpires() {
    return empires;
  }

  public Persistente<Army> getArmies() {
    return armies;
  }

  public Persistente<Farm> getFarms() {
    return farms;
  }

  public Persistente<Mine> getMines() {
    return mines;
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
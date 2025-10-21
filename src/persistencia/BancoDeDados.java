package persistencia;

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
  private int cBattles = 0;
  /**
   * Counter to generate a unique ID for the next Empire entity.
   */
  private int cEmpires = 0;
  /**
   * Counter to generate a unique ID for the next Lumber entity.
   */
  private int cLumbers = 0;
  /**
   * Counter to generate a unique ID for the next Army entity.
   */
  private int cArmies = 0;
  /**
   * Counter to generate a unique ID for the next Farm entity.
   */
  private int cFarms = 0;
  /**
   * Counter to generate a unique ID for the next Mine entity.
   */
  private int cMines = 0;

  public BancoDeDados() {
    this.battles = new Persistente();
    this.empires = new Persistente();
    this.lumbers = new Persistente();
    this.armies = new Persistente();
    this.farms = new Persistente();
    this.mines = new Persistente();
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

  // The rest of the methods are standard getters, setters, and utility checks
  // (has/size), which are generally self-explanatory.

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
package persistencia;

/**
 * Acts as an in-memory database for the application.
 * This class serves as a centralized container for all the data repositories ({@link Persistente} instances)
 * that manage the game's entities, such as empires, armies, and battles. It also handles the generation
 * of unique IDs for new entities.
 */
public class BancoDeDados {
  /** Repository for Battle entities. */
  private Persistente battles;
  /** Repository for Empire entities. */
  private Persistente empires;
  /** Repository for Lumber entities. */
  private Persistente lumbers;
  /** Repository for Army entities. */
  private Persistente armies;
  /** Repository for Farm entities. */
  private Persistente farms;
  /** Repository for Mine entities. */
  private Persistente mines;

  /** Counter to generate unique IDs for Battle entities. */
  private int cBattles = 0;
  /** Counter to generate unique IDs for Empire entities. */
  private int cEmpires = 0;
  /** Counter to generate unique IDs for Lumber entities. */
  private int cLumbers = 0;
  /** Counter to generate unique IDs for Army entities. */
  private int cArmies = 0;
  /** Counter to generate unique IDs for Farm entities. */
  private int cFarms = 0;
  /** Counter to generate unique IDs for Mine entities. */
  private int cMines = 0;

  /**
   * Constructs a new BancoDeDados instance, initializing all the entity repositories.
   */
  public BancoDeDados() {
    this.battles = new Persistente();
    this.empires = new Persistente();
    this.lumbers = new Persistente();
    this.armies = new Persistente();
    this.farms = new Persistente();
    this.mines = new Persistente();
  }
  
  /** @return The next available unique ID for a Battle. */
  public int nextBattle() { return cBattles++; }
  /** @return The next available unique ID for an Empire. */
  public int nextEmpire() { return cEmpires++; }
  /** @return The next available unique ID for a Lumber mill. */
  public int nextLumber() { return cLumbers++; }
  /** @return The next available unique ID for an Army. */
  public int nextArmy() { return cArmies++; }
  /** @return The next available unique ID for a Farm. */
  public int nextFarm() { return cFarms++; }
  /** @return The next available unique ID for a Mine. */
  public int nextMine() { return cMines++; }

  /** @return The repository for Lumber entities. */
  public Persistente getLumbers() {
    return lumbers;
  }
  
  /** @return The repository for Battle entities. */
  public Persistente getBattles() {
    return battles;
  }

  /** @return The repository for Empire entities. */
  public Persistente getEmpires() {
    return empires;
  }
  
  /** @return The repository for Army entities. */
  public Persistente getArmies() {
    return armies;
  }

  /** @return The repository for Farm entities. */
  public Persistente getFarms() {
    return farms;
  }

  /** @return The repository for Mine entities. */
  public Persistente getMines() {
    return mines;
  }

  /** @param battles The new repository for Battle entities. */
  public void setBattles(Persistente battles) {
    this.battles = battles;
  }

  /** @param empires The new repository for Empire entities. */
  public void setEmpires(Persistente empires) {
    this.empires = empires;
  }

  /** @param armies The new repository for Army entities. */
  public void setArmies(Persistente armies) {
    this.armies = armies;
  }

  /** @param lumbers The new repository for Lumber entities. */
  public void setLumbers(Persistente lumbers) {
    this.lumbers = lumbers;
  }

  /** @param farms The new repository for Farm entities. */
  public void setFarms(Persistente farms) {
    this.farms = farms;
  }

  /** @param mines The new repository for Mine entities. */
  public void setMines(Persistente mines) {
    this.mines = mines;
  }

  /** @return {@code true} if there is at least one Lumber entity, {@code false} otherwise. */
  public boolean hasLumber() {
    return !lumbers.getEntidades().isEmpty();
  }

  /** @return {@code true} if there is at least one Battle entity, {@code false} otherwise. */
  public boolean hasBattle() {
    return !battles.getEntidades().isEmpty();
  }

  /** @return {@code true} if there is at least one Empire entity, {@code false} otherwise. */
  public boolean hasEmpire() {
    return !empires.getEntidades().isEmpty();
  }

  /** @return {@code true} if there is at least one Farm entity, {@code false} otherwise. */
  public boolean hasFarm() {
    return !farms.getEntidades().isEmpty();
  }

  /** @return {@code true} if there is at least one Mine entity, {@code false} otherwise. */
  public boolean hasMine() {
    return !mines.getEntidades().isEmpty();
  }

  /** @return {@code true} if there is at least one Army entity, {@code false} otherwise. */
  public boolean hasArmy() {
    return !armies.getEntidades().isEmpty();
  }

  /** @return The total number of Lumber entities. */
  public int sizeLumbers() {
    return lumbers.getSize();
  }

  /** @return The total number of Battle entities. */
  public int sizeBattles() {
    return battles.getSize();
  }

  /** @return The total number of Empire entities. */
  public int sizeEmpires() {
    return empires.getSize();
  }

  /** @return The total number of Farm entities. */
  public int sizeFarms() {
    return farms.getSize();
  }

  /** @return The total number of Mine entities. */
  public int sizeMines() {
    return mines.getSize();
  }

  /** @return The total number of Army entities. */
  public int sizeArmies() {
    return armies.getSize();
  }
}
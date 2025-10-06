package persistencia;

public class BancoDeDados {
  private Persistente battles;
  private Persistente empires;
  private Persistente lumbers;
  private Persistente armies;
  private Persistente farms;
  private Persistente mines;

  public BancoDeDados() {
    this.battles = new Persistente();
    this.empires = new Persistente();
    this.lumbers = new Persistente();
    this.armies = new Persistente();
    this.farms = new Persistente();
    this.mines = new Persistente();
  }

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

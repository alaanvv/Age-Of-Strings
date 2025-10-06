package persistencia;

public class BancoDeDados {
  private Persistente lumberCamp;
  private Persistente battle;
  private Persistente empire;
  private Persistente farm;
  private Persistente mine;
  private Persistente army;
  private Persistente war;

  public BancoDeDados() {
    this.lumberCamp = new Persistente();
    this.battle = new Persistente();
    this.empire = new Persistente();
    this.farm = new Persistente();
    this.mine = new Persistente();
    this.army = new Persistente();
    this.war = new Persistente();
  }

  public Persistente getLumber() { return lumberCamp; }
  public Persistente getBattle() { return battle; }
  public Persistente getEmpire() { return empire; }
  public Persistente getFarm() { return farm; }
  public Persistente getMine() { return mine; }
  public Persistente getArmy() { return army; }
  public Persistente getWar() { return war; }

  public void setLumberCamp(Persistente lumberCamp) { this.lumberCamp = lumberCamp; }
  public void setBattle(Persistente battle) { this.battle = battle; }
  public void setEmpire(Persistente empire) { this.empire = empire; }
  public void setArmy(Persistente army) { this.army = army; }
  public void setFarm(Persistente farm) { this.farm = farm; }
  public void setMine(Persistente mine) { this.mine = mine; }
  public void setWar(Persistente war) { this.war = war; }
}

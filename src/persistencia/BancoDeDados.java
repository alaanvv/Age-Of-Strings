package persistencia;

public class BancoDeDados {
    private Persistente army;
    private Persistente empire;
    private Persistente farm;
    private Persistente lumberCamp;
    private Persistente mine;
    private Persistente battles;

    public BancoDeDados(){
        this.army = new Persistente();
        this.empire = new Persistente();
        this.farm = new Persistente();
        this.lumberCamp = new Persistente();
        this.mine = new Persistente();
        this.battles = new Persistente();
    }

    public Persistente getArmy() {
        return army;
    }

    public Persistente getEmpire(){
        return empire;
    }

    public Persistente getFarm() {
        return farm;
    }

    public Persistente getLumberCamp() {
        return lumberCamp;
    }

    public Persistente getMine() {
        return mine;
    }

    public Persistente getBattles() {
        return battles;
    }

    public void setArmy(Persistente army){
        this.army = army;
    }

    public void setEmpire(Persistente empire){
        this.empire = empire;
    }

    public void setFarm(Persistente farm){
        this.farm = farm;
    }

    public void setLumberCamp(Persistente lumberCamp){
        this.lumberCamp = lumberCamp;
    }

    public void setMine(Persistente mine){
        this.mine = mine;
    }

    public void setBattles(Persistente war){
        this.battles = battles;
    }
}

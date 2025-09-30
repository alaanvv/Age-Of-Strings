package persistency;

public class BancoDeDados {
    Persistente Army;
    Persistente Empire;
    Persistente Farm;
    Persistente LumberCamp;
    Persistente Mine;
    Persistente War;

    public BancoDeDados(){
        this.Army = new Persistente();
        this.Empire = new Persistente();
        this.Farm = new Persistente();
        this.LumberCamp = new Persistente();
        this.Mine = new Persistente();
        this.War = new Persistente();
    }

    public Persistente getArmy() {
        return Army;
    }

    public Persistente getEmpire(){
        return Empire;
    }

    public Persistente getFarm() {
        return Farm;
    }

    public Persistente getLumberCamp() {
        return LumberCamp;
    }

    public Persistente getMine() {
        return Mine;
    }

    public Persistente getWar() {
        return War;
    }

    public void setArmy(Persistente army){
        this.Army = army;
    }

    public void setEmpire(Persistente empire){
        this.Empire = empire;
    }

    public void setFarm(Persistente farm){
        this.Farm = farm;
    }

    public void setLumberCamp(Persistente lumberCamp){
        this.LumberCamp = lumberCamp;
    }

    public void setMine(Persistente mine){
        this.Mine = mine;
    }

    public void setWar(Persistente war){
        this.War = war;
    }
}

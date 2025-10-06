package modelo;

public abstract class Entidade {
  private int id;

  public Entidade(int id) {
    this.id = id;
  }

  public void set_id(int id) {
    this.id = id;
  }

  public int get_id() {
    return id;
  }

  @Override
  public String toString() {
    return String.format("Entidade #%d", id);
  }
}

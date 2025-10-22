package modelo;

public abstract class Entidade implements Comparable<Entidade>{
  private Integer id;

  public Entidade(){}

  public Entidade(int id) {
    this.id = id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public int compareTo(Entidade other){
    return Integer.compare(this.getId(), other.getId());
  }

  @Override
  public String toString() {
    return String.format("Entidade #%d", id);
  }
}
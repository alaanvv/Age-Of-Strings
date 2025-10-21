package persistencia;

import java.util.ArrayList;
import modelo.Entidade;
import java.util.List;

public class Persistente {
  private List<Entidade> entidades;

  public Persistente() {
    this.entidades = new ArrayList<>();
  }

  public void insert(Entidade entity) {
    this.entidades.add(entity);
  }

  public Boolean update(int id, Entidade entity) {
    for (int i = 0; i < entidades.size(); i++) {
      if (entidades.get(i).getId() == id) {
        entidades.set(i, entity);
        return true;
      }
    }
    return false;
  }

  public Boolean remove(int id) {
    for (Entidade entidade : entidades) {
      if (entidade.getId() == id) {
        entidades.remove(entidade);
        return true;
      }
    }
    return false;
  }

  public Entidade findById(int id) {
    for (Entidade entidade : entidades)
      if (entidade.getId() == id)
        return entidade;
    return null;
  }

  public int getSize() {
    return entidades.size();
  }

  public List<Entidade> getEntidades() {
    return entidades;
  }

  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Entidade entidade : entidades)
      s.append(entidade.toString()).append("\n");
    return s.toString();
  }
}
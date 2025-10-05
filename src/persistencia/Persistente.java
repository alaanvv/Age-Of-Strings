package persistencia;

import java.util.ArrayList;
import modelo.Entidade;
import java.util.List;

public class Persistente {
  private List<Entidade> entidades;

  public Persistente() {
    this.entidades = new ArrayList<>();
  }

  public void inserir(Entidade objeto) {
    this.entidades.add(objeto);
  }

  public Boolean alterar(int id, Entidade objeto) {
    for (int i = 0; i < entidades.size(); i++) {
      if (entidades.get(i).get_id() == id) {
        entidades.set(i, objeto);
        return true;
      }
    }
    return false;
  }

  public Boolean remover(int id) {
    for (Entidade entidade : entidades) {
      if (entidade.get_id() == id) {
        entidades.remove(entidade);
        return true;
      }
    }
    return false;
  }

  public Entidade buscarId(int id) {
    for (Entidade entidade : entidades)
      if (entidade.get_id() == id)
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

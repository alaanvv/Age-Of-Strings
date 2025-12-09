package persistencia;

import modelo.Entidade;
import java.util.Map;
import java.util.TreeMap;
/**
 * A generic, in-memory repository to manage a collection of {@link T} objects.
 */
public class Persistente<T extends Entidade> {

  private final Map<Integer, T> entidades;

  public Persistente() {
    this.entidades = new TreeMap<>();
  }

  public boolean insert(T entity) {
    if (this.entidades.containsKey(entity.getId())) 
      return false;
    
    this.entidades.put(entity.getId(), entity);
    return true;
  }

  /**
   * Finds an entity by its ID and replaces it with the provided entity object.
   */
  public Boolean update(int id, T entity) throws InexistentIdException{
    if(!entidades.containsKey(id)){
      throw new InexistentIdException();
    }
    entidades.put(id, entity);
    return true;
  }

  /**
   * Removes an entity from the collection based on its ID.
   */
  public Boolean remove(int id) throws InexistentIdException{
    if(!entidades.containsKey(id)){
      throw new InexistentIdException();
    }
    entidades.remove(id);
    return true;
  }

  public boolean exists(int id) {
    return entidades.containsKey(id);
  }

  public T findById(int id) throws InexistentIdException{
    T ret = entidades.get(id);
    if (ret == null){
      throw new InexistentIdException();
    }
    return entidades.get(id);
  }

  public int getSize() {
    return entidades.size();
  }

  /**
   * Returns a direct reference to the internal list of entities.
   */
  public Map<Integer, T> getEntidades() {
    return entidades;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (T entidade : entidades.values())
      s.append(entidade.toString()).append("\n");
    return s.toString();
  }
}
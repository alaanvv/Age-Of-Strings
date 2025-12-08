package persistencia;

import modelo.Entidade;
import java.util.Map;
import java.util.TreeMap;
/**
 * A generic, in-memory repository to manage a collection of {@link T} objects.
 * <p>
 * This class acts as a simple data access layer, providing basic CRUD
 * (Create, Read, Update, Delete) operations on a list of entities. It is not
 * thread-safe.
 */
public class Persistente<T extends Entidade> {
  /**
   * The internal list that stores all managed entities.
   */
  private final Map<Integer, T> entidades;

  public Persistente() {
    this.entidades = new TreeMap<>();
  }

  public void insert(T entity) {
    this.entidades.put(entity.getId(), entity);
  }

  /**
   * Finds an entity by its ID and replaces it with the provided entity object.
   *
   * @param id The ID of the entity to be updated.
   * @param entity The new entity object that will replace the old one.
   * @return {@code true} if an entity with the given ID was found and updated,
   * {@code false} otherwise.
   */
  public Boolean update(int id, T entity) {
    if(!entidades.containsKey(id)) return false;
    entidades.put(id, entity);
    return true;
    
    /* for (int i = 0; i < entidades.size(); i++) {
      if (entidades.get(i).getId() == id) {
        entidades.set(i, entity);
        return true;
      }
    }
    return false; */
  }

  /**
   * Removes an entity from the collection based on its ID.
   * <p>
   * This method iterates through the list to find the first entity matching the
   * specified ID and removes it. The iteration stops immediately after the first
   * match is found and removed.
   *
   * @param id The ID of the entity to remove.
   * @return {@code true} if an entity with the given ID was found and removed,
   * {@code false} otherwise.
   */
  public Boolean remove(int id) {
    if(!entidades.containsKey(id)) return false;
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
   * <p>
   * <b>Warning:</b> This method does not return a copy. Any modifications made
   * to the returned list (e.g., adding or removing elements) will directly
   * affect the internal state of this {@code Persistente} object, bypassing its
   * methods. This can break encapsulation and lead to unpredictable behavior.
   *
   * @return The internal list of {@code T} objects.
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
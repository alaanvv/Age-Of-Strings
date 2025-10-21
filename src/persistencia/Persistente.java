package persistencia;

import java.util.ArrayList;
import modelo.Entidade;
import java.util.List;

/**
 * A generic, in-memory repository to manage a collection of {@link Entidade} objects.
 * <p>
 * This class acts as a simple data access layer, providing basic CRUD
 * (Create, Read, Update, Delete) operations on a list of entities. It is not
 * thread-safe.
 */
public class Persistente {
  /**
   * The internal list that stores all managed entities.
   */
  private List<Entidade> entidades;

  public Persistente() {
    this.entidades = new ArrayList<>();
  }

  public void insert(Entidade entity) {
    this.entidades.add(entity);
  }

  /**
   * Finds an entity by its ID and replaces it with the provided entity object.
   *
   * @param id The ID of the entity to be updated.
   * @param entity The new entity object that will replace the old one.
   * @return {@code true} if an entity with the given ID was found and updated,
   * {@code false} otherwise.
   */
  public Boolean update(int id, Entidade entity) {
    for (int i = 0; i < entidades.size(); i++) {
      if (entidades.get(i).getId() == id) {
        entidades.set(i, entity);
        return true;
      }
    }
    return false;
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

  /**
   * Returns a direct reference to the internal list of entities.
   * <p>
   * <b>Warning:</b> This method does not return a copy. Any modifications made
   * to the returned list (e.g., adding or removing elements) will directly
   * affect the internal state of this {@code Persistente} object, bypassing its
   * methods. This can break encapsulation and lead to unpredictable behavior.
   *
   * @return The internal list of {@code Entidade} objects.
   */
  public List<Entidade> getEntidades() {
    return entidades;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Entidade entidade : entidades)
      s.append(entidade.toString()).append("\n");
    return s.toString();
  }
}
package persistencia;

import java.util.ArrayList;
import modelo.Entidade;
import java.util.List;
import java.util.Iterator;

/**
 * A generic in-memory repository for storing and managing objects that extend the {@link Entidade} class.
 * This class provides basic CRUD (Create, Read, Update, Delete) operations using an ArrayList as the underlying data store.
 */
public class Persistente {
  /** The list that holds all the Entidade instances. */
  private List<Entidade> entidades;

  /**
   * Constructs a new Persistente instance, initializing an empty list to store entities.
   */
  public Persistente() {
    this.entidades = new ArrayList<>();
  }

  /**
   * Inserts a new entity into the repository.
   * @param entity The entity to be added. It must not be null.
   */
  public void insert(Entidade entity) {
    this.entidades.add(entity);
  }

  /**
   * Updates an existing entity in the repository identified by its ID.
   * @param id The ID of the entity to be updated.
   * @param entity The new entity that will replace the old one.
   * @return {@code true} if an entity with the given ID was found and updated, {@code false} otherwise.
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
   * Removes an entity from the repository based on its ID.
   * This method safely removes an item while iterating to avoid {@code ConcurrentModificationException}.
   * @param id The ID of the entity to remove.
   * @return {@code true} if an entity with the given ID was found and removed, {@code false} otherwise.
   */
  public Boolean remove(int id) {
    Iterator<Entidade> iterator = entidades.iterator();
    while (iterator.hasNext()) {
        Entidade entidade = iterator.next();
        if (entidade.getId() == id) {
            iterator.remove();
            return true;
        }
    }
    return false;
  }


  /**
   * Finds and returns an entity by its unique identifier.
   * @param id The ID of the entity to find.
   * @return The {@link Entidade} object if found, or {@code null} if no entity with the given ID exists.
   */
  public Entidade findById(int id) {
    for (Entidade entidade : entidades)
      if (entidade.getId() == id)
        return entidade;
    return null;
  }

  /**
   * Gets the total number of entities currently stored in the repository.
   * @return The size of the entity list.
   */
  public int getSize() {
    return entidades.size();
  }

  /**
   * Returns the complete list of entities stored in the repository.
   * @return A {@link List} containing all {@link Entidade} objects.
   */
  public List<Entidade> getEntidades() {
    return entidades;
  }

  /**
   * Generates a string representation of all entities in the repository, with each entity on a new line.
   * @return A multi-line string listing all stored entities.
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    for (Entidade entidade : entidades)
      s.append(entidade.toString()).append("\n");
    return s.toString();
  }
}
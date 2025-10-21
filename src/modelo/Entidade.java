package modelo;

/**
 * An abstract base class for all model entities in the application.
 * It provides a common foundation by ensuring that every entity has a unique
 * integer identifier.
 */
public abstract class Entidade {
  /**
   * The unique identifier for this entity.
   */
  private int id;

  /**
   * Constructs an Entidade with a specified ID.
   *
   * @param id The unique identifier to assign to this entity.
   */
  public Entidade(int id) {
    this.id = id;
  }

  /**
   * Sets or updates the identifier for this entity.
   *
   * @param id The new unique identifier.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Retrieves the unique identifier of this entity.
   *
   * @return The entity's integer ID.
   */
  public int getId() {
    return id;
  }

  /**
   * Returns a basic string representation of the entity, primarily showing its ID.
   *
   * @return A string in the format "Entidade #id".
   */
  @Override
  public String toString() {
    return String.format("Entidade #%d", id);
  }
}
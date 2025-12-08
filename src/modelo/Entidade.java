package modelo;

/**
 * Base entity carrying an auto-managed identifier and ordering contract.
 * Every domain object persisted in memory inherits from this to get a common
 * id field and comparability for sorted collections.
 */
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

  

    @Override

    public int hashCode() {

        final int prime = 31;

        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;

    }

  

    @Override

    public boolean equals(Object obj) {

        if (this == obj)

            return true;

        if (obj == null)

            return false;

        if (!(obj instanceof Entidade))

            return false;

        Entidade other = (Entidade) obj;

        if (id == null) {

            if (other.id != null)

                return false;

        } else if (!id.equals(other.id))

            return false;

        return true;

    }

  }

  
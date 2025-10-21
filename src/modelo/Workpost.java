package modelo;

/** A contract for entities that can receive e send workers */
public abstract interface Workpost {
   public int takeWorkers(int amount);
   public int sendWorkers(int amount);   
}

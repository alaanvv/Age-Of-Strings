package auxiliar;

/**
 * A utility class that serves as a container for various tuple-like data structures.
 * This class groups record definitions for simple, immutable data carriers.
 */
public class Tuple{

   /**
    * A record representing an immutable tuple of three integers.
    * This is a convenient data carrier for holding three related integer values.
    *
    * @param first  The first integer element of the tuple.
    * @param second The second integer element of the tuple.
    * @param third  The third integer element of the tuple.
    */
   public record iii(int first, int second, int third){}
}
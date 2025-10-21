package auxiliar;

/**
 * A utility class that serves as a namespace for various tuple-like record definitions.
 * <p>
 * This class is not meant to be instantiated. Instead, it groups related, simple
 * data-carrying records.
 */
public class Tuple{

   /**
    * An immutable record representing a triplet of integers, often used for
    * coordinates or returning multiple integer values from a method.
    * <p>
    * The name 'iii' is a concise representation of its contents: (integer, integer, integer).
    * As a {@code record}, it automatically provides a canonical constructor,
    * accessor methods ({@code first()}, {@code second()}, {@code third()}),
    * {@code equals()}, {@code hashCode()}, and {@code toString()} implementations.
    *
    * @param first  The first integer value.
    * @param second The second integer value.
    * @param third  The third integer value.
    */
   public record iii(int first, int second, int third){}
}
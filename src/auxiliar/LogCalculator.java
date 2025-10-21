package auxiliar;

/**
 * A utility class providing mathematical functions related to logarithms.
 * This class contains static methods for common logarithmic calculations that are not
 * directly available in the standard {@link java.lang.Math} library.
 */
public class LogCalculator {

   /**
    * Calculates the logarithm of a number to a specified base.
    * This method uses the change of base formula: $log_b(x) = log_e(x) / log_e(b)$.
    *
    * @param x The number for which to find the logarithm (must be > 0).
    * @param b The base of the logarithm (must be > 0 and not equal to 1).
    * @return The logarithm of {@code x} to the base {@code b}.
    */
   public static double logBase(double x, double b) {
      return Math.log(x) / Math.log(b);
   }
}
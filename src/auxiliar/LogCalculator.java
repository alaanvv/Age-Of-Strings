package auxiliar;

/**
 * A utility class providing a static method for logarithmic calculations.
 * Since this is a utility class, it is not meant to be instantiated.
 */
public class LogCalculator {

   /**
    * Calculates the logarithm of a number with a specified base.
    * <p>
    * This method uses the mathematical change-of-base formula:
    * {@code log_b(x) = log_e(x) / log_e(b)}, where {@code log_e} is the
    * natural logarithm ({@link Math#log(double)}).
    *
    * @param x The number to find the logarithm of (the argument). Must be positive.
    * @param b The base of the logarithm. Must be positive and not equal to 1.
    * @return The value of log base {@code b} of {@code x}. Returns {@code NaN}
    * if either {@code x} or {@code b} is negative, or if {@code b} is 1.
    * Returns {@code Infinity} or {@code -Infinity} for certain edge cases
    * (e.g., base > 1 and x is 0 or infinity).
    */
   public static double logBase(double x, double b) {
      return Math.log(x) / Math.log(b);
   }
}
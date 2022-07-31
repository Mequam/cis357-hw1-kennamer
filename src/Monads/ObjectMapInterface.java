package CashProgram.Monads;

/** a basic mapping function interface,
 * intended to be used as an argument type for higher order functions that take 2d functions as an input*/
@FunctionalInterface
public interface ObjectMapInterface {
    /** A basic mapping function, f(x) = y
     * inteanded for use in "lambda arguments"
     *
     * @param i an arbitrary input of any type
     * @return returns an arbitrary output of any type
     * */
    Object map(Object i);
}

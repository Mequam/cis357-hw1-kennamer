package CashProgram.Monads;


import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * this class represents a generic Counter container and contains some convince function to interact with them
 */
public class CounterContainer {
    /**
     * array of counters that contain generic objects
     */
    public ArrayList<Counter> data;

    /**
     * empty initializer that performs no operations
     */
    public  CounterContainer() {

    }

    /**
     * adds a new counter to the data
     * @param c new counter to add
     * */
    public  void add(Counter c) {
        data.add(c);
    }

    /**
     * removes the counter at the given index
     *
     * @param c the index of the counter to remove
     * */
    public void remove(int c) {
        data.remove(c);
    }
    /**
     * removes the counter at the given reference from our data
     *
     * @param c reference to the counter that we want to remove
     * */
    public void remove(Counter c) {
        data.remove(c);
    }
    /**
     * create a new counter container of the given length @param length the length
     *
     * @param length the number of memory slots initialized in the container
     */
    public  CounterContainer(int length) {
        data = new ArrayList<Counter>(length);
    }

    /*
    * this pattern shows up enough that Im not typing it again
    * and I wanted to play with interfaces, lambdas and everyhting that makes functional
    * programmers happy :)
    *
    */

    /**
     * generic test mapping of an array, takes a test criteria lambda and a translator function lambda
     * returns null if an object in the array matching testCriteria is not found
     * and the result of mapping the found object if it is found @param testCriteria the test criteria
     *
     * @param testCriteria lambda that takes an object within the data array and returns true if it matches a given criteria
     * @param translator maps the found value to a value that we want to return, used for attribute grabing and type conversion (a -> b)
     * @return returns null if no object is found and the result of the translator([found object]) other wise
     */
    public Object testMap(Predicate<Object> testCriteria, ObjectMapInterface translator) {
        for (Object o : data) {
            if (testCriteria.test(o)) {
                return translator.map(o);
            }
        }
        return null;
    }

    /**
     * zero out the counter container and reset the number of things stored in it
     */
    public void zeroData() {
        for ( Counter c : data) {
            c.count = 0;
        }
    }
}

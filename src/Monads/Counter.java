package CashProgram.Monads;

/**
 * This class is intended for counting quantities of programmatically defined data
 * it functions similarly to a haskell monad
 */
public class Counter {
    /**
     * the item that we are counting, how many "elements" do we have
     */
    public Object element;
    /**
     * how many of "element" is there, defaults to 0 if no value is set on construction
     */
    public int count = 0;
    @Override
    public  String toString() {
        return String.format("%d " + element.toString(),count);
    }

    /**
     * Initializer that takes an element to count
     * NOTE: you MUST pass an element to a counter
     *
     * @param element the object that we are counting
     */
    public Counter(Object element) {
        this.element = element;
    }

    /**
     * Initialize a counter with a non default count
     *
     * @param e     the object that we are counting
     * @param count how many of that element that we have
     */
    public Counter(Object e, int count) {
        this(e);
        this.count = count;
    }

    /* convenience functions */

    /**
     * increment count
     */
    public void add() {
        this.count += 1;
    }

    /**
     * increments count by x
     *
     * @param x an integer that gets added to count
     */
    public void add(int x) {
        this.count += x;
    }

    /**
     * adds together the count of two counters and stores the result into the first Counter
     * ONLY runs if the Counter elements agree about equality
     *
     * @param c a counter that we add to our count, we only add if the types of the elements inside the counters match
     */
    public void add(Counter c) {
        if (c != null && c.element.equals(this.element)) {
            count += c.count;
        }
    }
}
package Monoids;

/**
 * This class is inteanded for counting quantities of programatically defined data
 * it functions simillarly to a haskell monoid
 */
public class Counter {
    /**
     * the item that we are counting, how many "elements" do we have
     */
    public Object element;
    /**
     * how many of "element" is there
     */
    public int count = 0;

    /**
     * Initilizer that takes an element to count
     * <p>
     * NOTE: you MUST pass an element to a counter
     */
    public Counter(Object element) {
        this.element = element;
    }

    /**
     * Initilize a counter with a non default count
     */
    public Counter(Object e, int count) {
        this(e);
        this.count = count;
    }

    /* convinence functons */

    /**
     * incriment count
     */
    public void add() {
        this.count += 1;
    }

    /**
     * incriments count by x
     */
    public void add(int x) {
        this.count += x;
    }

    /**
     * adds together the count of two counters and stores the result into the first Counter
     * ONLY runs if the Counter elements agree about equality
     */
    public void add(Counter c) {
        if (c != null && c.element.equals(this.element)) {
            count += c.count;
        }
    }
}
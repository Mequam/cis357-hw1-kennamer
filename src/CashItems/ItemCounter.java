package CashItems;
import Monads.*;

import java.util.function.Predicate;

/**
 * a convenient container class that ONLY contains items
 */
public class ItemCounter extends Monads.Counter {
    /**
     * generates a new list of item counters
     *
     * @param e an item to count
     */
    public ItemCounter(Item e) {
        super(e);
    }

    /**
     * Instantiates a new Item counter.
     *
     * @param e an item to count
     * @param x the count of that item
     */
    public  ItemCounter(Item e, int x) {
        super(e,x);
    }

    /**
     * returns the total cost for these items
     *
     * @return the unit price of the item multiplied by its count
     */
    public double cost() {
        return count*((Item)element).getUnitPrice();
    }
}

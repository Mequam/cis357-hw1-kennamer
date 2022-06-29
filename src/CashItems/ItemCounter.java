package CashItems;
import Monoids.*;

import java.util.function.Predicate;

/** a convinent container class that ONLY contains items*/
public class ItemCounter extends Monoids.Counter {
    /** generates a new list of item counters */
    public ItemCounter(Item e) {
        super(e);
    }
    public  ItemCounter(Item e, int x) {
        super(e,x);
    }
}

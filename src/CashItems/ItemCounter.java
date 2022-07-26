package CashItems;

/**
 * a convenient container class that ONLY contains items
 */
public class ItemCounter extends Monads.Counter {
    /**
     * generates a new list of item counters
     *
     * @param e an item to count
     */
    public ItemCounter(ProductSpecification e) {
        super(e);
    }

    /**
     * Instantiates a new Item counter.
     *
     * @param e an item to count
     * @param x the count of that item
     */
    public  ItemCounter(ProductSpecification e, int x) {
        super(e,x);
    }

    /**
     * returns the total cost for these items
     *
     * @return the unit price of the item multiplied by its count
     */
    public double cost() {
        return count*((ProductSpecification)element).getUnitPrice();
    }

    /**
     *
     * returns the cost of the item, taxed if it can be taxed
     *
     * @return if the item code does not start with B, return the total cost taxed, else the total cost
     * */
    public double dynamicCost() {
        if (((ProductSpecification)element).getTaxed()) {
            return taxedCost();
        }
        else {
            return cost();
        }
    }

    /**
     * gets the total cost of the given items, with tax included
     * @param tax the amount of tax used for the item
     * @return the total cost of the item with tax included
     * */
    public double taxedCost(double tax) {
        return count*((ProductSpecification)element).getUnitPrice()*(1+tax);
    }

    /**
     * static function to get the cost of an item given a count of items
     *
     * @param i the item that we want to get the cost of
     * @param count the count of that item
     * @return the cost of count items
     * */
    public static double cost(ProductSpecification i, int count) {
        return i.getUnitPrice()*count;
    }

    /**
     * static function to get the cost of an item given a count of items and the tax
     *
     * @param i the item that we want to get the cost of
     * @param count the count of that item
     * @param tax the tax rate to be using
     * @return the cost of count items
     * */
    public  static double dynamicCost(ProductSpecification i, int count, double tax) {
        if (i.getTaxed()) {
            return i.getUnitPrice() * count * (1 + tax);
        }
        return i.getUnitPrice()*count;
    }

    /**
     * returns the subtotal with a tax rate of 6%  @return the double
     *
     * @return the subtotal with a 6% tax rate
     */
    public double taxedCost(){
        return  taxedCost(0.06);
    }

}

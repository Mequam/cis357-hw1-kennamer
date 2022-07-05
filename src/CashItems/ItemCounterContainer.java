package CashItems;

import java.util.Arrays;

/**
 * an array of ItemCounters
 * this class is a CounterContainer that FORCES
 * the types input into it to be ItemCounters as apposed to generic
 * counters
 */
public class ItemCounterContainer extends Monads.CounterContainer {
    /*
     * java appears to have very ugly higher order functions
     * which makes since as its an oop language
     * */

    /**
     * generate an empty ItemCounterContainer with size memory slots
     *
     * @param size the size of the new item container
     */
    public ItemCounterContainer(int size) {
        super(size);
    }

    /**
     * generate an ItemCounterContainer from a csv file of item types
     *
     * @param file file path to a csv file containing item types
     */
    public ItemCounterContainer(String file) {
        Item [] item_list = Item.gen_item_list(file);
        data = new ItemCounter[item_list.length];

        //encapsulate each of the items that we returned in a counter container
        for (int i = 0 ; i < item_list.length; i++) {
            data[i] = new ItemCounter(item_list[i]);
        }

        //sort the data array by name
        Arrays.sort(data,(a,b) -> {return ((Item)a.element).getItemName().compareTo(((Item)b.element).getItemName());});

        for (ItemCounter c : (ItemCounter []) data)
        {
            System.out.println(((Item)c.element).getItemName());
        }
    }

    /**
     * returns the total cost of items inside the container @return the double
     *
     * @return the total cost of all objects in the container
     */
    public  double totalCost() {
        double ret_val = 0;
        for (ItemCounter ic: (ItemCounter []) data) {
            ret_val += ic.cost();
        }
        return ret_val;
    }

    /**
     * returns the subtotal with a tax rate of 6%  @return the double
     *
     * @return the subtotal with a 6% tax rate
     */
    public double taxedCost(){
        return  taxedCost(0.06);
    }

    /**
     * returns the subtotal with the given tax rate, takes percentages between 0 and 1
     *
     * @param taxRate the tax rate, using 0 and 1 percentage notation
     * @return the full cost of all items inside of the container, adjusted for a given tax rate
     */
    public double taxedCost(double taxRate)
    {
        return  totalCost() * (1+taxRate);
    }
    /** returns the item counter container as a string formatted for user eyes
     *
     * @return this object formatted as a string for the end user*/
    @Override
    public  String toString() {
        String ret_val = "Items List:\n";
        for (ItemCounter ic: (ItemCounter[])data) {
            if (ic.count != 0) {
                ret_val += String.format("   %-23s$ %.2f\n", ic.toString(), ic.cost());
            }
        }
        ret_val += String.format("%-26s$ %.2f\n","Subtotal",totalCost());
        ret_val += String.format("%-26s$ %.2f","Total With Tax (6%)",taxedCost());

        return ret_val;
    }

    /**
     * returns true if the object types contain the given id @param id the id
     *
     * @param id the type id to test for
     * @return returns true if the container has a type of id inside it
     */
    public boolean contains(int id) {
        return  !java.util.Objects.isNull(
                    testMap(
                        (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCode() == id;},
                        (j)->{ return true;} )
            );//if we return anything from our generic array search, then we are not null and we need to be true
    }

    /**
     * Gets the amount of the given item type
     *
     * @param id id of the type to count
     * @return the count of the given type stored inside of the container
     */
    public  int get_type_amount(int id) {
        return (int)testMap(
                (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCode() == id;},
                (j)->{ return (((ItemCounter)j).count);} );
    }

    /**
     * Gets the name of the given item type  @param id the id
     *
     * @param id the id of a type stored inside of the container
     * @return the type name, null if no type matching id exists
     */
    public String get_type_name(int id) {
        return (String) testMap(
                (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCode() == id;},
                (j)->{ return ((Item)((ItemCounter)(j)).element).getItemName();}
        );
    }

    /**
     * gets the cost of the give type  @param id the id
     *
     * @param id the type id to find inside of the container
     * @return the type cost, null if no type matching id exists
     */
    public  double get_type_cost(int id) {
        return (double)testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return ((Item) ((ItemCounter) (k)).element).getUnitPrice();
                }
        );
    }

    /**
     * gets the ItemCounter in the container that has the given id @param id the id
     *
     * @param id the type id we are searching for
     * @return the item counter matching id, null otherwise
     */
    public  ItemCounter get_counter(int id){
        return (ItemCounter) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return k;
                }
        );
    }
    /*TODO: the other get_x functions could be re-writen as return get_item(id).x; and some null checking*/

    /**
     * gets the item inside the container containing the given id @param id the id
     *
     * @param id the tyype id we are searching for
     * @return the item matching id, null otherwise
     */
    public Item get_item(int id) {
        return (Item) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return ((ItemCounter)k).element;
                }
        );
    }
}

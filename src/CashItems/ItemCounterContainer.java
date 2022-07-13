package CashItems;

import Monads.Counter;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

/**
 * an array of ItemCounters
 * this class is a CounterContainer that FORCES
 * the types input into it to be ItemCounters as apposed to generic
 * counters
 */
public class ItemCounterContainer extends Monads.CounterContainer {


    /**
     * tells the item counter whether to sort data on add,
     * used for efficiency if we do not care about sorting
     * */
    public boolean doSort = true;

    /** utility exception that is thrown when the given item is not found*/
    public static class ItemCodeNotInContainerException extends Exception {

    }



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
        this(new File(file));
    }

    /**
     * initilizes a sort of the data contained inside of our data variable
     * */
    public void sortData() {
        Collections.sort(data,(a, b) -> {return ((Item)a.element).getItemName().compareTo(((Item)b.element).getItemName());});
    }

    /**
     * reference to the file containing the data in the object
     * */
    java.io.File dataFile;

    /**
     * saves the types in the container out to the file that we originally read from
     * */
    public void save() {
        save(dataFile);
    }
    /**
     * saves the types in the container out to the disk
     *
     * @param f the file that we save to
     * */
    public  void save(java.io.File f) {
        FileWriter fw;
        try {
          fw = new FileWriter(f);

          for (int i = 0; i < data.size() ; i ++) {
              fw.write(((Item)((ItemCounter)data.get(i)).element).to_csv_line() + "\n");
          }

          fw.close();
        }
        catch (Exception e) {

        }

    }

    /**
     *
     * initilizes the lass using data from a csv file pointed to by f
     * */
    public ItemCounterContainer(java.io.File f) {
        dataFile = f;
        Item [] item_list = Item.gen_item_list(f);

        data = new ArrayList<Counter>();

        //encapsulate each of the items that we returned in a counter container
        for (Item i : item_list) {
            data.add(new ItemCounter(i));
        }

        if (doSort) {
            //sort the data array by name
            Collections.sort(data, (a, b) -> {
                return ((Item) a.element).getItemName().compareTo(((Item) b.element).getItemName());
            });
        }
        /*

            for debugging purposes, to see the items loaded, uncomment the following code

            for (ItemCounter c : (ItemCounter []) data)
            {
                System.out.println(((Item)c.element).getItemName());
            }
       */


    }

    /**
     * returns the total cost of items inside the container @return the double
     *
     * @return the total cost of all objects in the container
     */
    public  double totalCost() {
        double ret_val = 0;
        for (int i = 0; i < data.size(); i++) {
            ret_val += ((ItemCounter)data.get(i)).cost();
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

        double ret_val = 0;
        for (int i = 0; i < data.size(); i++) {
            ret_val += ((ItemCounter)data.get(i)).dynamicCost();
        }
        return ret_val;
    }
    /** returns the item counter container as a string formatted for user eyes
     *
     * @return this object formatted as a string for the end user*/
    @Override
    public  String toString() {
        String ret_val = "Items List:\n";
        for (int i = 0; i < data.size(); i++) {
            //cache a reference to the data for convenience
            ItemCounter ic = (ItemCounter) data.get(i);

            if (ic.count != 0) {
                ret_val += String.format("   %-23s$ %.2f\n", ic.toString(), ic.cost());
            }
        }
        ret_val += String.format("%-26s$ %.2f\n","Subtotal",totalCost());
        ret_val += String.format("%-26s$ %.2f","Total With Tax (6%)",taxedCost());

        return ret_val;
    }

    /**
    * removes the counter with the given item code
     *
     * @param itemCode the item code of the counter that we want to remove
    * */
    public  void remove(Item.ItemCode itemCode) {
        remove(itemCode.getValue());
    }
    /**
     * removes the counter with the given item code string
     *
     * @param itemCode the item code of the counter that we want to remove
     * */
    public void remove(String itemCode) {
        remove(get_counter(itemCode));
    }

    /***
     * overide the parent remove function to contain sorting behavior
     *
     * @param idx index of a counter to remove from the container
     */
    @Override
    public void remove(int idx) {
        super.remove(idx);
        if (doSort) {
            sortData();
        }
    }

    /**
     * overide the parent remove function to contain sorting behavior
     *
     * @param i counter to remove
     * */
    @Override
    public void remove(Counter i) {
        super.remove(i);
        if (doSort) {
            sortData();
        }
    }

    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code bieng any item code stored within the container
     *
     * @return the item code that the user typed
     * */

    public Item.ItemCode askContainedItemCode() {
        return askContainedItemCode("Enter Item Code:",Item.DEFAULT_FORMATING);
    }

    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code bieng any item code stored within the container
     *
     * @param question display question to ask the user
     * @return the item code that the user typed
     * */

    public Item.ItemCode askContainedItemCode(String question) {
        return askContainedItemCode(question,Item.DEFAULT_FORMATING);
    }
    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code bieng any item code stored within the container
     *
     * @param question display question to ask the user
     * @param formating the format string for that question
     * @return the item code that the user typed
     * */
    public Item.ItemCode askContainedItemCode(String question, String formating) {
        Item.ItemCode code = Item.ItemCode.askItemCode(question,formating);

        while (!contains(code)) {
            System.out.printf(formating,"!!Item Code Does Not Exist\n");
            code = Item.ItemCode.askItemCode(question,formating);
        }

        return code;
    }
    /** adds a new Item Counter to the container
     * @param i an item counter to add*/
    public  void add(ItemCounter i) {
        data.add((Counter) i);
        if (doSort) {
            sortData();
        }
    }
    public void add( Item i ) {
        ItemCounter to_add = new ItemCounter(i);
        data.add(to_add);
        if (doSort) {
            sortData();
        }
    }

    public String type_display_string() {
        String ret_val = String.format(Item.DISPLAY_STRING_FORMAT + "\n","Item Code","Item Name","Unit Price");
        for (int i = 0; i < data.size(); i++) {
            ItemCounter ic = ((ItemCounter) data.get(i));
            ret_val += ((Item)ic.element).display_string() + "\n";
        }
        return ret_val;
    }

    /**
     * returns true if the object types contain the given id @param id the id
     *
     * @param itemCode the item code object that we will test for
     * @return returns true if the container has a type of id inside it
     */
    public boolean contains(Item.ItemCode itemCode) {
        return contains(itemCode.getValue());
    }


    /**
     * returns true if the object types contain the given id @param id the id
     *
     * @param id the type id to test for
     * @return returns true if the container has a type of id inside it
     */
    public boolean contains(String id) {
        return  !java.util.Objects.isNull(
                    testMap(
                        (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCodeString().equals(id);},
                        (j)->{ return true;} )
            );//if we return anything from our generic array search, then we are not null and we need to be true
    }
    /**
     * simple function that throws an exception if we do NOT contain the given item or if the given item code is invalid
     *
     * @param id the item id that we are looking for
     * */
    public void exceptionCheckItemCode(String id)
            throws ItemCodeNotInContainerException,
            Item.ItemCode.MalformedItemCodeException {

        //throw the malformedItemCodeException if id is invalid
        Item.ItemCode.malformedItemExceptionCheck(id);

        if (!contains(id)) {
            throw new ItemCodeNotInContainerException();
        }
    }

    /**
     * Gets the amount of the given item type
     *
     * @param id id of the type to count
     * @return the count of the given type stored inside of the container
     */
    public  int get_type_amount(String id) {
        return get_counter(id).count;
    }

    /**
     * Gets the name of the given item type  @param id the id
     *
     * @param id the id of a type stored inside of the container
     * @return the type name, null if no type matching id exists
     */
    public String get_type_name(String id) {
        return get_item(id).getItemName();
    }

    /**
     * gets the cost of the give type  @param id the id
     *
     * @param id the type id to find inside of the container
     * @return the type cost, null if no type matching id exists
     */
    public  double get_type_cost(String id) {
        return get_item(id).getUnitPrice();
    }

    /**
     * gets the ItemCounter in the container that has the given id @param id the id
     *
     * @param id the type id we are searching for
     * @return the item counter matching id, null otherwise
     */
    public  ItemCounter get_counter(String id){
        return (ItemCounter) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCodeString().equals(id);
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
    public Item get_item(String id) {
        return (Item) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCodeString().equals(id);
                },
                (k) -> {
                    return ((ItemCounter)k).element;
                }
        );
    }
}

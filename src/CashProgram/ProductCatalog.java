package CashProgram;
import CashItems.Item;
import CashItems.ItemCounter;
import Monads.Counter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

/**
 * this class represents the loaded product catalog that we read from the disc
 * */
public class ProductCatalog {

    public boolean doSort = true;
    /** adds a new Item Counter to the container
     * @param i an item counter to add*/
    public void add(Item i) {
        if (!contains(i.getItemCode())) {
            item_list.add(i);
            if (doSort) {
                sortData();
            }
        }
    }

    /**
     * initilizes a sort of the data contained inside of our data variable
     * */
    public void sortData() {
        Collections.sort(item_list,(a, b) -> {return a.getItemName().toLowerCase().compareTo(b.getItemName().toLowerCase());});
    }

    /**
     * removes the counter with the given item code
     *
     * @param itemCode the item code of the counter that we want to remove
     * */
    public  void remove(Item.ItemCode itemCode) {
        remove(getSpecification(itemCode));
    }
    /**
     * removes the given item from the list
     *
     * */
    public void remove(Item i) {
        item_list.remove(i);
    }

    /**
     * prompts the user to update the data contained within the item container
     *
     * */
    void askUpdateData() {
        Scanner inScan = new Scanner(System.in);

        System.out.print("Do you want to update the item data? (A/D/M/Q): ");
        String response = inScan.nextLine();

        while (!response.toUpperCase().equals( "Q")) {

            switch (response.toUpperCase()) {
                case "A" -> {
                    //create a new item using user inputs
                    Item i = new Item();
                    add(i); // add it

                    System.out.println("Item addition successful!\n");
                }
                case "D" -> {
                    remove(askContainedItemCode());

                    System.out.println("Item delete successful!\n");
                }
                case "M" -> {
                    Item.ItemCode target = askContainedItemCode(); // get an item code from the collection
                    Item ni = new Item(target); //make an ew item with that code, asks the user for input
                    remove(ni.getItemCode()); //remove the existing item with the matching code

                    add(ni); //add and sort the new item
                    sortData();

                    System.out.println("Item modification successful!\n");
                }
            }

            System.out.print("Do you want to update the item data? (A/D/M/Q): ");
            response = inScan.nextLine();
        }
    }

    /**
     * gets a string representing the items contained in this container and inteanded
     * to be seen directly by the user
     *
     * */
    public String type_display_string() {
        String ret_val = String.format(Item.DISPLAY_STRING_FORMAT + "\n","Item Code","Item Name","Unit Price");
        for (int i = 0; i < item_list.size(); i++) {
            Item saleItem = item_list.get(i);
            ret_val += saleItem.display_string() + "\n";
        }
        return ret_val;
    }

    private static ArrayList<Item> item_list;

    java.io.File dataFile;
    public ProductCatalog(File f) {
        dataFile = f;
        item_list = Item.gen_item_linked_list(f);
        if (doSort) {
            sortData();
        }
    }
    public ProductCatalog(String f) {
        this(new File(f));
    }

    /**
    * returns the specification of the item with the given item id
    * */
    Item getSpecification(Item.ItemCode id) {
        for (Item i : item_list) {
            if (i.getItemCode().equals(id)) {
                return i;
            }
        }
        //with any luck we should never get here
        return null;
    }

    /**
     * returns true if we contain the given item code
     * */
    public boolean contains(Item.ItemCode id) {
        return getSpecification(id) != null;
    }

    /**
     * Gets the name of the given item type  @param id the id
     *
     * @param id the id of a type stored inside of the container
     * @return the type name, null if no type matching id exists
     */
    public String get_type_name(Item.ItemCode id) {
        return getSpecification(id).getItemName();
    }


    /**
     * return true if we contain the given product code
     * */
    public boolean contains(String itemCode) {
        //short circiting will make us return false if we get an invalid item code, WITHOUT iterating over the loop
        return Item.ItemCode.validItemCode(itemCode)
                && contains(new Item.ItemCode(itemCode));
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

    void save() {
        save(dataFile);
    }
    void save(java.io.File f) {

        FileWriter fw;
        try {
            fw = new FileWriter(f);

            for (int i = 0; i < item_list.size() ; i ++) {
                fw.write(item_list.get(i).to_csv_line() + "\n");
            }

            fw.close();
        }
        catch (Exception e) {

        }
    }
    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code being any item code stored within the container
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

}

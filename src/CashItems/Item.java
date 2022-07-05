package CashItems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import Monads.*;

import java.lang.reflect.Array;

/**
 * This class represents a TYPE of item, it is intended to be used as a programmatic type, much
 * like pythonic tuples. As such it is an IMMUTABLE type, and cannot be changed after creation
 */
public class Item {
    /** this function initializes an item, we use it apposed to the constructor so we can call it on lines OTHER than the first one*/
    private  void load(int code, String name, double unitPrice) {
        this.itemCode = code;
        this.itemName = name;
        this.unitPrice = unitPrice;
    }

    /**
     * generates a new item variable
     *
     * @param code      the unique id identifying this item type
     * @param name      the name of the given item
     * @param unitPrice how much a single item costs
     */
    public Item(int code,String name,double unitPrice) {
        load(code,name,unitPrice);
    }

    /**
     * generates a new Item from a csv line entry
     *
     * @param csvLine csv line representing this item, in the form: id,name,unitPrice
     */
    public Item(String csvLine) {
        String [] split_csv = csvLine.split(",");
        load(
                Integer.parseInt(split_csv[0]),
                split_csv[1],
                Double.parseDouble(split_csv[2])
        );
    }

    /**
     * gets the number of item entries in a given file, useful for pre-building arrays
     *
     * @param f file path to a csv file containing item entries
     * @return the number of item entries inside a csv file
     */
    public static int gen_item_entry_count(String f) {
        try {
            File file = new File(f);
            return gen_item_entry_count(file);
        }
        catch (Exception e){
            return 0;
        }
    }
    /** used to get the display name of the item, convince function*/
    @Override
    public String toString() {
        return getItemName();
    }

    /**
     * gets the number of item entries in a given file, useful for pre-building arrays
     *
     * @param f file object pointing to a csv file containing item entries
     * @return the number of item entries in a csv file
     */
    public static int gen_item_entry_count(File f) {
        int lineCount = 0;
        try {
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                s.nextLine();
                lineCount ++;
            }
        }
        catch ( Exception e) {
            System.out.print(e);
        }
        System.out.println("returning line count of : " + Integer.toString(lineCount));
        return lineCount;
    }

    /**
     * returns an array of items generated from the given csv file
     *
     * @param f file object to read from
     * @return an array of items contained in the given file
     */
    public static Item[] gen_item_list(File f) {
        /*
            for the time bieng this is a debug list until this feature is ironed out
        * */
        try {
            int lineCount = gen_item_entry_count(f);
            System.out.println(lineCount);
            //initilize the return value array
            Item [] ret_val = new Item[lineCount];
            System.out.println("return value length: " + Integer.toString(ret_val.length));
            Scanner s = new Scanner(f);

            int line = 0;
            while (s.hasNextLine()) {
                String data = s.nextLine();
                //generate a new item from our csv line
                ret_val[line] = new Item(data);
                System.out.println(ret_val[line].toString());
                line ++;
            }

            return ret_val;
        }
        catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("MISTAKES WERE MADE");
            return new Item[] {};
        }
        /*
        return new Item [] {
                new Item(1,"bottled water",1.50),
                new Item(1,"bottled water",1.50) ,
                new Item(2,"candy",1.00),
                new Item(3,"chocolate",2.50),
                new Item(4,"gum",1.00)
        };
*/

    }

    /**
     * generates and returns a new array of item types from the given file path
     *
     * @param fpath the file path to a csv file containing item types
     * @return an array of items generated from the given csv file
     */
    public static Item [] gen_item_list(String fpath) {
        try {
            File f = new File(fpath);
            return gen_item_list(f);
        } catch (Exception e) {
            return new Item [] {};
        }
    }

    /** The integer code for a given item */
    private int itemCode = 0;

    /**
     * getter for item code
     *
     * @return the item code
     */
    public int getItemCode() {
        return itemCode;
    }
    /** the name of this item */
    private String itemName;

    /**
     * the getter for the item name
     *
     * @return the item name
     */
    public String getItemName() {
        return itemName;
    }
    /** the price of a single unit of this item*/
    private double unitPrice;

    /**
     * the getter for the unitPrice of this item
     *
     * @return the unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }
}

package CashItems;

import java.io.File;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a TYPE of item, it is intended to be used as a programmatic type, much
 * like pythonic tuples. As such it is an IMMUTABLE type, and cannot be changed after creation
 */
public class Item {
    /**
     * represents an item code used to uniquely identify each item
     *
     * contains several convenience functions for working with strings in the desired format
     * */
    static public class ItemCode {
        /**Custom exception that is called if a malformed item code string is given as an item code*/
        static public class MalformedItemCodeException extends IllegalArgumentException {

        }

        /**
         * The string that actually represents our item code
         * */
        private String value = null;

        /**
         * getter for our value
         *
         * @return the string value of this item code potentially null if it is not set
         * */
        public String getValue() {
            return value;
        }


        /**
         * determines if the given string is a valid item code
         * @param s string to test
         * @return true if the given s is a valid item code
         * */
        public boolean validItemCode(String s) {
            Pattern p = Pattern.compile("[A-Za-z][A-Za-z\\d]\\d\\d",Pattern.DOTALL);
            Matcher m = p.matcher(s);

            return  m.matches();
        }
       /**
        * throws a new MalformedItemExpression if the string s is not valid
        *
        * @param s the string to validate
        * */
        public void malformedItemExceptionCheck(String s)
                throws MalformedItemCodeException {
            if (!validItemCode(s)) {
                throw new MalformedItemCodeException();
            }
        }
        ItemCode(String s) {
            //throw an exception if the given string is not valid
            malformedItemExceptionCheck(s);

            //otherwise set value equal to s
            value = s;
        }
    }
    /** this function initializes an item, we use it apposed to the constructor so we can call it on lines OTHER than the first one*/
    private  void load(String code, String name, double unitPrice) {
        this.itemCode = new ItemCode(code);
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
    public Item(String code,String name,double unitPrice) {
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
                split_csv[0].trim(),
                split_csv[1].trim(),
                Double.parseDouble(split_csv[2].trim())
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

            //initilize the return value array
            Item [] ret_val = new Item[lineCount];
            Scanner s = new Scanner(f);

            int line = 0;
            while (s.hasNextLine()) {
                String data = s.nextLine();
                //generate a new item from our csv line
                ret_val[line] = new Item(data);
                line ++;
            }

            return ret_val;
        }
        catch (Exception e) {
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
    private ItemCode itemCode = new ItemCode("A001");

    /**
     * getter for item code string
     *
     * @return the item code as a string
     */
    public String getItemCodeString() {
        return itemCode.value;
    }
    /**
     * getter for the item code object
     *
     * @return the item code object representing our code
     * */
    public ItemCode getItemCode() {
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

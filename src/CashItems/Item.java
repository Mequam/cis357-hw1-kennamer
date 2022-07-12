package CashItems;

import java.awt.*;
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
        /**
         * tests if two item codes are the same data
         *
         * @param ic Item code to test
         * */
        public  boolean equals(ItemCode ic) {
            return ic.value.equals(value);
        }
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
        public static boolean validItemCode(String s) {
            Pattern p = Pattern.compile("[A-Za-z][A-Za-z\\d]\\d\\d",Pattern.DOTALL);
            Matcher m = p.matcher(s);

            return  m.matches();
        }
       /**
        * throws a new MalformedItemExpression if the string s is not valid
        *
        * @param s the string to validate
        * */
        public static void malformedItemExceptionCheck(String s)
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


        /**
         * demands a valid item code formating from the user
         * */
        static ItemCode askItemCode(String question,String formating) {
            Scanner inscan = new Scanner(System.in);
            System.out.print(String.format(formating,question));
            try {
                String itemCode = inscan.nextLine();
                return new ItemCode(itemCode);

            }
            catch (MalformedItemCodeException e) {
                System.out.println("!!Invalid Data Type");
                return askItemCode(question,formating); //hideous recursion trick to demand a valid item code
                                                        //also experimenting with recursion in java
            }



        }
    }
    /** this function initializes an item, we use it apposed to the constructor so we can call it on lines OTHER than the first one*/
    private  void load(String code, String name, double unitPrice) {
        this.itemCode = new ItemCode(code);
        this.itemName = name;
        this.unitPrice = unitPrice;
    }

   /**generates a new item variable by asking the user for information
    * */
    public  Item() {
        fill_using_ask();
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
     * queries the user to give us a new item
     * sets the values of the current item to the given query
     * */
    public void fill_using_ask() {
        fill_using_ask(DEFAULT_FORMATING);
    }
    /**
     * queries the user to give us a new item
     * sets the values of the current item to the given query
     * */
    public void fill_using_ask(String formating) {
        askCode("Item Code:",formating);
        askName("Item Name:",formating);
        askUnitPrice("Unit Price:",formating);
    }
    void askCode(String question) {
        askCode(question,DEFAULT_DOUBLE_FORMATING);
    }
    /**queries the user to set the item code for this item*/
    void askCode(String question,String formating) {
        itemCode = ItemCode.askItemCode(question,formating);
    }
    /**queries the user to ask about the name of the item*/
    void askName(String question) {
        askName(question,DEFAULT_DOUBLE_FORMATING);
    }
    /**queries the user to ask about the name of the item*/
    void askName(String question,String formating) {
        System.out.print(String.format(formating,question));

        Scanner inscan = new Scanner(System.in);

        itemName = inscan.nextLine();
    }

    public  static final String DEFAULT_FORMATING = "%-26s";
    public static final String DEFAULT_DOUBLE_FORMATING = DEFAULT_FORMATING;

    public  static double askDouble() {
        return askDouble("Unit Price:");
    }
    public  static  double askDouble(String question) {
        return askDouble(question, "!!invalid decimal\n" + question);
    }

    public  static  double askDouble(String question, String angry_question) {
        return askDouble(question, angry_question, DEFAULT_DOUBLE_FORMATING, DEFAULT_DOUBLE_FORMATING);
    }

    public  static  double askDouble(String question, String angry_question,String formating) {
        return askDouble(question,angry_question,formating,formating);
    }
    /**
     * demands a valid double input from the user, used for unit price
     *
     * @param question       the question to ask the user
     * @param angry_question the angry question to ask the user when they give us an invalid double
     * @param formating the formating used in the base question
     * @param angry_formating the formating used if the user gives us incorrect data
     * @return a valid double typed by the user
     */
    public  static  double askDouble(String question, String angry_question,String formating,String angry_formating) {
        Scanner inscan = new Scanner(System.in);

        System.out.print(String.format(formating,question));

        String a = inscan.nextLine();

        boolean good_responce = false;

        double ret_val = 0;
        while (!good_responce) {
            try {
                ret_val = Double.parseDouble(a);
                good_responce = true;
            }
            catch (Exception e) {
                System.out.println(String.format(angry_formating,angry_question));
                a = inscan.nextLine();
            }
        }
        return ret_val;
    }

    /**queries the user to ask about the price of the item*/
    void askUnitPrice(String question,String formating) {
        unitPrice = askDouble(question,"Invalid Unit Price!!\n" + question,formating);
    }
    //formating used for the display string
    protected static final String  DISPLAY_STRING_FORMAT = "%-12s%-24s%-12s";

    /**returns a string representation of the item formated for user display
     *
     * @return returns the item displayed as a formated string
     * */
    public  String display_string() {
        return String.format(DISPLAY_STRING_FORMAT,getItemCodeString(),getItemName(),getUnitPrice());
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

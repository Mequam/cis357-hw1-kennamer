package CashProgram.CashItems;

import CashProgram.askUtils.AskUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a TYPE of item, it is intended to be used as a programmatic type, much
 * like pythonic tuples. As such it is an IMMUTABLE type, and cannot be changed after creation
 */
public class ProductSpecification {

    /**
     * @return is this item taxed?
     * */
    public boolean getTaxed() {
        return getItemCode().getTaxed();
    }
    /**
     * represents an item code used to uniquely identify each item
     *
     * contains several convenience functions for working with strings in the desired format
     * */
    static public class ItemCode {
        public static final Charset encoding = StandardCharsets.US_ASCII;
        public static final int encoding_size = 4;
        public byte [] encode() {

            return value.getBytes(encoding);
        }

        public ItemCode(byte [] data) {
            this(new String(data,encoding));
        }
        /**
         * @return is this an item code that we need to tax?
         * */
        public boolean getTaxed() {
            return (value.charAt(0) != 'B');
        }

        /**
         * tests if two item codes are the same data
         *
         * @param ic Item code to test
         * */
        @Override
        public  boolean equals(Object ic) {
            return ic instanceof ItemCode && ((ItemCode)ic).value.equals(value);
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

            return  m.matches() || s.equals("0000"); //0000 is a special command code, indicating a wild card item
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
        /**
         * creates a new item code with the given value string,
         * throws an exception if s is not a valid item code
         *
         * @param s string to set our value to
         * */
        public ItemCode(String s) {
            //throw an exception if the given string is not valid
            malformedItemExceptionCheck(s);

            //otherwise set value equal to s
            value = s;
        }

        /**
         * used to indicate if THIS item code is a special control code
         * */
        public boolean isControlCode() {
            return ProductSpecification.ItemCode.isControlCode(value);
        }
        /**
         * used to tell if a string is a valid item code
         * */
        public static boolean isControlCode(String s) {
            return s.equals("0000");
        }
        /**
         * demands a valid item code formating from the user
         * */
        public static ItemCode askItemCode(String question,String formating) {
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
    public ProductSpecification() {
        fill_using_ask();
    }

    /**
     * generates a new item with the given code,
     * asks the user for the item name and unit price
     * as ALL items must have that information
     * */
    public ProductSpecification(ItemCode ic) {
        itemCode = ic;
        askName("Item Name:");
        askUnitPrice("Unit Price:");
    }
    @Override
    public boolean equals(Object i) {
        return i instanceof ProductSpecification && ((ProductSpecification) i).getItemCode().equals(itemCode);
    }

    /**
     * generates a new item variable
     *
     * @param code      the unique id identifying this item type
     * @param name      the name of the given item
     * @param unitPrice how much a single item costs
     */
    public ProductSpecification(String code, String name, double unitPrice) {
        load(code,name,unitPrice);
    }

    /**
     * generates a new Item from a csv line entry
     *
     * @param csvLine csv line representing this item, in the form: id,name,unitPrice
     */
    public ProductSpecification(String csvLine) {
        String [] split_csv = csvLine.split(",");
        load(
                split_csv[0].trim(),
                split_csv[1].trim(),
                Double.parseDouble(split_csv[2].trim())
        );
    }

    /**
     * returns the item represented as a csv line
     * */
    public String to_csv_line() {
        return String.format("%s,%s,%.2f",getItemCodeString(),getItemName(),getUnitPrice());
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
     * writes the list of types out to the disc as a csv file
     * */
    public static void save_list_csv(String f,ArrayList<ProductSpecification> data) {
        FileWriter fw;

        try {
            fw = new FileWriter(f);
            for (int i = 0; i < data.size() ; i ++) {
                fw.write(data.get(i).to_csv_line() + "\n");
            }
            fw.close();
        }
        catch (Exception e) {

        }
    }
    /**
     * returns a linked list containing items generated from a csv file
     * */
    public static ArrayList<ProductSpecification> gen_item_linked_list(String f) {
        return gen_item_linked_list(new File(f));
    }

    /**
     * returns the number of bytes in a full binary encoding of this productSpecification
     * */
    int encodingSize() {
        return ItemCode.encoding_size + 8 + getItemName().length() + 1;
    }

    private byte[] encodeName() {
        byte[] itemNameBytes = itemName.getBytes(ItemCode.encoding);
        byte [] ret_val = new byte[itemNameBytes.length + 1];
        for (int i = 0; i < itemNameBytes.length;i++) {
            ret_val[i+1] = itemNameBytes[i];
        }
        ret_val[0] = (byte) itemNameBytes.length;
        return ret_val;
    }

    /**
     * sets the item name from a give entry of full data
     * */
    private void decodeName(byte [] full_data) {
        byte[] buffer = new byte[
                Byte.toUnsignedInt(full_data[ItemCode.encoding_size + Double.BYTES])
                ];
        for (int i = 0; i < buffer.length;i++) {
            buffer[i] = full_data[i+ItemCode.encoding_size+Double.BYTES+1];
        }
        itemName = new String(buffer,ItemCode.encoding);
    }
    /**
     * returns bytes representing the raw encoding of this ProductSpec
     *
     *
     * encoding format is in the form
     * item ID / Unit Price / name where name is null ended
     * see item ID.encode for more information on encoding the item IDs
     * */
    public byte[] encode() throws IOException{
        byte [] ret_val = new byte[encodingSize()];

        byte [] item_code = getItemCode().encode();
        byte [] d_arr = unitPriceToByteArray();

        for (int i = 0; i < item_code.length;i++) {
            ret_val[i] = item_code[i];
        }
        for (int i = item_code.length; i < d_arr.length;i++) {
            ret_val[i] = d_arr[i-item_code.length];
        }

        byte [] nameData = encodeName();

        for (int i = 0; i < nameData.length;i++) {
         ret_val[i + d_arr.length + item_code.length] = nameData[i];
        }

        for (byte b: ret_val) {
            System.out.println((char)b);
        }
        return ret_val;
    }

    private byte[] unitPriceToByteArray() throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(Double.BYTES);
        bb.putDouble(unitPrice);
        return bb.array();
    }
    /**
     * sets the item code from a given entry of full data
     * */
    private void decodeItemCode(byte [] fullData) {
        byte[] ic = new byte[ItemCode.encoding_size];
        for (int i = 0; i < ItemCode.encoding_size;i++) {
            ic[i] = fullData[i];
        }
        itemCode = new ItemCode(ic);
    }
    private void decodeUnitPrice(byte [] fullData) {
        byte [] unitPriceArr = new byte[Double.BYTES];
        for (int i = ItemCode.encoding_size; i < Double.BYTES+ItemCode.encoding_size;i++) {
            unitPriceArr[i-ItemCode.encoding_size] = fullData[i];
        }
        unitPrice = ByteBuffer.wrap(unitPriceArr).getDouble();
    }
    public ProductSpecification(byte[] data) {
        decodeItemCode(data);
        decodeUnitPrice(data);
        decodeName(data);
    }

    public ProductSpecification(RandomAccessFile raf) throws IOException{
        this(readItemBytes(raf));
    }

    /**
     * reads the bytes of an item in from a random access file and returns them
     * */
    public static byte [] readItemBytes(RandomAccessFile raf) throws IOException {
        //stores the first half of an item entry
        byte[] initial = new byte[ItemCode.encoding_size + Double.BYTES];
        raf.read(initial);

        byte[] len = new byte[1];
        raf.read(len);

        byte[] name = new byte[Byte.toUnsignedInt(len[0])];
        raf.read(name);

        byte[] ret_val = new byte[initial.length + len.length + name.length];
        //copy over the inital data
        for (int i = 0; i < initial.length;i++) {
            ret_val [i] = initial[i];
        }
        //store the length of the name in our bytes
        ret_val[initial.length] = len[0];
        //copy over the name into our bytes
        for (int i = 0; i < len[0];i++) {
            ret_val[i+initial.length+1] = name[i];
        }

        return ret_val;
    }
    public static ArrayList<ProductSpecification> gen_item_linked_list(RandomAccessFile f) throws IOException {
        ArrayList<ProductSpecification> ret_val = new ArrayList<ProductSpecification>();
        while (f.getFilePointer() != f.length()) {
            ret_val.add(new ProductSpecification(f));
        }
        return ret_val;
    }

    /**
     * returns an array list (ll behind the scenes)
     * containing the items in the given csv file
     * */
    public static ArrayList<ProductSpecification> gen_item_linked_list(File f) {
        try {
            ArrayList<ProductSpecification> ret_val = new ArrayList<ProductSpecification>();

            Scanner s = new Scanner(f);
            int line = 0;
            while (s.hasNextLine()) {
                String data = s.nextLine();
                //generate a new item from our csv line
                ret_val.add(new ProductSpecification(data));
                line ++;
            }
            return ret_val;
        }
        catch (Exception e) {
            return new ArrayList<ProductSpecification>();
        }
    }

    /**
     * returns an array of items generated from the given csv file
     *
     * @param f file object to read from
     * @return an array of items contained in the given file
     */
    public static ProductSpecification[] gen_item_list(File f) {

        try {
            int lineCount = gen_item_entry_count(f);

            //initilize the return value array
            ProductSpecification[] ret_val = new ProductSpecification[lineCount];
            Scanner s = new Scanner(f);

            int line = 0;
            while (s.hasNextLine()) {
                String data = s.nextLine();
                //generate a new item from our csv line
                ret_val[line] = new ProductSpecification(data);
                line ++;
            }

            return ret_val;
        }
        catch (Exception e) {
            return new ProductSpecification[] {};
        }
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

    /**
     * asks the user to type a valid item code string
     *
     * @param question the question to display to the user
     * */
    void askCode(String question) {
        askCode(question,DEFAULT_DOUBLE_FORMATING);
    }
    /**queries the user to set the item code for this item
     *
     * @param question the string to display to the user
     * @param formating the formating to use when displaying that question
     * */
    void askCode(String question,String formating) {
        itemCode = ItemCode.askItemCode(question,formating);
    }
    /**queries the user to ask about the name of the item
     * @param question the question to display to the user
     * */
    void askName(String question) {
        askName(question,DEFAULT_DOUBLE_FORMATING);
    }
    /**queries the user to ask about the name of the item
     * @param question the question to display to the user
     * @param formating how to format the question
     * */
    void askName(String question,String formating) {
        System.out.print(String.format(formating,question));

        Scanner inscan = new Scanner(System.in);

        itemName = inscan.nextLine();
    }

    /**
     * default formating used when generating strings to display for items
     * */
    public  static final String DEFAULT_FORMATING = "%-26s";
    /**
     * default formating for doubles to display from the item class,
     * currently the same as DEFAULT_FORMATING
     * */
    public static final String DEFAULT_DOUBLE_FORMATING = DEFAULT_FORMATING;

    /**
     * demand a valid double from the user
     * @return a double that the user typed
     * */
    public  static double askDouble() {
        return askDouble("Unit Price:");
    }
    /**
     * demand a valid double from the user
     * @param question the question to ask
     * @return a double that the user typed
     * */
    public  static  double askDouble(String question) {
        return askDouble(question, "!!invalid decimal\n" + question);
    }

    /**
     * demand a valid double from the user
     * @param question the string to display to the user
     * @param angry_question the string to display to the user if they type invalid data
     * @return a double the user typed
     */
    public  static  double askDouble(String question, String angry_question) {
        return askDouble(question, angry_question, DEFAULT_DOUBLE_FORMATING, DEFAULT_DOUBLE_FORMATING);
    }

    /**
     * demand a valid double from the user
     * @param question the string to display to the user
     * @param angry_question the string to display to the user if they type invalid data
     * @param formating how to format the question first question that is asked to the user
     * @return a double the user typed
     * */
    public  static  double askDouble(String question, String angry_question,String formating) {
        return askDouble(question,angry_question,formating,formating);
    }
    /**
     * demands a valid double input from the user, used for unit price
     *
     * goodness gracious java needs to add optional parameters
     *
     * @param question       the question to ask the user
     * @param angry_question the angry question to ask the user when they give us an invalid double
     * @param formating the formating used in the base question
     * @param angry_formating the formating used if the user gives us incorrect data
     * @return a valid double typed by the user
     */
    public  static  double askDouble(String question, String angry_question,String formating,String angry_formating) {
        return AskUtils.askDouble(question,angry_question,formating,angry_formating);
    }

    /**
     *
     * @param question the question to display to the user when we ask for a unit price
     * */
    void askUnitPrice(String question) {
        askUnitPrice(question, ProductSpecification.DEFAULT_FORMATING);
    }
    /** queries the user to ask about the price of the item
     *
     * @param question the question to display to the user when we ask for a unit price
     * @param formating the formating used to display the question
     * */
    void askUnitPrice(String question,String formating) {
        unitPrice = askDouble(question,"Invalid Unit Price!!\n" + question,formating);
    }
    //formating used for the display string
    /**default formating used when making a display string of ourselfs for the user*/
    public static final String  DISPLAY_STRING_FORMAT = "%-12s%-24s%-12s";

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
    public static ProductSpecification[] gen_item_list(String fpath) {
        try {
            File f = new File(fpath);
            return gen_item_list(f);
        } catch (Exception e) {
            return new ProductSpecification[] {};
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
    /**
     * returns a string of the unit price formated to
     * look nice
     * */
    public String getUnitPriceFormated() {
        return formatPrice(getUnitPrice());
    }

    /**
     * formats a price to look nice as a string
     * */
    public static String formatPrice(double price) {
        return String.format("%.02f",price);
    }
}

// Homework 2: Sales Register Program Updated
// Course: CIS357
// Due date: Tue Jul 12 11:59pm
// GitHub: https://github.com/Mequam/cis357-hw1-kennamer, tag: hw2-release-v2.0.0
// Instructor: Il-Hyung Cho
// A simple cash register program made to practice ArrayLists
// to run the program, compile it on the command line, then call it from the command line with
// the file path to a csv file in the form id, name, unit price
// if no path is given, the program will prompt for one
/*
    Program features:
    Change the item code type to String: Y
    Provide the input in CSV format. Ask the user to enter the input file name: Y
    Implement exception handling for
        File input: Y
        Checking wrong input data type: Y
        Checking invalid data value: Y
        Tendered amount less than the total amount: Y
    Use ArrayList for the items data: Y
    Adding item data: Y
    Deleting item data: Y
    Modifying item data: Y
*/

package CashProgram;
import CashItems.*;

import java.io.File;
import java.util.Scanner;

/**
 * The main class of the program,
 * represents a cash register that sells individual items
 */
public class HW2_kennamer {
    /**
     * Creates a new main class to run main program code on
     * */
    HW2_kennamer() {

    }
    /**
     * syntactic sugar function that prints strings to the console and is fast to type
     *
     * @param s the string to display to the user
     * @param format the formatting used by the say function
     */
    public static void say(String s,String format) {
        System.out.print(String.format(format,s));
    }

    /**
     * output information to the user with the global formatting
     *
     * @param s the string to display to the user
     */
    public static void say(String s) {
        say(s,PROGRAM_FORMATING);
    }

    /**
     * queries the user for information using the proper formatting
     *
     *
     * @param question the question to display to the user
     * @param formatting the string formating of that question
     * @return the answer, as a string, that the user typed
     */
    public  static String ask(String question,String formatting) {
        say(question,formatting);
        Scanner inScan = new Scanner(System.in);
        return inScan.nextLine();
    }

    /**
     * demands a valid file input from the user
     *
     * @param question the question to display to the user
     * @param angry_question the question to display after a file path which does not exist is passed
     * @param formatting the formating to use when displaying that question
     * @return a file object pointing to an existing file and opened
     * */
    public  static File askFile(String question, String angry_question, String formatting) {
        String fpath = ask(question, formatting);

        File f = new File(fpath);

        while (! f.exists()) {
            f = new File(ask(angry_question,formatting));
        }

        return f;
    }

    /**
     * demands a valid file input from the user, enables optional variables
     *
     * @param question the question to display to the user
     * @param angry_question the question to display after a file path which does not exist is passed
     * @return a file object pointing to an existing file and opened
     * */
    public  static File askFile(String question, String angry_question) {
        return askFile(question,angry_question,PROGRAM_FORMATING);
    }

    /**
     * Constant formating used when displaying text to the user
     * */
    private static final String  PROGRAM_FORMATING = "%-26s";
    /**
     * gets a string response from the user
     * the idea here is that we can put our formating in one place
     * and not have to change it everywhere in the program
     *
     * @param question the question to display to the user
     * @return the string that the user typed
     */
    public static String ask(String question) {
        return ask(question,PROGRAM_FORMATING);
    }

    /**
     * demands a valid double input from the user
     *
     * @param question       the question to ask the user
     * @param angry_question the angry question to ask the user when they give us an invalid double
     * @return a valid double typed by the user
     */
    public  static  double askDouble(String question, String angry_question) {
        String a = ask(question,PROGRAM_FORMATING + "$ ");
        boolean good_responce = false;
        double ret_val = 0;
        while (!good_responce) {
            try {
                ret_val = Double.parseDouble(a);
                good_responce = true;
            }
            catch (Exception e) {
                a = ask(angry_question,PROGRAM_FORMATING + "$ ");
            }
        }
        return ret_val;
    }

    /**
     * demands a valid double input from the user
     *
     * @param question question to ask the user
     * @return valid double typed by the user
     */
    public  static double askDouble(String question) {
        return  askDouble(question,"Enter a valid decimal:");
    }

    /**
     * demands a valid integer input from the user
     *
     * @param question question to display to the user
     * @param angry_question question to ask the user after they type an invalid integer
     * @return a valid integer
     */
    public static int askInt(String question,String angry_question) {
        String a = ask(question);
        boolean good_responce = false;
        int ret_val = 0;
        while (!good_responce) {
            try {
                ret_val = Integer.parseInt(a);
                good_responce = true;
            }
            catch (Exception e) {
                a = ask(angry_question);
            }
        }
        return ret_val;
    }

    /**
     * demands a valid integer input from the user
     *
     * @param question question to display to the user
     * @return integer that the user typed
     */
    public  static int askInt(String question) {
        return askInt(question,"!!Invalid Integer\n\n" + question);
    }

    /**
     * asks the user for a product code
     *
     *
     * @param icc item counter container, used to determine what product codes are valid
     * @return valid product code, or -1 indicating a terminating response
     */
    public  static String askProductCode(ItemCounterContainer icc) {
        String id = ask("Enter Product Code:");
        while (!(icc.contains(id))) {
            id = ask("Enter Valid Product Code:");
        }
        return id;
    }


    /**
     * asks the user for a product code, throws an exception if an invalid product code is given
     * we could probably make this better in the future by making the askItemCode accept a white list of valid
     * values in an array, but this quick and dirty solution works atm
     *
     * @param pc item container to test against
     * @return valid product code, or -1 indicating a terminating response
     */
    public  static String productCodeCommand(ProductCatalog pc) {
        String id = ask("Enter Product Code:");
        while (!pc.contains(id) && !(id.equals( "-1"))) { //we run until we get a command input, or a valid id
            if (ProductSpecification.ItemCode.isControlCode(id)) {
                /**
                 * the control code indicates that we allways want to print to the screen
                 * */
                System.out.println(pc.type_display_string());
            }
            else if (!ProductSpecification.ItemCode.validItemCode(id)) {
                say("!!Invalid Data Type", PROGRAM_FORMATING + "\n\n");
            } else {
                say("!!Invalid Item Code",PROGRAM_FORMATING + "\n\n");
            }

            id = ask("Enter Product Code:");
        }
        return id;
    }


    /**
     * displays a separator on standard out
     */
    public  static void printSeperator() {
        System.out.println(String.format("%-32s","").replace(' ','-'));
    }

    /**
     * the main entry point of the program,
     * reads in a series of sales items from the disk
     * queries the user to see what purchases they are making
     * computes change and
     * records total sales for the day
     *
     * @param args command line arguments inputted to the program
     */
    public static void main(String[] args) {
        //initialize a new ItemCounterContainer that will hold the items the cash register accepts
        ProductCatalog productcat;
        if (args.length > 0) {
            productcat = new ProductCatalog(args[0]);
        }
        else {
            productcat = new ProductCatalog(askFile("Item File: ","Item File: "));
        }

        CashRegister cashreg = new CashRegister(productcat);

        System.out.println("Welcome to Kennamer cash register system!\n");

        Scanner inScan = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.print(String.format("\n%-26s","Begin a new sale? (Y/N)"));
            String userInput = inScan.nextLine();

            printSeperator();

            if (userInput.toUpperCase().equals("Y")) {
                //demand a valid product code

                cashreg.makeNewSale();

                String productCode = productCodeCommand(productcat);

                while (!productCode.equals("-1")) {
                    System.out.print(String.format("%8s %-17s", "", "item name:"));

                    //this is well formated because we made it to this sle
                    ProductSpecification.ItemCode ic = new ProductSpecification.ItemCode(productCode);

                    say(productcat.get_type_name(ic), "%s\n");

                    int amount = askInt("Enter Quantity: ");

                    //actually add the item to the register
                    cashreg.enterItemVerbose(ic,amount);

                    //demand a valid product code
                    productCode = productCodeCommand(productcat);
                }



                printSeperator();

                cashreg.endSale();


                //we also want a new line before each run of the program
                printSeperator();

            } else {//leave the loop

                run = false;
            }
        }

        System.out.println("\nThe total sale for the day is $" + String.format("%.2f\n",cashreg.dailyTotal));

        productcat.askUpdateData();

        printSeperator();

        System.out.println( "\n" + productcat.type_display_string());

        productcat.save();

        System.out.println("Thank you for using POS system. Goodbye!");

    }
}
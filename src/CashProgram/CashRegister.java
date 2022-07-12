// Homework 1: Sales Register Program
// Course: CIS357
// Due date: Tue Jul 5th 11:59pm
// GitHub: https://github.com/Mequam/cis357-hw1-kennamer
// Instructor: Il-Hyung Cho
// A simple cash register program made to practice the basics of java
// to run the program, compile it on the command line, then call it from the command line with
// the file path to a csv file in the form id, name, unit price

package CashProgram;
import CashItems.*;

import java.io.File;
import java.util.Locale;
import java.util.Scanner;

/**
 * The main class of the program,
 * represents a cash register that sells individual items
 */
public class CashRegister {
    /**
     * Creates a new cash register class to run main program code on
     * */
    CashRegister() {

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
     *
     *
     * @param icc item counter container, used to determine what product codes are valid
     * @return valid product code, or -1 indicating a terminating response
     */
    public  static String productCodeCommand(ItemCounterContainer icc) {
        String id = ask("Enter Product Code:");
        boolean run = !icc.contains(id);
        while (run && !(id.equals( "-1") || id.equals( "0000"))) { //we run until we get a command input, or a valid id

            //this try catch block works and is required for the assignment,
            //but checking this would be SO much better if we just used booleans as
            //return flags, that's basically what this is doing but in a round-about
            //inefficient and clunky way with except. Still it is in the assignment requirements so we
            //include it here

            try {
                icc.exceptionCheckItemCode(id);
                run = false;
                break;
            } catch (Item.ItemCode.MalformedItemCodeException e) {
                say("!!Invalid Data Type",PROGRAM_FORMATING + "\n\n");
            } catch (ItemCounterContainer.ItemCodeNotInContainerException e) {
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
        ItemCounterContainer icc;
        if (args.length > 0) {
            icc = new ItemCounterContainer(args[0]);
        }
        else {
            icc = new ItemCounterContainer(askFile("Item File: ","Item File: "));

        }
        System.out.println("Welcome to Kennamer cash register system!\n");

        Scanner inScan = new Scanner(System.in);
        boolean run = true;
        double dailyTotal = 0.0;
        while (run) {
            //ensure that we start with no data in the container
            icc.zeroData();
            System.out.print(String.format("\n%-26s","Begin a new sale? (Y/N)"));
            String userInput = inScan.nextLine();

            printSeperator();

            if (userInput.toUpperCase().equals("Y")) {
                //demand a valid product code
                String productCode = productCodeCommand(icc);

                while (!productCode.equals("-1")) {
                    if (productCode.equals("0000")) {
                        System.out.println("\n" + icc.type_display_string());
                    }
                    else {
                        //print out to the screen in a nice formated way
                        System.out.print(String.format("%8s %-17s", "", "item name:"));
                        say(icc.get_type_name(productCode), "%s\n");
                        int amount = askInt("Enter Quantity: ");

                        //actually store the increase in amount
                        icc.get_counter(productCode).count += amount;

                        double itemTotal = icc.get_type_cost(productCode) * amount;
                        System.out.println(String.format("%7s %-17s $ %.02f\n", "", "item total:", itemTotal));

                    }
                    //demand a valid product code
                    productCode = productCodeCommand(icc);
                }



                printSeperator();
                System.out.println(icc.toString());

                dailyTotal += icc.totalCost();

                double total = icc.taxedCost();

                double payment = askDouble("Tendered amount:");

                total -= payment;

                while (total > 0) {
                    System.out.println(String.format("%-26s$%.02f","Remaining charge:",total));
                    payment = askDouble("Further payment required:");
                    total -= payment;
                }

                System.out.println(String.format("%-26s$ %.2f","Change",Math.abs(total)));

                //we also want a new line before each run of the program
                printSeperator();

            } else {//leave the loop

                run = false;
            }
        }

        System.out.println("\nThe total sale for the day is $" + String.format("%.2f\n",dailyTotal));

        System.out.print("Do you want to update the item data? (A/D/M/Q): ");
        String response = inScan.nextLine();


        switch (response.toUpperCase()) {
            case "A":
                //create a new item using user inputs
                Item i = new Item();
                icc.add(i); // add it
                break;
            case "D":
                System.out.println("they want to delete an item");
                break;
            case "M":
                break;
            case "Q":
                break;

        }

        printSeperator();
        System.out.println( "\n" + icc.type_display_string());

        System.out.println("Thank you for using POS system. Goodbye!");
    }
}

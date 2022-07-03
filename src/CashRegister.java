import CashItems.*;

import java.util.Locale;
import java.util.Scanner;

public class CashRegister {

    public static void say(String s,String format) {
            System.out.print(String.format(format,s));
    }
    public static void say(String s) {
        say(s,"%-26s");
    }

    /**queries the user for information using the proper formating*/
    public  static String ask(String question,String formatting) {
        say(question,formatting);
        Scanner inScan = new Scanner(System.in);
        return inScan.nextLine();
    }
    public static String ask(String question) {
        return ask(question,"%-26s");
    }
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

    public  static int askInt(String question) {
        return askInt(question,"Enter a valid integer:");
    }
    /**asks the user for a product code*/
    public  static int askProductCode(ItemCounterContainer icc) {
        int id = askInt("Enter Product Code:");
        while (!icc.contains(id)) {
            id = askInt("Enter Valid Product Code:");
        }
        return id;
    }
    public static void main(String[] args) {
        System.out.println(args[0]);
        System.out.println("Welcome to Kennamer cash register system!\n");

        ItemCounterContainer icc = new ItemCounterContainer(args[0]);
        Scanner inScan = new Scanner(System.in);
        boolean run = true;
        while (run) {
            System.out.print("Begin a new sale? (Y/N)");
            String userInput = inScan.nextLine();
            if (userInput.toUpperCase().equals("Y")) {
                //demand a valid product code
                int productCode = askProductCode(icc);

                //print out to the screen in a nice formated way
                say("item name: ","%26s");
                say(icc.get_type_name(productCode),"%s\n");
                int amount = askInt("Enter Quantity: ");

                //actually store the increase in amount
                icc.get_counter(productCode).count += amount;

                double itemTotal = icc.get_type_cost(productCode)*amount;
                say("item total: ","%26s");
                say(String.format("$%.02f",itemTotal),"%s\n");
            }
        }
    }
}

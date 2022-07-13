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
       the daily total for this CashRegister
     */
    double dailyTotal = 0.0;

    /** actually charges the cost of the items to the user
     * @param icc the items that we are charging
     * */
    public void chargeCost(ItemCounterContainer icc) {
        System.out.println(icc.toString());

        dailyTotal += icc.totalCost();

        double total = icc.taxedCost();

        double payment = HW2_kennamer.askDouble("Tendered amount:");

        total -= payment;

        while (total > 0) {
            System.out.println(String.format("%-26s$ %.02f","Remaining charge:",total));
            payment = HW2_kennamer.askDouble("Further payment required:");
            total -= payment;
        }

        System.out.println(String.format("%-26s$ %.2f","Change",Math.abs(total)));

    }

}

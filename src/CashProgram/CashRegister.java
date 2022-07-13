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

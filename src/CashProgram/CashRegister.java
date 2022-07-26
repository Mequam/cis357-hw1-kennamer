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
    /**reference to the catalog that this register will look into*/
    private ProductCatalog catalog_reference;

    /**
     * generate a new register with a reference to a catalog
     * */
    CashRegister(ProductCatalog pc) {
        catalog_reference = pc;
    }
    /**
       the daily total for this CashRegister
     */
    double dailyTotal = 0.0;

    Sale current_sale = null;
    /**
     * creates a new sale and adds it to the cash register
     * */
    void makeNewSale() {
        if (current_sale != null) {
            endSale();
        }
        current_sale = new Sale();
    }

    /**
     * syntactic sugar function that passes the amount to pay to the current_sale
     * */
    void makePayment(double amount) {
        current_sale.makePayment(amount);
    }
    /**
     *
     * prompts the user for the given contained item
     */
    void promptContainedItem() {
    }
    /**
     *
     * puts a new item into the current sale
     *
     * */
    void enterItem(Item.ItemCode id, int quantity) {
        if (current_sale != null) {
            current_sale.makeLineItem(new Item(id),quantity);
        }
    }
    /**
     * enter an item and tell the user what we are doing
     * */
    void enterItemVerbose(Item.ItemCode id,int quantity) {
        if (current_sale != null && catalog_reference.contains(id)) {
            current_sale.makeAndAddLineItemVerbose(
                    catalog_reference.getSpecification(id),
                    quantity);
        }
    }
    /**
     * ends the current sale
     * */
    void endSale() {
        //we do NOTHING if the sale is null
        if (current_sale != null) {

            System.out.println(current_sale);
            //since the dailyTotal is OUR profits,
            //we use the sale function which does NOT include tax
            //becomeComplete does nothing if the current sale is already complete
            current_sale.becomeComplete();
            dailyTotal += current_sale.totalCost(); //make sure to save the cost of the current sale
        }
    }
}

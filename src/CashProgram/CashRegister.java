package CashProgram;
import CashItems.*;

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

    /**
     * represents the current sale of the cash register
     * this variable gets re-set whenever we make a new sale
     * think of it like a storage place to put our sales
     * while we wait for them to finish
     * */
    Sale current_sale = null;

    /**
     * creates a new sale and adds it to the cash register
     * */
    void makeNewSale() {
        if (current_sale != null && !current_sale.isComplete) {
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
     * puts a new item into the current sale
     *
     * */
    void enterItem(ProductSpecification.ItemCode id, int quantity) {
        if (current_sale != null) {
            current_sale.makeLineItem(new ProductSpecification(id),quantity);
        }
    }
    /**
     * enter an item and tell the user what we are doing
     * */
    void enterItemVerbose(ProductSpecification.ItemCode id, int quantity) {
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
            System.out.printf("%-26s$ %.02f\n","Change:",
                                Math.abs(current_sale.taxedCost() - current_sale.amount)
                            );
            dailyTotal += current_sale.totalCost(); //make sure to save the cost of the current sale
        }
    }
}

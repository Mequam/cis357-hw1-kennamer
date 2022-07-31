package CashProgram.CashProgram;

import CashProgram.CashItems.ProductSpecification;
import CashProgram.CashItems.ItemCounter;
import CashProgram.CashItems.ItemCounterContainer;
import CashProgram.askUtils.AskUtils;

/**
 * represents a single sale, contains functions to demand sale
 * completion, store and add items
 *
 *
 * */
public class Sale extends ItemCounterContainer {

    /**
     * represents the total amount that the user has payed for this
     * sale
     * */
    double amount;
    /**
     * flag that indicates if the given sale is complete
     * true if amount surpases totalCost
     *
     * */
    boolean isComplete;
    /**
     * default constructor
     */
    public Sale() {
        super(0);
    }
    /**
     * syntactic sugar function to fulfill assignment specifications
     * */
    double getTotal () {
        return taxedCost();
    }
    /**
     * syntactic sugar rename of the item counter class for the
     * Sale class, this is here so that the class name matches
     * the assignment specification for SalesLineItem,
     * <p>
     * importantly ItemCounter matches ALL requirements of SalesLineItem,
     * it just needed to be renamed to make the code match
     */
    class SalesLineItem extends ItemCounter {
        public SalesLineItem(ProductSpecification e) {
            super(e);
        }
        public SalesLineItem(ProductSpecification e, int count) {
            super(e,count);
        }
        /**
         * syntactic sugar function that reuturns the total cost of this sale
         * this is here in case someone else was expecting this function name
         * and needs it to be in the class, for all intetns and purposes
         * the dynamicCost function should be used when computing cost for this item
         * */
        double getSubtotal() {
            return dynamicCost();
        }
    }

    /**
     * returns a new line item from the users specification
     */
    SalesLineItem makeLineItem(ProductSpecification i, int count) {
        return new SalesLineItem(i,count);
    }


    /**
     * adds a line item to the container and displays the cost of that
     * line item, formated out to the user for convinence
     *
     * */
    void makeAndAddLineItemVerbose(ProductSpecification id, int count) {

        SalesLineItem si = makeLineItem(id,count);
        System.out.println(String.format("%7s %-17s $ %.02f\n", "", "item total:", si.cost()));
        add(si);
    }

    /**
     * creates a new line item and adds it to the container
     * */
    void makeAndAddLineItem(ProductSpecification id, int count) {
        add(makeLineItem(id,count));
    }
    /**
     * get the user to make a SINGLE payment, may or may not be valid
     */
    void makePayment(double amount) {
        addAmount(amount);
    }
    /**
     * goes straight to the user and asks them to supply payment
     * for THIS sale, changing the prompt to match the sale state
     * */
    double promptPayment() {

        String msg = "Payment: ";
        if (amount > 0) {
            AskUtils.say("Remaining Charge: ","%-26s"+String.format("$ %.02f",getChange()) + "\n");
            msg = "Further Payment Required: ";
        }
        return HW2_kennamer.askDouble(msg);
    }

    /**
     *
     * returns the change required for this sale
     *
     * */
    double getChange() {
        return Math.abs(taxedCost() - amount);
    }
    /**
     * demand payment until we are fully paid
     * */
    void becomeComplete() {
        while (!isComplete) {
            makePayment(promptPayment());
        }
    }

    /**
     * increase the amount payed by toAdd, runs additional hooks
     * required when incrimenting amount
     *
     * */
    void addAmount(double toAdd) {
        amount += toAdd;
        if (amount >= taxedCost()) {
            isComplete = true;
        }
    }
}
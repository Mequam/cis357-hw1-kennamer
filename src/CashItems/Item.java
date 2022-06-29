package CashItems;
import Monoids.*;

import java.lang.reflect.Array;
/** This class represents a TYPE of item, it is inteanded to be used as a programatic type, much
 * like pythonic tuples. As such it is an imutable type, and cannot be changed after creation*/
public class Item {
    /** generates a new item variable */
    public Item(int code,String name,double unitPrice) {
        this.itemCode = code;
        this.itemName = name;
        this.unitPrice = unitPrice;
    }
    /** generates and returns a new array of item types from the given file */
    static Item [] gen_item_list(String file) {

        /*
            for the time bieng this is a debug list until this feature is ironed out
        * */
        return new Item [] {
                new Item(1,"bottled water",1.50),
                new Item(1,"bottled water",1.50) ,
                new Item(2,"candy",1.00),
                new Item(3,"chocolate",2.50),
                new Item(4,"gum",1.00)
            };
    }

    /** The integer code for a given item */
    private int itemCode = 0;
    public int getItemCode() {
        return itemCode;
    }
    /** the name of this item */
    private String itemName;
    public String getItemName() {
        return itemName;
    }
    /** the price of a single unit of this item*/
    private double unitPrice;
    public double getUnitPrice() {
        return unitPrice;
    }
}

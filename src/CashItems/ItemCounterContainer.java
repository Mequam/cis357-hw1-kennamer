package CashItems;

import Monoids.CounterContainer;

public class ItemCounterContainer extends Monoids.CounterContainer {
    /*
     * java appears to have very ugly higher order functions
     * which makes since as its an oop language
     * */

    public ItemCounterContainer(int size) {
        super(size);
    }
    public ItemCounterContainer(String file) {
        Item [] item_list = Item.gen_item_list(file);
        data = new ItemCounter[item_list.length];

        //encapsulate each of the items that we returned in a counter container
        for (int i = 0 ; i < item_list.length; i++) {
            data[i] = new ItemCounter(item_list[i]);
        }
    }
    /** Gets the amount of the given item type*/
    public  int get_type_amount(int id) {
        return (int)testMap(
                (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCode() == id;},
                (j)->{ return (((ItemCounter)j).count);} );
    }
    /** Gets the name of the given item type*/
    public String get_type_name(int id) {
        return (String) testMap(
                (i) -> {return ((Item)((ItemCounter)(i)).element).getItemCode() == id;},
                (j)->{ return ((Item)((ItemCounter)(j)).element).getItemName();}
        );
    }
    /** gets the cost of the give type*/
    public  double get_type_cost(int id) {
        return (double)testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return ((Item) ((ItemCounter) (k)).element).getUnitPrice();
                }
        );
    }
    public  ItemCounter get_counter(int id){
        return (ItemCounter) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return k;
                }
        );
    }
    public Item get_item(int id) {
        return (Item) testMap(
                (i) -> {
                    return ((Item) ((ItemCounter) (i)).element).getItemCode() == id;
                },
                (k) -> {
                    return ((ItemCounter)k).element;
                }
        );
    }
}

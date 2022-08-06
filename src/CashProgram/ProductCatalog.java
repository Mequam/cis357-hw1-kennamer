package CashProgram.CashProgram;
import CashProgram.CashItems.ProductSpecification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * this class represents the loaded product catalog that we read from the disc
 * */
public class ProductCatalog {

    public boolean doSort = true;
    /** adds a new Item Counter to the container
     * @param i an item counter to add*/
    public void add(ProductSpecification i) {
        if (!contains(i.getItemCode())) {
            productSpecification_list.add(i);
            if (doSort) {
                sortData();
            }
        }
    }

    /**
     * initilizes a sort of the data contained inside of our data variable
     * */
    public void sortData() {
        Collections.sort(productSpecification_list,(a, b) -> {return a.getItemName().toLowerCase().compareTo(b.getItemName().toLowerCase());});
    }

    /**
     * removes the counter with the given item code
     *
     * @param itemCode the item code of the counter that we want to remove
     * */
    public  void remove(ProductSpecification.ItemCode itemCode) {
        remove(getSpecification(itemCode));
    }
    /**
     * removes the given item from the list
     *
     * */
    public void remove(ProductSpecification i) {
        productSpecification_list.remove(i);
    }

    /**
     * assumes the full product specification is given
     * and updates the existing items to match it
     * */
    public void modify(ProductSpecification ps) {
        if (contains(ps.getItemCode())) {
            remove(ps.getItemCode());
            add(ps);
            sortData();
        }
    }
    /**
     * updates the existing prodcuct specification of the given target
     * with user givein CLI input
     * */
    public void modify(ProductSpecification.ItemCode target) {
        modify(new ProductSpecification(target));
    }
    /**
     * prompts the user to update the data contained within the item container
     *
     * */
    void askUpdateData() {
        Scanner inScan = new Scanner(System.in);

        System.out.print("Do you want to update the item data? (A/D/M/Q): ");
        String response = inScan.nextLine();

        while (!response.toUpperCase().equals( "Q")) {

            switch (response.toUpperCase()) {
                case "A" -> {
                    //create a new item using user inputs
                    ProductSpecification i = new ProductSpecification();
                    add(i); // add it

                    System.out.println("Item addition successful!\n");
                }
                case "D" -> {
                    remove(askContainedItemCode());

                    System.out.println("Item delete successful!\n");
                }
                case "M" -> {
                    ProductSpecification.ItemCode target = askContainedItemCode(); // get an item code from the collection
                    modify(target);
                    System.out.println("Item modification successful!\n");
                }
            }

            System.out.print("Do you want to update the item data? (A/D/M/Q): ");
            response = inScan.nextLine();
        }
    }

    /**
     * gets a string representing the items contained in this container and inteanded
     * to be seen directly by the user
     *
     * */
    public String type_display_string() {
        String ret_val = String.format(ProductSpecification.DISPLAY_STRING_FORMAT + "\n","Item Code","Item Name","Unit Price");
        for (int i = 0; i < productSpecification_list.size(); i++) {
            ProductSpecification saleProductSpecification = productSpecification_list.get(i);
            ret_val += saleProductSpecification.display_string() + "\n";
        }
        return ret_val;
    }

    public ArrayList<ProductSpecification> productSpecification_list;
    public void forEach(Consumer<ProductSpecification> action) {
        productSpecification_list.forEach(action);
    }

    public ArrayList<ProductSpecification> getListCopy() {
        ArrayList<ProductSpecification> ret_val = new ArrayList<ProductSpecification>();

        //copy over each value of the list
        forEach((p)-> {
            ret_val.add(p);
        });
        return ret_val;
    }

    public ArrayList<ProductSpecification.ItemCode> getItemCodeList() {
        ArrayList<ProductSpecification.ItemCode> ret_val = new ArrayList<>();
        productSpecification_list.forEach((i)->{
            ret_val.add(i.getItemCode());
        });
        return ret_val;
    }
    java.io.File dataFile;
    public ProductCatalog(File f) {
        dataFile = f;
        productSpecification_list = ProductSpecification.gen_item_linked_list(f);
        if (doSort) {
            sortData();
        }
    }

    /**
     * runs the ProducCatalog(File f ) function with the file pointed
     * to by
     *
     * @param f path to a csv file where the products are stored
     * */
    public ProductCatalog(String f) {
        this(new File(f));
    }

    /**
     * generates a new ProductCatalog given a random access file
     * to read and write from
     *
     * */
    public ProductCatalog(RandomAccessFile raf) throws IOException{
        productSpecification_list = ProductSpecification.gen_item_linked_list(raf);
        if (this.doSort) {
            sortData();
        }
    }

    /**
    * returns the specification of the item with the given item id
     *
     * @param id ItemCode that must be within the container
     * @return a ProductSpecification from within the container
    * */
    public ProductSpecification getSpecification(ProductSpecification.ItemCode id) {
        for (ProductSpecification i : productSpecification_list) {
            if (i.getItemCode().equals(id)) {
                return i;
            }
        }
        //with any luck we should never get here
        return null;
    }

    public ProductSpecification getSpecification(String s) {
        return getSpecification(new ProductSpecification.ItemCode(s));
    }

    /**
     * returns true if we contain the given item code
     * */
    public boolean contains(ProductSpecification.ItemCode id) {
        return getSpecification(id) != null;
    }

    /**
     * Gets the name of the given item type  @param id the id
     *
     * @param id the id of a type stored inside of the container
     * @return the type name, null if no type matching id exists
     */
    public String get_type_name(ProductSpecification.ItemCode id) {
        return getSpecification(id).getItemName();
    }


    /**
     * return true if we contain the given product code
     * */
    public boolean contains(String itemCode) {
        //short circiting will make us return false if we get an invalid item code, WITHOUT iterating over the loop
        return ProductSpecification.ItemCode.validItemCode(itemCode)
                && contains(new ProductSpecification.ItemCode(itemCode));
    }

    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code bieng any item code stored within the container
     *
     * @return the item code that the user typed
     * */

    public ProductSpecification.ItemCode askContainedItemCode() {
        return askContainedItemCode("Enter Item Code:", ProductSpecification.DEFAULT_FORMATING);
    }

    /**
     * saves this container out to the file that was used to generate it
     * */
    public void save() {
        save(dataFile);
    }


    /**
     * only inherited classes can have no arguments when they create us
     * */
    protected ProductCatalog() {

    }
    /**
     * saves this class out to the given random access file
     * */
    public void save(RandomAccessFile f) throws IOException {
        f.setLength(0);
        f.seek(0);
        for (ProductSpecification ps : productSpecification_list) {
            f.write(ps.encode());
        }
    }

    /**
     * saves this container out to the given file as a .csv file
     * */
    public void save(java.io.File f) {

        FileWriter fw;
        try {
            fw = new FileWriter(f);

            for (int i = 0; i < productSpecification_list.size() ; i ++) {
                fw.write(productSpecification_list.get(i).to_csv_line() + "\n");
            }

            fw.close();
        }
        catch (Exception e) {

        }
    }
    /**
     * demands a valid item code from the user running the program
     *
     * a valid item code being any item code stored within the container
     *
     * @param question display question to ask the user
     * @param formating the format string for that question
     * @return the item code that the user typed
     * */
    public ProductSpecification.ItemCode askContainedItemCode(String question, String formating) {
        ProductSpecification.ItemCode code = ProductSpecification.ItemCode.askItemCode(question,formating);

        while (!(contains(code))) {
            if (code.isControlCode()) {
                System.out.println(type_display_string());
            }
            else {
                System.out.printf(formating,"!!Item Code Does Not Exist\n");
            }
            code = ProductSpecification.ItemCode.askItemCode(question,formating);
        }

        return code;
    }

}

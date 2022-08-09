package CashProgram.CashProgram;

import CashProgram.CashItems.ProductSpecification;
import CashProgram.NetUtils.ProductSpecProtocal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class represents a product catalog who retrives data
 * from over a network to a ProductSpec Server
 * */
public class ProductCatalogClient extends ProductCatalog {

    /**
     *
     * server socket representing the location of the item server
     *
     * */
    Socket server;

    /**
     * creates a new ProductCatalogClient from a given ip address string and port
     * */
    public ProductCatalogClient(String ip,int port) {
        try {
            server = new Socket(ip, port);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * returns a list containing all of the items stored inside of the database
     *
     *
     * this is a WIP function, it works, but it is FAAAAAR from efficient in
     * terms of network performance, we use double the bandwidth that we have to with
     * this approach as we ping the network back and forth
     *
     * in the future it would make much more sense to simply send over ALL
     * item specifications, but this works atm
     * */
    @Override
    public ArrayList<ProductSpecification> get_specification_list() {
        ArrayList<ProductSpecification> ret_val = new ArrayList<>();

        ArrayList<ProductSpecification.ItemCode> ic = new ArrayList<>();
        try {
            ic = get_valid_item_code();
        }
        catch (Exception e) {
            System.out.println(e);
        }

        for (ProductSpecification.ItemCode i : ic) {
            ret_val.add(getSpecification(i));
        }

        return ret_val;
    }

   /**
    * generates a new list of all item codes contained inside of the database
    * */
   @Override
    public ArrayList<ProductSpecification.ItemCode> get_valid_item_code() throws IOException {
        ArrayList<ProductSpecification.ItemCode> ret_val = new ArrayList<ProductSpecification.ItemCode>();

        OutputStream os = server.getOutputStream();
        InputStream is = server.getInputStream();
        os.write(ProductSpecProtocal.COMMANDS.GET_ALL_ITEM_CODES.ordinal());

        ProductSpecification.ItemCode ic;

        ic = new ProductSpecification.ItemCode(is.readNBytes(ProductSpecification.ItemCode.encoding_size));
        while (!ic.isControlCode()) {
            ret_val.add(ic);
            ic = new ProductSpecification.ItemCode(is.readNBytes(ProductSpecification.ItemCode.encoding_size));
        }

        return ret_val;
    }
    /**
     * grabs a specification with the given item code from the server
     * */
    @Override
    public ProductSpecification getSpecification(ProductSpecification.ItemCode ic) {
        System.out.println("running OUR specification");
        try {
            OutputStream os = server.getOutputStream();
            os.write(
                    ProductSpecProtocal.gen_get_request(ic)
            );
            InputStream is = server.getInputStream();
            if (is.read() != ProductSpecProtocal.COMMANDS.SPEC_NOT_FOUND.ordinal()) {
                return new ProductSpecification(
                        ProductSpecProtocal.read_specification_bytes(is)
                );
            }
            System.out.println("ITEM CODE NOT FOUND");
            return null;
        }
        catch (Exception e) {
            System.out.println("EXCEPTION OCCURED");
            System.out.println(e);
        }
        System.out.println("ITEM CODE NOT FOUND");
        return null;
    }
}

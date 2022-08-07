package CashProgram.CashProgram;

import CashProgram.CashItems.ProductSpecification;
import CashProgram.NetUtils.ProductSpecProtocal;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * this class represents a product catalog who retrives data
 * from over a network to a ProductSpec Server
 * */
public class ProductCatalogClient extends ProductCatalog {
    Socket server;
    public ProductCatalogClient(String ip,int port) {
        try {
            server = new Socket(ip, port);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<ProductSpecification> get_specification_list() {
        ArrayList<ProductSpecification> ret_val = new ArrayList<ProductSpecification>();
        ret_val.add(getSpecification("A001"));
        return ret_val;
    }

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

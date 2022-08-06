package CashProgram.CashProgram;
import CashProgram.CashItems.ProductSpecification;

import javax.xml.transform.Result;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * this class contains all of the behavior and functionality of the
 * product catalog, but it is overloaded to have its data source
 * point to the sql database
 * */
public class ProductCatalogSQL extends ProductCatalog {
    Connection sqlCon;

    /**
     * determines wether or not we contain the given item
     * */
    @Override
    public boolean contains(ProductSpecification.ItemCode ic) {
        boolean ret_val = false;
        Statement s;
        ResultSet rs;
        try {
            if (!sqlCon.isClosed()) {
                s = sqlCon.createStatement();
                rs = s.executeQuery("SELECT * FROM item WHERE item_code='" + ic.getValue() + "'");
                ret_val = rs.next();
                rs.close();
                s.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return ret_val;
    }


    @Override
    public ProductSpecification getSpecification(ProductSpecification.ItemCode id) {
        System.out.println(id.getValue());
        System.out.println("SELECT * FROM item WHERE item_id='"+id.getValue() + "'");

        if (contains(id)) {
            try {
                Statement s = sqlCon.createStatement();
                //the ItemCode class does a LOT to sanatize the value inside of it, we can
                //mostly trust the incoming string value fed right into sql
                //try to break it I dare you :p

                System.out.println(id.getValue());
                System.out.println("SELECT * FROM item WHERE item_id='"+id.getValue() + "'");
                ResultSet rs = s.executeQuery("SELECT * FROM item WHERE item_code='"+id.getValue() + "'");

                rs.next();
                //since we contain this item code, there WILL be values there
                return new ProductSpecification(
                        rs.getString("item_code"),
                        rs.getString("item_name"),
                        rs.getDouble("unit_price")
                );


            } catch (Exception e) {
                System.out.println(e);
                return null;
            }

        }
        return null;
    }

    /**
     * generate a new ProductCatalogSQL from the given sqllite db
     * */
    public ProductCatalogSQL(Connection c) {
        super();
        this.sqlCon = c;
        /*try {
            /*
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection("jdbc:sqlite:/home/j0hn/ProgramingWorkshop/java/IdeaProjects/ProductSpecServer/sql/ProductCat.db");
              */
        /*
                c.setAutoCommit(false);


                Statement statement = c.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM item WHERE");


                while (rs.next()) {
                    String itemCode = rs.getString("item_code");
                    System.out.println(itemCode);
                }

                rs.close();
        }
        catch (Exception e) {

        }
    }
    */
    }
}

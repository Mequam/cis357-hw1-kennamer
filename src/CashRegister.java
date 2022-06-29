import CashItems.*;

public class CashRegister {
    public static void main(String[] args) {
       ItemCounterContainer icc = new ItemCounterContainer("blah");

        System.out.println(icc.get_type_name(2));
        System.out.println(icc.get_type_amount(2));
        System.out.println(icc.get_type_cost(2));


        System.out.println(icc.get_item(2).getItemName());
        System.out.println(icc.get_item(2).getItemCode());
        System.out.println(icc.get_item(2).getUnitPrice());

        System.out.println("Hello World");
    }
}

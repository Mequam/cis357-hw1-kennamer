package Monoids;


import CashItems.Item;
import CashItems.ItemCounter;

import java.util.function.Predicate;

/** this class represents a generic Counter container and contains some convinece functiosn to interact with them*/
public class CounterContainer {
    public  Counter[] data;

    public  CounterContainer() {

    }
    public  CounterContainer(int length) {
        data = new Counter[length];
    }
    /*
        this pattern shows up enough that Im not typing it again
    * and I wanted to play with interfaces lambdas and everyhting that makes functional
    * programmers happy :)
    * */

    public Object testMap(Predicate<Object> testCriteria, ObjectMapInterface translator) {
        for (Object o : data) {
            if (testCriteria.test(o)) {
                return translator.map(o);
            }
        }
        return null;
    }
}

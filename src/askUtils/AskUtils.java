package CashProgram.askUtils;

import java.util.Scanner;

public  class AskUtils {
    public static void say(String s,String format) {
        System.out.print(String.format(format,s));
    }

    public  static double askDouble(String question, String angry_question,String formating,String angry_formating) {
        Scanner inscan = new Scanner(System.in);

        System.out.print(String.format(formating,question));

        String a = inscan.nextLine();

        boolean good_responce = false;

        double ret_val = 0;
        while (!good_responce) {
            try {
                ret_val = Double.parseDouble(a);
                good_responce = true;
            }
            catch (Exception e) {
                System.out.println(String.format(angry_formating,angry_question));
                a = inscan.nextLine();
            }
        }
        return ret_val;

    }
}

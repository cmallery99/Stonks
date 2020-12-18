import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Company> companies = new ArrayList<>();

        int l = in.nextInt();
        int l1 = in.nextInt();
        String pn = in.next();

        companies = Stock.generateCompanies(l);

        System.out.println();

        if (pn.equals("p")) {
            Stock.stockDayPrint(companies,l1);
        }
        else if (pn.equals("n")) {
            Stock.stockDayNoPrint(companies,l1);
        }


    }
}

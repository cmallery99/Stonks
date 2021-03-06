import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Stock {
    HashMap<String,Company> companyMap = new HashMap<>();

    public static String tickerGen() {
        Random rand = new Random();

        int a;
        char c;
        String ticker = "";

        for (int i = 0; i < 3; i++) {
            a = rand.nextInt(25) + 65;
            c = (char) a;
            ticker = ticker + c;
        }
        return ticker;
    }

    public static double initialPrice() {
        Random rand = new Random();

        double a = rand.nextInt(9999);

        return a/100;

    }

    public static int initialShares() {
        Random rand = new Random();
        int a = rand.nextInt(2);
        int[] shares = new int[]{10000,100000,1000000};
        return shares[a];
    }

    public static double initialDividend() {
        Random rand = new Random();
        int a = rand.nextInt(500);
        return a/100.0;
    }

    public static double priceChange(double a) {
        Random rand = new Random();

        double change = rand.nextInt(100) - 48;

        change = ((change/100.0) + 100) / 100.0;

        double b = (a*change) * 100;

        double l = Math.round(b);

        return l/100.0;

    }
    public static ArrayList<Company> generateCompanies(int l) {
        ArrayList<Company> companies = new ArrayList<>();

        for (int i = 0; i < l; i++) {
            String companyName = Stock.tickerGen();
            double startingPrice = Stock.initialPrice();
            int totalShares = Stock.initialShares();
            double initialDividend = Stock.initialDividend();
            Company c = new Company(companyName,startingPrice,totalShares,initialDividend);
            companies.add(c);
        }

        for (int i = 0; i < l; i++) {
            System.out.println(companies.get(i).getName() + ": " + companies.get(i).getStockPrice() + " " + companies.get(i).getDividendPercent() + "%");
        }

        return companies;
    }

    public HashMap<String,Company> getCompanyMap(ArrayList<Company> companies) {
        for (Company company : companies) {
            companyMap.put(company.getName(), company);
        }
        return companyMap;
    }

    public static void stockDayNoPrint(ArrayList<Company> companies, int days) {
        double currentPrice;
        double newPrice;
        double change;
        double changePercent;

        for (int i = 0; i < (days -1); i++) {
            for (Company company : companies) {
                currentPrice = company.getStockPrice();
                newPrice = Stock.priceChange(currentPrice);
                company.priceChange(newPrice);
            }
        }
        for (Company company : companies) {
            currentPrice = company.getStockPrice();
            newPrice = Stock.priceChange(currentPrice);
            change = Math.round((newPrice - currentPrice) * 100.0) / 100.0;
            changePercent = Math.round((newPrice / currentPrice - 1) * 100 * 100) / 100.0;

            company.priceChange(newPrice);

            System.out.println(company.getName() + ": " + company.getStockPrice() + " " + change + " " + changePercent + "%");
        }
    }

    public static double cashRound(double amount) {
        return Math.round(amount * 100.0)/100.0;
    }

}

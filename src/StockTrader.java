import java.util.ArrayList;
import java.util.HashMap;

public class StockTrader {
    ArrayList<Company> companies;
    Player player;
    Stock stock = new Stock();
    HashMap<String, Company> companyMap;

    public StockTrader(ArrayList<Company> companies, Player player) {
        this.companies = companies;
        this.player = player;
        companyMap = stock.getCompanyMap(companies);
    }

    public void buyOrder(String name, int shares) throws YouABrokeAssHoeException {
        if (companyMap.containsKey(name)) {
             double price = companyMap.get(name).getStockPrice() * shares;
             if (player.getCash() >= price) {
                 player.buyShares(name,shares,price);
             }
             else {
                 throw new YouABrokeAssHoeException("You broke hoe");
             }
        }
        else {
            throw new IllegalArgumentException("unknown company tag");
        }
    }

    public void sellOrder(String name,int shares) throws NotEnoughSharesException {
        if (companyMap.containsKey(name)) {
            if (player.getShares(name) >= shares) {
                double price = companyMap.get(name).getStockPrice() * shares;
                player.sellShares(name,shares,price);
            }
            else {
                throw new NotEnoughSharesException("not enough shares");
            }
        }
        else {
            throw new IllegalArgumentException("unknown company tag");
        }
    }

    public int numberOfShares(String name) {
        return player.getShares(name);
    }

    public void updateNetWorth() {
        player.updateNetWorth(companies);
    }

    public static void stockDayPrint(ArrayList<Company> companies, int days) {
        int l = companies.size() ;

        for (int i = 0; i < days; i++) {
            for (Company company : companies) {
                double currentPrice = company.getStockPrice();
                double newPrice = Stock.priceChange(currentPrice);
                double change = Math.round((newPrice - currentPrice) * 100.0) / 100.0;
                double changePercent = Math.round((newPrice / currentPrice - 1) * 100 * 100) / 100.0;

                company.priceChange(newPrice);

                System.out.println(company.getName() + ": " + company.getStockPrice() + " " + change + " " + changePercent + "%");
            }
            System.out.println();
        }
    }

    public double getStockPrice(String companyName) {
        return companyMap.get(companyName).getStockPrice();
    }

}

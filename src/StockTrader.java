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

    public void sellOrder(String name,int shares) throws NotEnoughSharesException{
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

}

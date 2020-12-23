import java.util.ArrayList;

public class Company {

    public String name;
    public double stockPrice;
    public int totalShares;
    public double marketCap;
    public int availableShares;
    public ArrayList<Double> priceHistory = new ArrayList<>();

    public Company(String name, Double price, int totalShares) {
        this.name = name;
        this.stockPrice = price;
        this.totalShares = totalShares;
        this.marketCap = Stock.cashRound(price*(double)totalShares);
        this.availableShares = totalShares;
    }

    public String getName() {
        return this.name;
    }

    public double getStockPrice() {
        return this.stockPrice;
    }

    public int getTotalShares() {
        return this.totalShares;
    }

    public double getMarketCap() {
        return this.marketCap;
    }

    public int getAvailableShares() {
        return this.availableShares;
    }

    public void priceChange(double newPrice) {
        priceHistory.add(this.stockPrice);
        this.stockPrice = newPrice;
        this.updateMarketCap();
    }

    public ArrayList<Double> getHistory(int days) {
        int l = priceHistory.size();
        if (l-1 >= days) {
            ArrayList<Double> history = new ArrayList<>();
            priceHistory.subList(l - 1 - days, l - 1);
            return history;
        }
        else {
            throw new IllegalArgumentException("Not enough history");
        }
    }

    public void updateMarketCap() {
        this.marketCap = Stock.cashRound(this.stockPrice*(double)this.totalShares);
    }

    public String toString() {
        return name;
    }

    public void addAvailableShares(int amount) {
        this.availableShares += amount;
    }

    public void subtractAvailableShares(int amount) {
        this.availableShares -= amount;
    }
}

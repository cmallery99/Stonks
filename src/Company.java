import java.util.ArrayList;

public class Company {

    public String name;
    public double stockPrice;
    public ArrayList<Double> priceHistory = new ArrayList<>();

    public Company(String name, Double price) {
        this.name = name;
        this.stockPrice = price;
    }

    public String getName() {
        return this.name;
    }

    public double getStockPrice() {
        return this.stockPrice;
    }

    public void priceChange(double a) {
        priceHistory.add(this.stockPrice);
        this.stockPrice = a;
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

    public String toString() {
        return name;
    }
}

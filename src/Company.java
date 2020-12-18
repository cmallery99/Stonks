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

    public double[] get7DayHistory() {
        double [] a = new double[7];
        int l = priceHistory.size();
        for (int i = 0; i < 7; i++) {
            a[i] = priceHistory.get((l-1)-i);
        }
        return a;
    }
}

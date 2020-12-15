import java.util.Random;

public class Stock {
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

    public static double priceChange(double a) {
        Random rand = new Random();

        double change = rand.nextInt(100) - 48;

        change = ((change/100.0) + 100) / 100.0;

        double b = (a*change) * 100;

        double l = Math.round(b);

        return l/100.0;

    }

}

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int l = input.nextInt();
        int c = input.nextInt();
        String[] names = new String[l];
        double[] prices = new double[l];
        double[] newPrices = new double[l];
        double[] changes = new double[l];
        double[] changesPercent = new double[l];

        for (int i = 0; i < l; i++) {
            names[i] = Stock.tickerGen();
            prices[i] = Stock.initialPrice();
        }
        for (int i1 = 0; i1 < l; i1++) {
            System.out.println(names[i1] + ": " + prices[i1]);
        }
        System.out.println();

        for (int i2 = 0; i2 < c; i2++) {
            for (int i3 = 0; i3 < l; i3++) {
                newPrices[i3] = Stock.priceChange(prices[i3]);
                changes[i3] = Math.round((newPrices[i3] - prices[i3] ) * 100.0) / 100.0;
                changesPercent[i3] = Math.round((newPrices[i3] / prices[i3] - 1)*100*100)/100.0;
                prices[i3] = newPrices[i3];
            }
            //for (int i1 = 0; i1 < l; i1++) {
               // System.out.println(names[i1] + ": " + prices[i1] + " " + changes[i1] + " " + changesPercent[i1] + "%");
            //}
            //System.out.println();
        }
        for (int i1 = 0; i1 < l; i1++) {
            System.out.println(names[i1] + ": " + prices[i1] + " " + changes[i1] + " " + changesPercent[i1] + "%");
        }
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter number of companies to generate: ");
        int l = in.nextInt();
        ArrayList<Company> companies = Stock.generateCompanies(l);
        Stock stock = new Stock();
        HashMap<String,Company> companyMap = stock.getCompanyMap(companies);
        ArrayList<String> companyNames = new ArrayList<>();

        for (int i = 0; i < l; i++) {
            companyNames.add(companies.get(i).getName());
        }

        Player player = new Player(companyNames, 10000.00);
        StockTrader stockTrader = new StockTrader(companies,player);

        System.out.println();

        while (true){
            System.out.println("Enter desired action: ");
            String action = in.next();

            if (action.equalsIgnoreCase("day")) {
                System.out.println("Enter number of days: ");
                int l1 = in.nextInt();
                System.out.println("Print or no?: ");
                String pn = in.next();

                if (pn.equals("p")) {
                    StockTrader.stockDayPrint(companies,l1);
                }
                else if (pn.equals("n")) {
                    Stock.stockDayNoPrint(companies,l1);
                }
            }

            else if (action.equalsIgnoreCase("buy")) {
                String name;
                double totalCost;
                while (true) {
                    System.out.println("Enter name of company: ");
                    name = in.next();
                    name = name.toUpperCase();

                    System.out.println("Enter number of shares to buy: ");
                    int shareAmount = in.nextInt();

                    try {
                        stockTrader.buyOrder(name,shareAmount);
                        break;
                    } catch (YouABrokeAssHoeException e) {
                        System.out.println("You a broke hoe");
                    }
                }
                System.out.println("Successfully bought shares");
            }
            else if (action.equalsIgnoreCase("sell")) {
                String name;
                double totalCost;
                while (true) {
                    System.out.println("Enter name of company: ");
                    name = in.next();
                    name = name.toUpperCase();

                    System.out.println("Enter number of shares to sell: ");
                    int shareAmount = in.nextInt();

                    try {
                        stockTrader.sellOrder(name,shareAmount);
                        break;
                    } catch (NotEnoughSharesException e) {
                        System.out.println("Not enough shares to sell");
                    }
                }
                System.out.println("Successfully sold shares");
            }

            else if (action.equalsIgnoreCase("get")) {
                String name;
                while (true) {
                    System.out.println("Enter name of company: ");
                    name = in.next();
                    name = name.toUpperCase();

                    if (companyMap.containsKey(name)) {
                        break;
                    }
                    else {
                        System.out.println("Invalid input");
                    }
                }
                int amount = player.getShares(name);
                System.out.println("You have " + amount + " shares");
            }

            else if (action.equalsIgnoreCase("cash")) {
                System.out.println("You have " + player.getCash());
            }
        }

    }
}

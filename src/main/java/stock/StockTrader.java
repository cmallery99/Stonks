package main.java.stock;

import main.java.companies.Company;
import main.java.players.Player;
import main.java.stonkexceptions.NotEnoughSharesException;
import main.java.stonkexceptions.YouABrokeAssHoeException;

import java.util.ArrayList;
import java.util.HashMap;

public class StockTrader {
    ArrayList<Company> companies;
    Player player;
    Stock stock = new Stock();
    HashMap<String, Company> companyMap;
    int daysOfQuarter;

    public StockTrader(ArrayList<Company> companies, Player player) {
        this.companies = companies;
        this.player = player;
        companyMap = stock.getCompanyMap(companies);
        daysOfQuarter = 0;
    }

    public void buyOrder(String name, int shares) throws YouABrokeAssHoeException, NotEnoughSharesException {
        if (companyMap.containsKey(name)) {
             double price = companyMap.get(name).getStockPrice() * shares;
             if (player.getCash() >= price) {
                 if (companyMap.get(name).getAvailableShares() >= shares) {
                     player.buyShares(name, shares, price);
                     companyMap.get(name).subtractAvailableShares(shares);
                 }
                 else {
                     throw new NotEnoughSharesException("not enough available shares");
                 }
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
                companyMap.get(name).addAvailableShares(shares);
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

    public void stockDayPrint(ArrayList<Company> companies, int days) {
        int l = companies.size() ;

        for (int i = 0; i < days; i++) {
            for (Company company : companies) {
                double currentPrice = company.getStockPrice();
                double newPrice = Stock.priceChange(currentPrice);
                double change = Math.round((newPrice - currentPrice) * 100.0) / 100.0;
                double changePercent = Math.round((newPrice / currentPrice - 1) * 100 * 100) / 100.0;

                company.priceChange(newPrice);

                //System.out.println(company.getName() + ": " + company.getStockPrice() + " " + change + " " + changePercent + "%");
            }
            //System.out.println();
            this.daysOfQuarter++;
            if (daysOfQuarter >= 90) {
                this.quarterTick();
                daysOfQuarter = 0;
            }
        }
    }

    public double getStockPrice(String companyName) {
        return companyMap.get(companyName).getStockPrice();
    }

    public void quarterTick() {
        System.out.println("Quarter complete");
        this.payDividend();
    }

    public void payDividend() {
        double total = 0;
        for (int i = 0; i < companies.size(); i++) {
            double shares = player.getShares(companies.get(i).toString());
            if (shares > 0) {
                double percent = (companies.get(i).getDividendPercent() / 4.0) / 100;
                total += percent * companies.get(i).getStockPrice() * shares;
            }
        }
        total = Stock.cashRound(total);
        player.addCash(total);
        System.out.println("Earned " + total + " in dividends this quarter");
    }

}

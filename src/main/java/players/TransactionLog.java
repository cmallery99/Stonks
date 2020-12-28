package main.java.players;

import main.java.stock.Stock;

public class TransactionLog {
    public String type;
    public String companyName;
    public int shareAmount;
    public double price;
    public double total;

    public TransactionLog(String type,String companyName,int shareAmount, double price) {
        this.type = type;
        this.companyName = companyName;
        this.shareAmount = shareAmount;
        this.price = price;

        this.total = Stock.cashRound((double)shareAmount * price);
        System.out.println("Log created");
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public double cash;
    ArrayList<String> companyNames;
    HashMap<String,Integer> sharesMap = new HashMap<>();

    public Player(ArrayList<String> names,double cash) {
        this.cash = cash;
        this.companyNames = names;
        int l = names.size();

        for (int i = 0; i < l; i++) {
            sharesMap.put(names.get(i),0);
        }

    }

    public double getCash() {
        return cash;
    }

    // TODO Not thread safe
    public void buyShares(String name,int order, double cost) {
        int newValue = sharesMap.get(name) + order;
        sharesMap.replace(name,newValue);
        subtractCash(cost);
    }

    // TODO Not thread safe
    public void sellShares(String name, int order, double cost) {
        int newValue = sharesMap.get(name) - order;
        sharesMap.replace(name,newValue);
        addCash(cost);
    }

    public void addCash(double amount) {
        this.cash = this.cash + amount;
        this.cash = Math.round(this.cash * 100.0) / 100.0;
    }

    public void subtractCash(double amount) {
        this.cash = this.cash - amount;
        this.cash = Math.round(this.cash * 100.0) / 100.0;
    }

    public  int getShares(String name) {
        return sharesMap.getOrDefault(name, 0);
    }

    public HashMap<String,Integer> getAllShares() {
        return sharesMap;
    }

}

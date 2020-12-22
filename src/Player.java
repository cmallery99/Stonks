import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    public double cash;
    ArrayList<String> companyNames;
    HashMap<String,Integer> shares = new HashMap<>();

    public Player(ArrayList<String> names,double cash) {
        this.cash = cash;
        this.companyNames = names;
        int l = names.size();

        for (int i = 0; i < l; i++) {
            shares.put(names.get(i),0);
        }

    }

    public double getCash() {
        return cash;
    }

    // TODO Not thread safe
    public void buyShares(String name,int order, double cost) {
        int newValue = shares.get(name) + order;
        shares.replace(name,newValue);
        cash -= cost;
    }

    // TODO Not thread safe
    public void sellShares(String name, int order, double cost) {
        int newValue = shares.get(name) - order;
        shares.replace(name,newValue);
        cash += cost;
    }

    public void addCash(double amount) {
        this.cash = this.cash + amount;
    }

    public void subtractCash(double amount) {
        this.cash = this.cash - amount;
    }

    public  int getShares(String name) {
        return shares.getOrDefault(name, 0);
    }

}

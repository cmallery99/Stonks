import java.util.ArrayList;

public class PlayerLog {
    ArrayList<TransactionLog> playerLog = new ArrayList<>();


    public void addLogEntry(String type,String companyName, int shareAmount, double price) {
        System.out.println("Adding new log for: " + type  + " " + companyName + " " + shareAmount + " " + price);
        playerLog.add(new TransactionLog(type,companyName,shareAmount,price));
        System.out.println("Log added");
        System.out.println();
    }

    public ArrayList<TransactionLog> getPlayerLog() {
        return playerLog;
    }


}

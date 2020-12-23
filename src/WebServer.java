import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;


public class WebServer {
    public static void main(String[] args) throws Exception {
        ArrayList<Company> companies = Stock.generateCompanies(5);
        Stock stock = new Stock();
        HashMap<String,Company> companyMap = stock.getCompanyMap(companies);
        ArrayList<String> companyNames = new ArrayList<>();

        for (Company company : companies) {
            companyNames.add(company.getName());
        }

        Player player = new Player(companyNames, 10000.00);
        StockTrader stockTrader = new StockTrader(companies,player);
        PlayerLog playerLog = new PlayerLog();

        System.out.println();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/companies",new Companies(companies));
        server.createContext("/day", new DayHandler(companies,stockTrader));
        server.createContext("/buy", new StockBuyHandler(stockTrader,playerLog));
        server.createContext("/sell", new StockSellHandler(stockTrader,playerLog));
        server.createContext("/player", new PlayerHandler(player));
        server.createContext("/cashmoney", new CheatHandler(player));
        server.createContext("/log", new PlayerLogHandler(playerLog));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class Companies implements HttpHandler {
        ArrayList<Company> companies;
        public Companies(ArrayList<Company> companies) {
            this.companies = companies;
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            String json;
            try {
                json = new ObjectMapper().writeValueAsString(this.companies);
            }
            catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
                json = "error";
            }
            byte[] response = json.getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
    static class DayHandler implements HttpHandler {
        ArrayList<Company> companies;
        StockTrader stockTrader;

        public DayHandler(ArrayList<Company> companies,StockTrader stockTrader) {
            this.companies = companies;
            this.stockTrader = stockTrader;
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            StockTrader.stockDayPrint(companies,1);
            stockTrader.updateNetWorth();
            byte[] response = "Simulated 1 day".getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
    static class StockBuyHandler implements HttpHandler {
        StockTrader stockTrader;
        PlayerLog playerLog;

        public StockBuyHandler(StockTrader stockTrader, PlayerLog playerLog) {
            this.stockTrader = stockTrader;
            this.playerLog = playerLog;
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            StringBuilder sb = new StringBuilder();
            InputStream ios = t.getRequestBody();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println("hm: " + sb.toString());
            byte[] response;
            String companyName = sb.toString().split(",")[0];
            int shareAmount = Integer.parseInt(sb.toString().split(",")[1]);
            try {
                stockTrader.buyOrder(companyName,shareAmount);
                response = "Buy Successful".getBytes();
                stockTrader.updateNetWorth();
                playerLog.addLogEntry("Buy",companyName,shareAmount,stockTrader.getStockPrice(companyName));
                t.sendResponseHeaders(200, response.length);
            } catch (YouABrokeAssHoeException e) {
                e.printStackTrace();
                response = "You a broke hoe".getBytes();
                t.sendResponseHeaders(400, response.length);
            } catch (NotEnoughSharesException e) {
                e.printStackTrace();
                response = "not enough available shares".getBytes();
                t.sendResponseHeaders(400, response.length);
            } catch (Exception e) {
                e.printStackTrace();
                response = "unknown exception".getBytes();
                t.sendResponseHeaders(500, response.length);
            }

            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
    static class StockSellHandler implements HttpHandler {
        StockTrader stockTrader;
        PlayerLog playerLog;

        public StockSellHandler(StockTrader stockTrader, PlayerLog playerLog) {
            this.stockTrader = stockTrader;
            this.playerLog = playerLog;
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            StringBuilder sb = new StringBuilder();
            InputStream ios = t.getRequestBody();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println("hm: " + sb.toString());
            byte[] response;
            String companyName = sb.toString().split(",")[0];
            int shareAmount = Integer.parseInt(sb.toString().split(",")[1]);
            try {
                stockTrader.sellOrder(companyName,shareAmount);
                response = "Sale Successful".getBytes();
                stockTrader.updateNetWorth();
                //playerLog.addLogEntry("Sell",companyName,shareAmount,stockTrader.getStockPrice(companyName));
                t.sendResponseHeaders(200, response.length);
            } catch (NotEnoughSharesException e) {
                e.printStackTrace();
                response = "Not enough shares to sell".getBytes();
                t.sendResponseHeaders(400, response.length);
            } catch (Exception e) {
                e.printStackTrace();
                response = "unknown exception".getBytes();
                t.sendResponseHeaders(500, response.length);
            }

            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class PlayerHandler implements HttpHandler {
        Player player;

        public PlayerHandler(Player player) {
            this.player = player;
        }


        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            String json;
            try {
                json = new ObjectMapper().writeValueAsString(this.player);
            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
                json = "error";
            }
            byte[] response = json.getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class CheatHandler implements HttpHandler {
        Player player;

        public CheatHandler(Player player) {
            this.player = player;
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }
            player.cashMoney(10000);
            byte[] response = "Money Money".getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    static class PlayerLogHandler implements HttpHandler {
        PlayerLog playerLog;
        ArrayList<TransactionLog> playersLogList;
        public PlayerLogHandler(PlayerLog playerLog) {
            this.playerLog = playerLog;
            this.playersLogList = playerLog.getPlayerLog();
        }
        public void handle(HttpExchange t) throws IOException {
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
                t.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
                t.sendResponseHeaders(204, -1);
                return;
            }

            String json;
            try {
                json = new ObjectMapper().writeValueAsString(this.playersLogList);
            }
            catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
                json = "error";
            }
            byte[] response = json.getBytes();
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}

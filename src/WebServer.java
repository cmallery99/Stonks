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

        System.out.println();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/companies",new Companies(companies));
        server.createContext("/day", new DayHandler(companies,stockTrader));
        server.createContext("/buy", new StockBuyHandler(stockTrader));
        server.createContext("/sell", new StockSellHandler(stockTrader));
        server.createContext("/player", new PlayerHandler(player));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class Companies implements HttpHandler {
        private ArrayList<Company> companies;
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
        private ArrayList<Company> companies;
        private StockTrader stockTrader;

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
        private StockTrader stockTrader;

        public StockBuyHandler(StockTrader stockTrader) {
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

            StringBuilder sb = new StringBuilder();
            InputStream ios = t.getRequestBody();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println("hm: " + sb.toString());
            byte[] response;
            try {
                stockTrader.buyOrder(sb.toString().split(",")[0],Integer.parseInt(sb.toString().split(",")[1]));
                response = "Buy Successful".getBytes();
                stockTrader.updateNetWorth();
                t.sendResponseHeaders(200, response.length);
            } catch (YouABrokeAssHoeException e) {
                e.printStackTrace();
                response = "You a broke hoe".getBytes();
                t.sendResponseHeaders(400, response.length);
            }

            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }
    static class StockSellHandler implements HttpHandler {
        private StockTrader stockTrader;

        public StockSellHandler(StockTrader stockTrader) {
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

            StringBuilder sb = new StringBuilder();
            InputStream ios = t.getRequestBody();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            System.out.println("hm: " + sb.toString());
            byte[] response;
            try {
                stockTrader.sellOrder(sb.toString().split(",")[0],Integer.parseInt(sb.toString().split(",")[1]));
                response = "Sale Successful".getBytes();
                stockTrader.updateNetWorth();
                t.sendResponseHeaders(200, response.length);
            } catch (NotEnoughSharesException e) {
                e.printStackTrace();
                response = "Not enough shares to sell".getBytes();
                t.sendResponseHeaders(400, response.length);
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
}

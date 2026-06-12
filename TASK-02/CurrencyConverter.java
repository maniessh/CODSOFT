import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter base currency (e.g. USD): ");
        String base = sc.next().toUpperCase();

        System.out.print("Enter target currency (e.g. INR): ");
        String target = sc.next().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = sc.nextDouble();

        try {
            String urlString = "https://api.exchangerate-api.com/v4/latest/" + base;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String json = response.toString();

            // Extract the rate for target currency using simple string parsing
            String searchKey = "\"" + target + "\":";
            int index = json.indexOf(searchKey);

            if (index == -1) {
                System.out.println("Target currency not found!");
                return;
            }

            int start = index + searchKey.length();
            int end = json.indexOf(",", start);
            if (json.charAt(end - 1) == '}') {
                end = json.indexOf("}", start);
            }

            String rateStr = json.substring(start, end).trim();
            double rate = Double.parseDouble(rateStr);

            double converted = amount * rate;

            System.out.println("\n----- Conversion Result -----");
            System.out.println("Exchange Rate: 1 " + base + " = " + rate + " " + target);
            System.out.printf("%.2f %s = %.2f %s\n", amount, base, converted, target);

        } catch (Exception e) {
            System.out.println("Error fetching exchange rate: " + e.getMessage());
        }

        sc.close();
    }
}
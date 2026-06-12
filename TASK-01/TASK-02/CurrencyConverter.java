import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        }
    }
}
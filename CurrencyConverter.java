import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "your_api_key_here"; // Replace with your ExchangeRate-API key

    public static void main(String[] args) {
        // Step 1: Allow user to choose base and target currencies
        try (Scanner scanner = new Scanner(System.in)) {
            // Step 1: Allow user to choose base and target currencies
            System.out.print("Enter the base currency code (e.g., USD): ");
            String baseCurrency = scanner.nextLine().toUpperCase();
            
            System.out.print("Enter the target currency code (e.g., EUR): ");
            String targetCurrency = scanner.nextLine().toUpperCase();
            
            // Step 2: Fetch real-time exchange rate
            double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);
            if (exchangeRate == -1) {
                System.out.println("Failed to fetch exchange rate. Exiting...");
                return;
            }
            
            // Step 3: Take amount input from user
            System.out.print("Enter the amount to convert: ");
            double amount = scanner.nextDouble();
            
            // Step 4: Perform currency conversion
            double convertedAmount = amount * exchangeRate;
            
            // Step 5: Display result
            DecimalFormat df = new DecimalFormat("#.##"); // Format to two decimal places
            System.out.println("\nConverted amount: " + df.format(convertedAmount) + " " + targetCurrency);
        }
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            URL url = new URL("https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/" + baseCurrency + "/" + targetCurrency);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            StringBuilder response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            if (jsonObject.getString("result").equals("success")) {
                return jsonObject.getDouble("conversion_rate");
            } else {
                System.out.println("Error: " + jsonObject.getString("error-type"));
                return -1;
            }

        } catch (IOException e) {
            return -1;
        }
    }
}

    
    


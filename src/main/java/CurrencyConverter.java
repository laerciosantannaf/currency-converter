import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class CurrencyConverter {

  static Logger log = Logger.getLogger("log");
  static final String API_KEY = "319b165b48bb3a7d3baf";
  static final String API_URL = "https://free.currconv.com/api/v7/convert?q=USD_BRL&compact=ultra&apiKey=%s";
  static final int DECIMAL_NUMBER = 2;

  public static void main(String[] args) throws Exception {
    HttpURLConnection con = (HttpURLConnection) new URL(String.format(API_URL, API_KEY)).openConnection();
    con.setRequestMethod("GET");

    int status = con.getResponseCode();
    if (status != 200) throw new Exception("Content not found");

    String responseContent = getResponseContent(con);
    JSONObject json = new JSONObject(responseContent);
    String value = json.getString("USD_BRL");

    Scanner scan = new Scanner(System.in);
    System.out.println("Enter the USD value: ");
    String valueToConvert = scan.nextLine();

    log.warning("Total in BRL: " + round(value, valueToConvert));
  }

  private static BigDecimal round(String value, String valueToConvert) {
    BigDecimal bd = BigDecimal.valueOf(Float.parseFloat(valueToConvert) * Float.parseFloat(value));
    bd = bd.setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP);
    return bd;
  }

  private static String getResponseContent(HttpURLConnection con) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    StringBuilder content = new StringBuilder();
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      content.append(inputLine);
    in.close();
    con.disconnect();
    return content.toString();
  }
}

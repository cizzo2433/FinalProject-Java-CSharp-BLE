package weatherAPI;

import com.google.gson.Gson;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Weather {
    static double currentTemp;
    static String currentCondition;

    private static final String API_KEY = "3a86deaa95c6eea2a6462d1d879afc68";
    private static final String API_ENDPOINT = "https://api.openweathermap.org/data/3.0/onecall?lat=%s&lon=%s&exclude=minutely,hourly,daily,alerts&appid=%s&units=imperial";

    public static void getWeather(double latitude, double longitude) {
        try {
            URL url = new URL(String.format(API_ENDPOINT, latitude, longitude, API_KEY));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            BufferedReader weatherReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder weatherResponse = new StringBuilder();
            String line;
            while ((line = weatherReader.readLine()) != null) {
                weatherResponse.append(line);
            }
            weatherReader.close();

            Gson gson = new Gson();
            WeatherData weatherData = gson.fromJson(weatherResponse.toString(), WeatherData.class);

            currentTemp = weatherData.current.temp;
            currentCondition = weatherData.current.weather[0].main;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving weather information.");
        }
    }
}
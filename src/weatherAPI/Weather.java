package weatherAPI;

import com.google.gson.Gson;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Utilizes the OpenWeatherMap OneCall 3.0 API to fetch current weather data for a given
 * location
 */

public class Weather {
    public static double currentTemp;          // Current temperature in ºF
    public static String currentCondition;     // Current weather condition
    // API Key for OpenWeatherMap OneCall 3.0 API
    private static final String API_KEY = "3a86deaa95c6eea2a6462d1d879afc68";
    // API Endpoint
    private static final String API_ENDPOINT = "https://api.openweathermap.org/data/3.0/onecall?lat=%s&lon=%s&exclude=minutely,hourly,daily,alerts&appid=%s&units=imperial";

    /**
     * Overloaded method to accept a String location and call Geocoder directly
     * @param location
     */
    public static void getWeather(String location) {
        Geocoder.geocode(location);
        getWeather(Geocoder.lat, Geocoder.lon);
    }

    /**
     * Utilizes the OpenWeatherMap OneCall 3.0 API to gather
     * current weather data for a given location.
     * @param latitude      Latitude of desired location
     * @param longitude     Longitude of desired location
     */
    public static void getWeather(double latitude, double longitude) {
        try {
            URL url = new URL(String.format(API_ENDPOINT, latitude, longitude, API_KEY)); // Generate URL for API call
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();    // Open connection to API
            BufferedReader weatherReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            // StringBuilder will store the API response
            StringBuilder weatherResponse = new StringBuilder();
            String line;
            while ((line = weatherReader.readLine()) != null) {
                weatherResponse.append(line);
            }
            weatherReader.close();
            // GSON object to parse data from API response and store in WeatherData class
            Gson gson = new Gson();
            WeatherData weatherData = gson.fromJson(weatherResponse.toString(), WeatherData.class);
            // Set currentTemp and currentCondition fields
            currentTemp = weatherData.current.temp;
            currentCondition = weatherData.current.weather[0].main;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while retrieving weather information.");
        }
    }
}
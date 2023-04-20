package weatherAPI;

/**
 * This class will hold the weather data gathered from the weatherAPI.Weather class,
 * allowing other classes to access current weather data.
 */
public class WeatherData {
    CurrentWeather current;


    static class CurrentWeather {
        double temp;
        Weather[] weather;
    }

    static class Weather {
        String main;
    }
}
